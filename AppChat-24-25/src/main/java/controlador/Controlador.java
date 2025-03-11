package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ImageIcon;

import appChat.Contacto;
import appChat.ContactoIndividual;
import appChat.Mensaje;
import appChat.RepositorioUsuarios;
import appChat.Usuario;
import persistencia.*;

public class Controlador {
	//Uso del patron Singleton de instancia unica
	private static Controlador unicaInstancia = null;
	
	//Repositorio de usuarios que almacena los usuarios de la aplicacion
	private RepositorioUsuarios repoUsuarios;
	
	//Adaptadores de la base de datos para los grupos
	private GrupoDAO adaptadorGrupo;
	
	//Adaptadores de la base de datos para los mensajes
	private MensajeDAO adaptadorMensaje;
	
	//Adaptadores de la base de datos para los usuarios
	private UsuarioDAO adaptadorUsuario;
	
	//Adaptadores de la base de datos para los contactos
	private ContactoIndividualDAO adaptadorContactoIndividual;
	
	//Usuario actual autenticado usando la aplicación
	private Usuario usuarioActual;
	
	//Contacto en actualmente seleccionado en el chat
	private Contacto contactoActual;
	
	/**
	 * Constructor privado del controlador. 
	 * Inicializa los adaptadores y repositorios
	 * 
	 * SOlo es llamado una vez debido al uso del patrón Singleton
	 */
	private Controlador() {
		inicializarAdaptadores();
		inicializarRepositorios();
	}
	
