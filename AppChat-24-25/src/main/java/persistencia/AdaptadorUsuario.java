package persistencia;

import appChat.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Clase que implementa el Adaptador de Usuario para base de datos
 */
public class AdaptadorUsuario implements UsuarioDAO {
	
	/**
	 * Instancia única de la clase AdaptadorUsuario (Singleton)
	 */
	private static AdaptadorUsuario unicaInstancia = null;
	/**
	 * Servicio de persistencia para la base de datos
	 */
	private static ServicioPersistencia servPersistencia;
	
	/**
	 * Constructor privado Singleton
	 */
	private AdaptadorUsuario() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	/**
	 * Devuelve la única instancia unica de la clase
	 * Garantiza Singleton
	 * 
	 * @return unicaInstancia
	 */
	public static AdaptadorUsuario getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorUsuario();
		return unicaInstancia;
	}

	/**
	 * Registra un usuario en la base de datos
	 * 
	 * @param usuario a registrar
	 */
	@Override
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;
		
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {			
		}
		
		if(eUsuario != null) 
			return;
		
		if (existeUsuario(usuario)) {
	        return;
	    }

		usuario.getContactos().forEach(c -> {
            AdaptadorContactoIndividual.getUnicaInstancia().registrarContacto((ContactoIndividual) c);
            });
		
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<>(Arrays.asList(
				new Propiedad("nombre", usuario.getNombre()), 
				new Propiedad("telefono", usuario.getTelefono()),
				new Propiedad("contraseña", usuario.getContraseña()),
				new Propiedad("saludo", usuario.getSaludo()),
				new Propiedad("premium", String.valueOf(usuario.isPremium())),
				new Propiedad("foto", obtenerRutaRelativaFoto(usuario.getFotoPerfil())),
				new Propiedad("contactos", obtenerCodigosContactoIndividual(usuario.getContactos())),
				new Propiedad("fechaRegistro", usuario.getFechaRegistro().toString()),
				new Propiedad("grupos", obtenerCodigosGrupos(usuario.getGrupos())))));
				
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setCodigo(eUsuario.getId());
		
		PoolDAO.getUnicaInstancia().añadirObjeto(usuario.getCodigo(), usuario);
	}

	/**
	 * Obtiene la ruta relativa de la foto si proviene de recursos.
	 * Si no, devuelve la descripción tal cual.
	 */
	private String obtenerRutaRelativaFoto(ImageIcon foto) {
	    String desc = foto.getDescription();

	    if (desc == null) return "";

	    // Si es una ruta que empieza por "file:/", es desde recursos del proyecto
	    if (desc.contains("/recursos/")) {
	        // Extrae solo desde /recursos/... en adelante
	        int idx = desc.indexOf("/recursos/");
	        return desc.substring(idx);
	    }

	    return desc; // Ruta absoluta local
	}

	/**
	 * Borra un usuario de la base de datos
	 * 
	 * @param usuario a borrar
	 */
	@Override
	public void borrarUsuario(Usuario usuario) {
	    Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
	    servPersistencia.borrarEntidad(eUsuario);
	    
	    // Eliminar contactos individuales asociados
	    usuario.getContactos().forEach(c -> {
	        if (c instanceof ContactoIndividual) {
	            AdaptadorContactoIndividual.getUnicaInstancia().borrarContacto((ContactoIndividual) c);
	        }
	    });
	    
	    if (PoolDAO.getUnicaInstancia().contieneID(usuario.getCodigo())) {
	        PoolDAO.getUnicaInstancia().eliminarObjeto(usuario.getCodigo());
	    }
	}


	/**
	 * Recupera un usuario de la base de datos a traves del codigo
	 * 
	 * @param codigo del usuario
	 * @return usuario
     */
	@Override
	public Usuario recuperarUsuario(int codigo) {
	    if (PoolDAO.getUnicaInstancia().contieneID(codigo)) {
	        return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
	    }

	    Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);

	    String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
	    String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
	    String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
	    String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
	    Boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium"));
	    LocalDate fechaRegistro = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaRegistro"));
	    String direccionFoto = servPersistencia.recuperarPropiedadEntidad(eUsuario, "foto");

	    
	    ImageIcon fotoPerfil = null;

	    if (direccionFoto != null) {
	        try {
	            if (direccionFoto.startsWith("/recursos/")) {
	                URL url = getClass().getResource(direccionFoto);
	                fotoPerfil = new ImageIcon(url);
	            } else {
	                // Imagen local del PC (ruta absoluta)
	                fotoPerfil = new ImageIcon(direccionFoto);
	            }
	        } catch (Exception e) {
	            System.err.println(" Error cargando imagen: " + direccionFoto);
	            fotoPerfil = new ImageIcon(getClass().getResource("/recursos/account.png"));
	        }

	        if (fotoPerfil != null) {
	            fotoPerfil.setDescription(direccionFoto);
	        }
	    }



	    Usuario usuario = new Usuario(nombre, fotoPerfil, contraseña, telefono, saludo, fechaRegistro, premium);
	    usuario.setCodigo(codigo);

	    // Añadir al Pool antes de reconstruir contactos y grupos
	    PoolDAO.getUnicaInstancia().añadirObjeto(codigo, usuario);

	    // Recuperar contactos y grupos asociados   
	    List<ContactoIndividual> contactos = obtenerContactosDesdeCodigos(
	        servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos"));
	    contactos.forEach(usuario::añadirContacto);

	    List<Grupo> grupos = obtenerGruposDesdeCodigos(
	        servPersistencia.recuperarPropiedadEntidad(eUsuario, "grupos"));
	    grupos.forEach(usuario::añadirContacto);

	    return usuario;
	}

	/**
	 * Modifica un usuario de la base de datos
	 * 
	 * @param usuario a modificar
	 */
	@Override
	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		for (Propiedad p : eUsuario.getPropiedades()) {
			switch (p.getNombre()) {
			case "nombre":
				p.setValor(usuario.getNombre());
				break;
			case "fecha":
				String codigosContactos = usuario.getContactos().stream()
		        .map(c -> String.valueOf(c.getCodigo()))
		        .collect(Collectors.joining(" ")); // 🔹 Almacena solo los códigos separados por espacio
				p.setValor(codigosContactos);

				break;
			case "telefono":
				p.setValor(usuario.getTelefono());
				break;
			case "contraseña":
				p.setValor(usuario.getContraseña());
				break;
			case "saludo":
				p.setValor(usuario.getSaludo());
				break;
			case "premium":
				p.setValor(String.valueOf(usuario.isPremium()));
				break;
			case "foto":
				p.setValor(usuario.getFotoPerfil().getDescription());
				break;
			case "contactos":
				p.setValor(obtenerCodigosContactoIndividual(usuario.getContactos()));
				break;
			case "fechaRegistro":
				p.setValor(usuario.getFechaRegistro().toString());
				break;
			case "grupos":
				p.setValor(obtenerCodigosGrupos(usuario.getGrupos()));
				break;
			}
			servPersistencia.modificarPropiedad(p);
		}
		
	}
	
	/**
	 * Recupera todos los usuarios de la base de datos
	 * 
	 * @return lista de usuarios
	 */
	@Override
	public List<Usuario> recuperarTodosUsuarios() {
		List<Usuario> usuarios = new LinkedList<>();
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
	
		return usuarios;
	}
	
	/**
	 * Recupera un usuario de la base de datos a traves del telefono
	 * 
	 * @param telefono del usuario
	 * @return usuario
	 */
	@Override
	public Usuario recuperarUsuarioPorTelefono(String telefono) {
	    return recuperarTodosUsuarios().stream()
	        .filter(u -> u.getTelefono().equals(telefono))
	        .findFirst()
	        .orElse(null);
	}
	
	/**
	 * Obtiene los contactos asociados a un usuario
	 * 
	 * @param codigos a convertir
	 * @return lista de contactos
	 */
	private List<ContactoIndividual> obtenerContactosDesdeCodigos(String codigos) {
	    List<ContactoIndividual> contactos = new LinkedList<>();

	    if (codigos == null || codigos.trim().isEmpty() || codigos.equals("[]") ){
	        return contactos;
	    }
	    
	    StringTokenizer strTok = new StringTokenizer(codigos, " ");
	    AdaptadorContactoIndividual adaptadorC = AdaptadorContactoIndividual.getUnicaInstancia();

	    while (strTok.hasMoreTokens()) {
	        String token = strTok.nextToken().trim();

	        try {
	            int codigo = Integer.parseInt(token);
	            ContactoIndividual contacto = adaptadorC.recuperarContacto(codigo);
	            contactos.add(contacto);
	        } catch (NumberFormatException e) {
	            System.err.println("⚠️ Error al convertir código de contacto: " + token);
	        }
	    }
	    return contactos;
	}

	/**
	 * Convierte un String de codigos en una lista de grupos
	 * 
	 * @param codigos a convertir
	 * @return lista de grupos
	 */
	private List<Grupo> obtenerGruposDesdeCodigos(String codigos) {
	    List<Grupo> grupos = new LinkedList<>();

	    // Verificar que el string no sea null, vacío o contenga valores incorrectos
	    if (codigos == null || codigos.isEmpty() || codigos.equals("[]")) {
	        return grupos; // Devuelve una lista vacía en lugar de procesar un null
	    }

	    StringTokenizer strTok = new StringTokenizer(codigos, " ");
	    AdaptadorGrupo adaptadorC = AdaptadorGrupo.getUnicaInstancia();

	    while (strTok.hasMoreTokens()) {
	        String token = strTok.nextToken().trim(); // Eliminar espacios en blanco
	        try {
	            int codigo = Integer.parseInt(token); // Intentar convertir en número
	            grupos.add(adaptadorC.recuperarGrupo(codigo));
	        } catch (NumberFormatException e) {
	            System.err.println("⚠️ Error al convertir el código de grupo: " + token);
	        }
	    }
	    return grupos;
	}

	/**
	 * Convierte una lista de contactos a un String de codigos
	 * 
	 * @param contactos a convertir
	 * @return codigo
	 */
	private String obtenerCodigosContactoIndividual(List<Contacto> contactos) {
		return contactos.stream().filter(c -> c instanceof ContactoIndividual)
				.map(c -> String.valueOf(c.getCodigo())).reduce("", (l, c) -> l + c + " ")
				.trim();
	}
	
	/**
	 * Verifica si el usuario existe en la base de datos
	 * 
	 * @param usuario a verificar
	 * @return true si existe, false si no
	 */
	private boolean existeUsuario(Usuario usuario) {
	    return servPersistencia.recuperarEntidad(usuario.getCodigo()) != null;
	}
	
	/**
	 * Convierte una lista de grupos a un String de codigos
	 * 
	 * @param grupos a convertir
	 * @return codigo
	 */
	private String obtenerCodigosGrupos(List<Grupo> grupos) {
	    return grupos.stream()
	            .map(g -> String.valueOf(g.getCodigo()))
	            .collect(Collectors.joining(" ")); // separados por espacio, como los contactos
	}

}
