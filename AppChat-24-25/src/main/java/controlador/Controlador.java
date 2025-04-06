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
import appChat.Grupo;
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
	
	/**
	 * Inicializa los adaptadores de la base de datos
	 */
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

	/**
	 * Inicializa el repositorio de usuarios
	 */
	private void inicializarRepositorios() {
		this.repoUsuarios = RepositorioUsuarios.getUnicaInstancia();
	}

	/**
	 * Devuelve la instancia unica del controlador.
	 * 
	 * @return Controlador
	 */
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
	        return resultado;
	    }
	    
	    Usuario usuario = adaptadorUsuario.recuperarUsuarioPorTelefono(telefono);
	    if (usuario == null) {
	        return resultado;
	    }
	    
	    if (usuario.getContraseña().equals(Contraseña)) {
	        this.usuarioActual = usuario;
	        resultado = true;
	    }
	    
	    return resultado;
	}

	/**
	 * 
	 * Registra un nuevo usuario en la aplicacion.
	 * 
	 * @param nombre
	 * @param fechaNacimiento
	 * @param foto
	 * @param telefono
	 * @param saludo
	 * @param contraseña
	 * @return
	 */
	public boolean registrarUsuario(String nombre, LocalDate fechaNacimiento, ImageIcon foto, String telefono, 
									String saludo, String contraseña) {
		Usuario usuarioExistente = repoUsuarios.getUsuario(telefono);
		if (usuarioExistente != null) {
			return false;
		}
				
		Usuario nuevoUsuario = new Usuario(nombre, foto, contraseña, telefono, saludo, fechaNacimiento, false);
		
		//Añadimos al repositorio si no existe
		if (!repoUsuarios.existeUsuario(nuevoUsuario)) {
			repoUsuarios.agregarUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);
			
			//Devuelve true si se ha registrado correctamente
			return hacerLogin(nuevoUsuario.getTelefono(), nuevoUsuario.getContraseña());
		}
		return false;
	}
	
	/**
	 * Devuelve los mensajes relevantes de un contacto para el usuario actual.
	 * 
	 * @param contacto del que se obtendrán los mensajes.
	 * @return Lista de mensajes relacionados con el contacto.
	 */
	public List<Mensaje> getMensajes(Contacto contacto) {
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
	        return contacto.getMensajesEnviados();
	    }
	}
	

	/**
	 * Devuelve el contacto del usuario autenticado. Si no existe, devuelve null.
	 * 
	 * @return Contacto del usuario autenticado
	 */
	private Contacto buscarContactoDelUsuario() {
	    // Buscar el contacto que representa al usuario autenticado
		for (ContactoIndividual c : usuarioActual.getContactos().stream().filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c).collect(Collectors.toList())) {
	        if (c.isUsuario(usuarioActual)) { // Método para verificar si es el usuario
	            return c;
	        }
	    }
	    return null;
	}
	
	/**
	 * Devuelve los contactos del usuario ordenados por fecha del ultimo mensaje.
	 * 
	 * @return Lista de contactos 
	 */
	public List<Contacto> getContactosUsuarioActual() {
	    if (usuarioActual == null) {
	        return new LinkedList<>();
	    }

	    List<Contacto> contactos = new LinkedList<>(usuarioActual.getContactosOrdenadosPorMensaje());

	    // Buscar mensajes de emisores que no estén en la lista de contactos
	    List<Usuario> emisoresDesconocidos = new ArrayList<>();

	    for (Usuario posibleEmisor : repoUsuarios.getUsuarios()) {
	        if (posibleEmisor.equals(usuarioActual)) continue;

	        boolean yaAgregado = usuarioActual.tieneContactoIndividualPorTelefono(posibleEmisor.getTelefono());
	        if (!yaAgregado) {
	            // Obtener mensajes enviados por este posible emisor
	            List<Mensaje> mensajesEnviados = adaptadorMensaje.getMensajesEnviadosPor(posibleEmisor.getTelefono());

	            boolean haEnviadoAlUsuarioActual = mensajesEnviados.stream()
	                .anyMatch(m -> {
	                    if (m.getReceptor() instanceof ContactoIndividual) {
	                        ContactoIndividual receptor = (ContactoIndividual) m.getReceptor();
	                        return receptor.isUsuario(usuarioActual);
	                    }
	                    return false;
	                });

	            if (haEnviadoAlUsuarioActual) {
	                emisoresDesconocidos.add(posibleEmisor);
	            }
	        }
	    }

	    // Crear contactos temporales para esos emisores
	    for (Usuario emisor : emisoresDesconocidos) {
	        String telefono = emisor.getTelefono();
	        String nombre = telefono; // Se muestra el número como nombre
	        ContactoIndividual contactoAnonimo = new ContactoIndividual(nombre, emisor, telefono);
	        contactos.add(contactoAnonimo);
	    }

	    return contactos;
	}


	
	
	/**
	 * Devuelve el contacto individual del usuario actual.
	 * 
	 * @param usuario del que se quiere obtener el contacto
	 * @return El contacto individual del usuario actual, o null si no existe.
	 */
	public Optional<ContactoIndividual> getContactoDelUsuarioActual(Usuario usuario) {
	    List<ContactoIndividual> contactosIndividuales = Controlador.getInstancia().getContactosUsuarioActual().stream()
	            .filter(c -> c instanceof ContactoIndividual)
	            .map(c -> (ContactoIndividual) c)
	            .collect(Collectors.toList());

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

	    // Verifica si el contacto ya existe por nombre o por teléfono
	    boolean yaExistePorNombre = usuarioActual.tieneContactoIndividual(nombre);
	    boolean yaExistePorTelefono = usuarioActual.tieneContactoIndividual(numTelefono);

	    if (yaExistePorNombre || yaExistePorTelefono) {
	        return null;
	    }

	    Optional<Usuario> usuarioOpt = repoUsuarios.buscarUsuario(numTelefono);

	    if (usuarioOpt.isPresent()) {
	        Usuario usuarioContacto = usuarioOpt.get();

	        ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioContacto, numTelefono);
	        usuarioActual.añadirContacto(nuevoContacto);

	        adaptadorContactoIndividual.registrarContacto(nuevoContacto);
	        adaptadorUsuario.modificarUsuario(usuarioActual);

	        return nuevoContacto;
	    }

	    return null;
	}

	/**
	 * Envia un mensaje al contacto especificado
	 *
	 * @param texto    Texto del mensaje a enviar
	 * @param contaco Contacto al que se enviará el mensaje
	 */
	public void enviarMensaje(String texto, Contacto contacto) {
	    if (usuarioActual == null || contacto == null || texto == null || texto.isEmpty()) return;

		Mensaje mensaje = null;

		if (contacto instanceof ContactoIndividual) {
			if (!isEnListaContactos(contacto)) {
				crearContactoAnonimo((ContactoIndividual) contacto);
			}
			
			mensaje = new Mensaje(texto, LocalDateTime.now(), usuarioActual, contacto);
			contacto.enviarMensaje(mensaje);

			adaptadorMensaje.registrarMensaje(mensaje);
			ContactoIndividual ci = (ContactoIndividual) contacto;
			if (ci.getCodigo() != 0 && PoolDAO.getUnicaInstancia().contieneID(ci.getCodigo())) {
			    adaptadorContactoIndividual.modificarContacto(ci);
			}		
		} else if (contacto instanceof Grupo) {
			
		}
	}


	/**
	 * Envia un mensaje al contacto especificado
	 *
	 * @param emoticono Emoticono del mensaje a enviar
	 * @param contacto Contacto al que se enviará el mensaje
	 */
	public void enviarMensaje(int emoticono, Contacto contacto) {
	    if (usuarioActual == null || contacto == null) return;

	    Mensaje mensaje = new Mensaje(emoticono, java.time.LocalDateTime.now(), usuarioActual, contacto);
	    contacto.enviarMensaje(mensaje);

	    adaptadorMensaje.registrarMensaje(mensaje);

	    if (contacto instanceof ContactoIndividual) {
	        adaptadorContactoIndividual.modificarContacto((ContactoIndividual) contacto);
	    } else if (contacto instanceof Grupo) {
	        // lógica para grupo si es necesario
	    }
	}

	private boolean isEnListaContactos(Contacto contacto) {
		ContactoIndividual contactoIndividual = (ContactoIndividual) contacto;
		return usuarioActual.getContactos().stream().filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c)
				.anyMatch(c -> c.getTelefono().equals(contactoIndividual.getTelefono()));
	}
	
	private void crearContactoAnonimo(ContactoIndividual contacto) {
	    Optional<Usuario> usuarioOpt = repoUsuarios.buscarUsuario(contacto.getTelefono());
	    
	    if (usuarioOpt.isEmpty()) return;

	    Usuario receptor = usuarioOpt.get();

	    // Si el usuario actual NO está en su lista de contactos, creamos uno nuevo
	    if (!receptor.tieneContactoIndividualPorTelefono(usuarioActual.getTelefono())) {
	        ContactoIndividual nuevo = new ContactoIndividual(
	            usuarioActual.getTelefono(), 
	            usuarioActual,
	            usuarioActual.getTelefono()  
	        );

	        receptor.añadirContacto(nuevo);
	        adaptadorContactoIndividual.registrarContacto(nuevo);
	        adaptadorUsuario.modificarUsuario(receptor);
	    }
	}
	
	/**
	 * Añade un grupo al usuario actual y lo guarda en la base de datos.
	 *
	 * @param grupo Grupo a añadir
	 */
	public void añadirGrupo(Grupo grupo) {
	    if (grupo == null || usuarioActual == null) return;

	    usuarioActual.añadirContacto(grupo);         // Agrega el grupo como contacto
	    adaptadorGrupo.registrarGrupo(grupo);        // Lo guarda en la base de datos
	    adaptadorUsuario.modificarUsuario(usuarioActual); // Persiste el usuario con su nuevo grupo
	}



	
	public RepositorioUsuarios getRepoUsuarios() {
	    return this.repoUsuarios;
	}

	public ContactoIndividualDAO getAdaptadorContactoIndividual() {
	    return this.adaptadorContactoIndividual;
	}

	public UsuarioDAO getAdaptadorUsuario() {
		return this.adaptadorUsuario;
	}
	
	public Usuario getUsuarioActual() {
		return this.usuarioActual;

	}
	
	
}
	