	private void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorGrupo = factoria.getGrupoDAO();
		adaptadorMensaje = factoria.getMensajeDAO();
		adaptadorUsuario = factoria.getUsuarioDAO();
		adaptadorContactoIndividual = factoria.getContactoIndividualDAO();
	}

	private void inicializarRepositorios() {
		this.repoUsuarios = RepositorioUsuarios.getUnicaInstancia();
	}

	public static Controlador getInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new Controlador();
		}
		return unicaInstancia;
	}
	/**
	 * Realiza el inicio de sesion con telefono y contraseña
	 * 
	 * @param telefono
	 * @param Contraseña
	 */
	public boolean hacerLogin(String telefono, String Contraseña) {
	    boolean resultado = false;
	    
	    if (telefono.isEmpty() || Contraseña.isEmpty()) {
	        System.err.println("❌ Error: Teléfono o contraseña vacíos.");
	        return resultado;
	    }
	    
	    Usuario usuario = repoUsuarios.getUsuario(telefono);
	    
	    if (usuario == null) {
	        System.err.println("❌ Error: No se encontró un usuario con el teléfono " + telefono);
	        return resultado;
	    }
	    
	    if (usuario.getContraseña().equals(Contraseña)) {
	        this.usuarioActual = usuario;
	        System.out.println("✅ Usuario autenticado: " + usuario.getNombre());
	        System.out.println("🔹 Contactos del usuario autenticado: " + usuario.getContactos().size());
	        resultado = true;
	    } else {
	        System.err.println("❌ Error: Contraseña incorrecta.");
	    }
	    
	    return resultado;
	}

	
	public boolean registrarUsuario(String nombre, LocalDate fecha, ImageIcon foto, String telefono, 
									String saludo, String contraseña) {
		Usuario usuarioExistente = repoUsuarios.getUsuario(telefono);
		if (usuarioExistente != null) {
			return false;
		}
		
		LocalDate fechaRegistro = LocalDate.now();
		
		Usuario nuevoUsuario = new Usuario(nombre, fecha, foto, contraseña, telefono, saludo, fechaRegistro, false);
		
		//Añadimos al repositorio si no existe
		if (!repoUsuarios.existeUsuario(nuevoUsuario)) {
			repoUsuarios.agregarUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);
			
			//Devuelve true si se ha registrado correctamente
			return hacerLogin(nuevoUsuario.getTelefono(), nuevoUsuario.getContraseña());
		}
		return false;
	}
	
	public Usuario getUsuarioActual() {
		return this.usuarioActual;

	}

	public List<Mensaje> getMensajesUsuario(Contacto contacto) {
		 if (contacto == null) {
		        System.err.println("⚠️ Error en getMensajesUsuario(): Contacto es null.");
		        return new LinkedList<>();
		    }
		
		if (contacto instanceof ContactoIndividual && !((ContactoIndividual) contacto).isUsuario(usuarioActual)) {
			List<Mensaje> mensajes = Stream
					.concat(((ContactoIndividual) contacto).getMensajesEnviados().stream(),
                    ((ContactoIndividual) contacto).getMensajesRecibidos(Optional.of(usuarioActual)).stream()).
					sorted().collect(Collectors.toList());
		return mensajes.stream()
				.filter(m-> !(m.getEmisor().getTelefono()
				.equals(usuarioActual.getTelefono()) && m.isGroup()))
				.collect(Collectors.toList());
		}
		else {
	        System.err.println("⚠️ Error: Contacto no es de tipo ContactoIndividual.");
	        return new LinkedList<>();
	    }
	}
	
	public List<Mensaje> getMensajesUsuarioActual() {
		List<Contacto> contactos = Controlador.getInstancia().getContactosUsuarioActual();
		if (contactos.isEmpty()) {
		    System.err.println("⚠️ El usuario no tiene contactos en la base de datos.");
		} else {
		    System.out.println("✅ Contactos cargados: " + contactos.size());
		}

	    Contacto contacto = buscarContactoDelUsuario(); // Buscar el contacto del usuario autenticado

	    if (contacto == null) {
	        return new ArrayList<>(); // Si no hay contacto, devolver una lista vacía
	    }

	    List<Mensaje> mensajesEnviados = contacto.getMensajesEnviados();
	    
	    // Si se gestionan mensajes recibidos, hay que agregarlos (ver cómo se implementa en Contacto)
	    List<Mensaje> mensajesRecibidos = new ArrayList<>();
	    if (contacto instanceof ContactoIndividual) {
	        mensajesRecibidos = ((ContactoIndividual) contacto).getMensajesRecibidos(Optional.of(usuarioActual));
	    }

	    return Stream.concat(mensajesEnviados.stream(), mensajesRecibidos.stream())
	            .sorted()
	            .collect(Collectors.toList());
	}

	
	private Contacto buscarContactoDelUsuario() {
	    if (usuarioActual == null) {
	        System.err.println("Error: No hay un usuario autenticado.");
	        return null;
	    }

	    // Buscar el contacto que representa al usuario autenticado
		for (ContactoIndividual c : usuarioActual.getContactos().stream().filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c).collect(Collectors.toList())) {
	        if (c.isUsuario(usuarioActual)) { // Método para verificar si es el usuario
	            return c;
	        }
	    }

	    System.err.println("Error: No se encontró un contacto asociado al usuario actual.");
	    return null;
	}
	
	public List<Contacto> getContactosUsuarioActual() {
	    if (usuarioActual == null) {
	        System.err.println("❌ Error: usuarioActual es NULL en getContactosUsuarioActual.");
	        return new LinkedList<>();
	    }

	    List<Contacto> contactos = usuarioActual.getContactosOrdenadosPorMensaje();
	    System.out.println("🔹 Contactos obtenidos para " + usuarioActual.getNombre() + ": " + contactos.size());
	    return contactos;
	}

	
	
	public Optional<ContactoIndividual> getContactoDelUsuarioActual(Usuario usuario) {
	    if (usuario == null) {
	        System.err.println("⚠️ Usuario es null en getContactoDelUsuarioActual().");
	        return Optional.empty();
	    }

	    List<ContactoIndividual> contactosIndividuales = Controlador.getInstancia().getContactosUsuarioActual().stream()
	            .filter(c -> c instanceof ContactoIndividual)
	            .map(c -> (ContactoIndividual) c)
	            .collect(Collectors.toList());

	    if (contactosIndividuales.isEmpty()) {
	        System.err.println("⚠️ No se encontraron contactos individuales para el usuario.");
	        return Optional.empty();
	    }

	    return contactosIndividuales.stream()
	            .filter(c -> c.getUsuario() != null && c.getUsuario().getCodigo() == usuario.getCodigo())
	            .findAny();
	}
	
	/**
	 * Crea el contacto especificado.
	 * 
	 * @param nombre      Nombre del contacto a guardar
	 * @param numTelefono Número de telefono del contacto a guardar
	 * @return El contacto creado. Devuelve null en caso de que ya existiese el
	 *         contacto o el contacto no se corresponda con un usuario real.
	 */
	public ContactoIndividual crearContacto(String nombre, String numTelefono) {
		if (numTelefono.equals(usuarioActual.getTelefono())) {
			return null;
		}
		// Si no tiene el contacto creado lo crea
		if (!usuarioActual.tieneContactoIndividual(nombre)) {
			Optional<Usuario> usuarioOpt = repoUsuarios.buscarUsuario(numTelefono);

			if (usuarioOpt.isPresent()) {
				
				ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioOpt.get(), numTelefono);
				usuarioActual.añadirContacto(nuevoContacto);
				
				adaptadorContactoIndividual.registrarContacto(nuevoContacto);

				adaptadorUsuario.modificarUsuario(usuarioActual);

				return nuevoContacto;
			}
		}
		return null;
	}
	

	
	public RepositorioUsuarios getRepoUsuarios() {
	    return this.repoUsuarios;
	}

	public ContactoIndividualDAO getAdaptadorContactoIndividual() {
	    return this.adaptadorContactoIndividual;
	}

	public UsuarioDAO getAdaptadorUsuario() {
		// TODO Auto-generated method stub
		return this.adaptadorUsuario;
	}
	
	
}
	

