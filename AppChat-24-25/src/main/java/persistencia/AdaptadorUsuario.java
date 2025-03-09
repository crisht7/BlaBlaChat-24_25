package persistencia;

import appChat.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorUsuario implements UsuarioDAO {
	
	private static AdaptadorUsuario unicaInstancia = null;
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
	        System.out.println("ℹ️ El usuario ya está registrado: " + usuario.getNombre());
	        return;
	    }
		
		
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<>(Arrays.asList(
				new Propiedad("nombre", usuario.getNombre()), 
				new Propiedad("fecha", usuario.getFecha().toString()), 
				new Propiedad("telefono", usuario.getTelefono()),
				new Propiedad("contraseña", usuario.getContraseña()),
				new Propiedad("saludo", usuario.getSaludo()),
				new Propiedad("premium", String.valueOf(usuario.isPremium())),
				new Propiedad("foto", usuario.getFotoPerfil().getDescription()),
				new Propiedad("contactos", usuario.getContactos().toString()), //TODO: ¿Es la forma?
				new Propiedad("fechaRegistro", usuario.getFechaRegistro().toString()),
				new Propiedad("Grupos", usuario.getGrupos().toString()))));
		
		usuario.getContactos().forEach(c -> {
            AdaptadorContactoIndividual.getUnicaInstancia().registrarContacto((ContactoIndividual) c);
            });
		
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setCodigo(eUsuario.getId());
		PoolDAO.getUnicaInstancia().añadirObjeto(usuario.getCodigo(), usuario);
	}

	@Override
	public void borrarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Usuario recuperarUsuario(int codigo) {
		if (PoolDAO.getUnicaInstancia().contieneID(codigo)) {
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		
		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		LocalDate fecha = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fecha"));
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
		String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
		Boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium"));
		LocalDate fechaRegistro = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaRegistro"));
		String direccionFoto = servPersistencia.recuperarPropiedadEntidad(eUsuario, "foto");
		
		ImageIcon fotoPerfil = new ImageIcon(direccionFoto);
		
		Usuario usuario = new Usuario(nombre, fecha, fotoPerfil, contraseña, telefono, saludo, fechaRegistro, premium);
		
	    usuario.setContactos(new LinkedList<>());
		
		// Recuperar contactos y grupos asociados
		List<ContactoIndividual> contactos = obtenerContactosDesdeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos"));
			contactos.forEach(usuario::añadirContacto);

		List<Grupo> grupos = obtenerGruposDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "grupos"));
		grupos.forEach(usuario::añadirContacto
				);
		
		PoolDAO.getUnicaInstancia().añadirObjeto(codigo, usuario);
				
		return usuario;
		
	}

	@Override
	public List<Usuario> recuperarTodosUsuarios() {
		List<Usuario> usuarios = new LinkedList<>();
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
	
		return usuarios;
	}

	@Override
	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		for (Propiedad p : eUsuario.getPropiedades()) {
			switch (p.getNombre()) {
			case "nombre":
				p.setValor(usuario.getNombre());
				break;
			case "fecha":
				p.setValor(usuario.getFecha().toString());
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
				p.setValor(usuario.getContactos().toString());
				break;
			case "fechaRegistro":
				p.setValor(usuario.getFechaRegistro().toString());
				break;
			case "grupos":
				p.setValor(usuario.getGrupos().toString());
				break;
			}
			servPersistencia.modificarPropiedad(p);
		}
		
	}
	
	private List<ContactoIndividual> obtenerContactosDesdeCodigos(String codigos) {
	    List<ContactoIndividual> contactos = new LinkedList<>();

	    // Verificar si la cadena está vacía, es null o contiene "[]"
	    if (codigos == null || codigos.isEmpty() || codigos.equals("[]")) {
	        return contactos; // Devolver una lista vacía en lugar de procesar "[]"
	    }

	    StringTokenizer strTok = new StringTokenizer(codigos, " ");
	    AdaptadorContactoIndividual adaptadorC = AdaptadorContactoIndividual.getUnicaInstancia();

	    while (strTok.hasMoreTokens()) {
	        String token = strTok.nextToken().trim(); // Eliminar espacios en blanco
	        try {
	            int codigo = Integer.parseInt(token); // Intentar convertir en número
	            contactos.add(adaptadorC.recuperarContacto(codigo));
	        } catch (NumberFormatException e) {
	            System.err.println("⚠️ Error al convertir el código de contacto: " + token);
	        }
	    }
	    return contactos;
	}

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

	private boolean existeUsuario(Usuario usuario) {
	    return servPersistencia.recuperarEntidad(usuario.getCodigo()) != null;
	}


}
