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
	// Uso del patron Singleton de instancia unica
	private static Controlador unicaInstancia = null;

	// Repositorio de usuarios que almacena los usuarios de la aplicacion
	private RepositorioUsuarios repoUsuarios;

	// Adaptadores de la base de datos
	private GrupoDAO adaptadorGrupo;
	private MensajeDAO adaptadorMensaje;
	private UsuarioDAO adaptadorUsuario;
	private ContactoIndividualDAO adaptadorContactoIndividual;

	// Usuario actual autenticado usando la aplicación
	private Usuario usuarioActual;

	// Constructor privado del controlador
	private Controlador() {
		inicializarAdaptadores();
		inicializarRepositorios();
	}

	// Inicialización de adaptadores
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

	// Inicialización de repositorios
	private void inicializarRepositorios() {
		this.repoUsuarios = RepositorioUsuarios.getUnicaInstancia();
	}

	// Devuelve la instancia única del controlador
	public static Controlador getInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new Controlador();
		}
		return unicaInstancia;
	}

	/**
	 * Realiza el login del usuario en la aplicación
	 * 
	 * @param telefono
	 * @param Contraseña
	 * @return true si el login es exitoso, false en caso contrario
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
	 * Registra un nuevo usuario en la aplicación
	 * 
	 * @param nombre
	 * @param fechaNacimiento
	 * @param foto
	 * @param telefono
	 * @param saludo
	 * @param contraseña
	 * @return true si el registro es exitoso, false en caso contrario
	 */
	public boolean registrarUsuario(String nombre, LocalDate fechaNacimiento, ImageIcon foto, String telefono,
			String saludo, String contraseña) {
		Usuario usuarioExistente = repoUsuarios.getUsuario(telefono);
		if (usuarioExistente != null) {
			return false;
		}

		Usuario nuevoUsuario = new Usuario(nombre, foto, contraseña, telefono, saludo, fechaNacimiento, false);

		if (!repoUsuarios.existeUsuario(nuevoUsuario)) {
			repoUsuarios.agregarUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);
			return hacerLogin(nuevoUsuario.getTelefono(), nuevoUsuario.getContraseña());
		}

		return false;
	}

	/**
	 * Obtiene la lista de mensajes de un contacto específico
	 * 
	 * @param contacto
	 * @return Lista de mensajes del contacto
	 */
	public List<Mensaje> getMensajes(Contacto contacto) {
		if (contacto instanceof ContactoIndividual && !((ContactoIndividual) contacto).isUsuario(usuarioActual)) {
			List<Mensaje> mensajes = Stream
					.concat(((ContactoIndividual) contacto).getMensajesEnviados().stream(),
							((ContactoIndividual) contacto).getMensajesRecibidos(Optional.of(usuarioActual)).stream())
					.sorted().collect(Collectors.toList());
			return mensajes.stream()
					.filter(m -> !(m.getEmisor().getTelefono()
							.equals(usuarioActual.getTelefono()) && m.isGroup()))
					.collect(Collectors.toList());
		} else {
			return contacto.getMensajesEnviados();
		}
	}

	/**
	 * Obtiene la lista de contactos del usuario actual
	 * 
	 * @return Lista de contactos del usuario actual
	 */
	public List<Contacto> getContactosUsuarioActual() {
		if (usuarioActual == null) {
			return new LinkedList<>();
		}

		List<Contacto> contactos = new LinkedList<>(usuarioActual.getContactosOrdenadosPorMensaje());
		List<Usuario> emisoresDesconocidos = new ArrayList<>();

		for (Usuario posibleEmisor : repoUsuarios.getUsuarios()) {
			if (posibleEmisor.equals(usuarioActual)) continue;

			boolean yaAgregado = usuarioActual.tieneContactoIndividualPorTelefono(posibleEmisor.getTelefono());
			if (!yaAgregado) {
				List<Mensaje> mensajesEnviados = adaptadorMensaje.getMensajesEnviadosPor(posibleEmisor.getTelefono());
				boolean haEnviadoAlUsuarioActual = mensajesEnviados.stream()
						.anyMatch(m -> m.getReceptor() instanceof ContactoIndividual &&
								((ContactoIndividual) m.getReceptor()).isUsuario(usuarioActual));

				if (haEnviadoAlUsuarioActual) {
					emisoresDesconocidos.add(posibleEmisor);
				}
			}
		}

		for (Usuario emisor : emisoresDesconocidos) {
			String telefono = emisor.getTelefono();
			ContactoIndividual contactoAnonimo = new ContactoIndividual(telefono, emisor, telefono);
			contactos.add(contactoAnonimo);
		}

		// Asignar correctamente la foto a los ContactosIndividuales
		for (Contacto contacto : contactos) {
	        if (contacto instanceof ContactoIndividual) {
	            ContactoIndividual cIndividual = (ContactoIndividual) contacto;
	            Optional<Usuario> usuarioReal = repoUsuarios.buscarUsuario(cIndividual.getTelefono());
	            usuarioReal.ifPresent(user -> {
	                if (user.getFotoPerfil() != null) {
	                    cIndividual.setFoto(user.getFotoPerfil());
	                }
	            });
	        }
	    }
		
		
		return contactos;
	}

	/**
	 * Obtiene un contacto específico del usuario actual
	 * 
	 * @param usuario
	 * @return ContactoIndividual correspondiente al usuario
	 */
	public Optional<ContactoIndividual> getContactoDelUsuarioActual(Usuario usuario) {
		List<ContactoIndividual> contactosIndividuales = getContactosUsuarioActual().stream()
				.filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c)
				.collect(Collectors.toList());

		return contactosIndividuales.stream()
				.filter(c -> c.getUsuario() != null && c.getUsuario().getCodigo() == usuario.getCodigo())
				.findAny();
	}

	/**
	 * Crea un nuevo contacto individual
	 * 
	 * @param nombre
	 * @param numTelefono
	 * @return ContactoIndividual creado, o null si no se pudo crear
	 */
	public ContactoIndividual crearContacto(String nombre, String numTelefono) {
		if (numTelefono.equals(usuarioActual.getTelefono())) {
			return null;
		}

		boolean yaExistePorNombre = usuarioActual.tieneContactoIndividual(nombre);
		boolean yaExistePorTelefono = usuarioActual.tieneContactoIndividual(numTelefono);

		if (yaExistePorNombre || yaExistePorTelefono) {
			return null;
		}

		Optional<Usuario> usuarioOpt = repoUsuarios.buscarUsuario(numTelefono);

		if (usuarioOpt.isPresent()) {
			Usuario usuarioContacto = usuarioOpt.get();
			ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioContacto, numTelefono);
			nuevoContacto.setFoto(usuarioContacto.getFotoPerfil()); // Añade la foto del usuario
			usuarioActual.añadirContacto(nuevoContacto);
			adaptadorContactoIndividual.registrarContacto(nuevoContacto);
			adaptadorUsuario.modificarUsuario(usuarioActual);
			return nuevoContacto;
		}
		return null;
	}

	/**
	 * Envía un mensaje a un contacto específico
	 * 
	 * @param texto
	 * @param contacto
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

	        adaptadorContactoIndividual.modificarContacto((ContactoIndividual) contacto);
	    } else if (contacto instanceof Grupo) {
	        Grupo grupo = (Grupo) contacto;
	        for (ContactoIndividual c : grupo.getIntegrantes()) {
	            if (!isEnListaContactos(c)) {
	                crearContactoAnonimo(c);
	            }

	            Mensaje mensajeGrupo = new Mensaje(texto, LocalDateTime.now(), usuarioActual, c);
	            mensajeGrupo.setGroup(true);
	            c.enviarMensaje(mensajeGrupo);
	            adaptadorMensaje.registrarMensaje(mensajeGrupo);
	            adaptadorContactoIndividual.modificarContacto(c);
	        }

	        mensaje = new Mensaje(texto, LocalDateTime.now(), usuarioActual, contacto);
	        contacto.enviarMensaje(mensaje);
	        adaptadorMensaje.registrarMensaje(mensaje);
	        adaptadorGrupo.modificarGrupo(grupo);
	    }
	}

	/**
	 * Envía un mensaje con un emoticono a un contacto específico
	 * 
	 * @param emoticono
	 * @param contacto
	 */
	public void enviarMensaje(int emoticono, Contacto contacto) {
	    if (usuarioActual == null || contacto == null) return;

	    Mensaje mensaje = null;

	    if (contacto instanceof ContactoIndividual) {
	        if (!isEnListaContactos(contacto)) {
	            crearContactoAnonimo((ContactoIndividual) contacto);
	        }

	        mensaje = new Mensaje(emoticono, LocalDateTime.now(), usuarioActual, contacto);
	        contacto.enviarMensaje(mensaje);
	        adaptadorMensaje.registrarMensaje(mensaje);
	        adaptadorContactoIndividual.modificarContacto((ContactoIndividual) contacto);
	    } else if (contacto instanceof Grupo) {
	        Grupo grupo = (Grupo) contacto;
	        for (ContactoIndividual c : grupo.getIntegrantes()) {
	            if (!isEnListaContactos(c)) {
	                crearContactoAnonimo(c);
	            }

	            Mensaje mensajeGrupo = new Mensaje(emoticono, LocalDateTime.now(), usuarioActual, c);
	            mensajeGrupo.setGroup(true);
	            c.enviarMensaje(mensajeGrupo);
	            adaptadorMensaje.registrarMensaje(mensajeGrupo);
	            adaptadorContactoIndividual.modificarContacto(c);
	        }

	        mensaje = new Mensaje(emoticono, LocalDateTime.now(), usuarioActual, contacto);
	        contacto.enviarMensaje(mensaje);
	        adaptadorMensaje.registrarMensaje(mensaje);
	        adaptadorGrupo.modificarGrupo(grupo);
	    }
	}


	/**
	 * Crea un nuevo grupo y lo añade a la lista de contactos del usuario actual
	 * 
	 * @param grupo
	 */
	public void añadirGrupo(Grupo grupo) {
		if (grupo == null || usuarioActual == null) return;
		usuarioActual.añadirContacto(grupo);
		adaptadorGrupo.registrarGrupo(grupo);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}


	/**
	 * Verifica si el contacto ya está en la lista de contactos del usuario actual
	 * 
	 * @param contacto
	 * @return
	 */
	private boolean isEnListaContactos(Contacto contacto) {
	    if (!(contacto instanceof ContactoIndividual)) return false;
	    ContactoIndividual contactoIndividual = (ContactoIndividual) contacto;
	    return usuarioActual.getContactos().stream()
	            .filter(c -> c instanceof ContactoIndividual)
	            .map(c -> (ContactoIndividual) c)
	            .anyMatch(c -> c.getTelefono().equals(contactoIndividual.getTelefono()));
	}

	
	/**
	 * Crea un contacto anónimo para el usuario actual si no existe
	 * 
	 * @param contacto
	 */
	private void crearContactoAnonimo(ContactoIndividual contacto) {
	    Optional<Usuario> usuarioOpt = repoUsuarios.buscarUsuario(contacto.getTelefono());
	    if (usuarioOpt.isEmpty()) return;

	    Usuario receptor = usuarioOpt.get();

	    // Asegurar que el receptor tenga al usuario actual en sus contactos
	    if (!receptor.tieneContactoIndividualPorTelefono(usuarioActual.getTelefono())) {
	        ContactoIndividual anonimoParaReceptor = new ContactoIndividual(
	            usuarioActual.getTelefono(), usuarioActual, usuarioActual.getTelefono());

	        ImageIcon fotoAnonima = new ImageIcon(getClass().getResource("/recursos/account.png"));
	        anonimoParaReceptor.setFoto(fotoAnonima);

	        receptor.añadirContacto(anonimoParaReceptor);
	        adaptadorContactoIndividual.registrarContacto(anonimoParaReceptor);
	        adaptadorUsuario.modificarUsuario(receptor);
	    }

	    // Asegurar que el usuario actual tenga al receptor en sus contactos
	    if (!usuarioActual.tieneContactoIndividualPorTelefono(receptor.getTelefono())) {
	        ContactoIndividual anonimoParaActual = new ContactoIndividual(
	            receptor.getTelefono(), receptor, receptor.getTelefono());

	        anonimoParaActual.setFoto(receptor.getFotoPerfil());

	        usuarioActual.añadirContacto(anonimoParaActual);
	        adaptadorContactoIndividual.registrarContacto(anonimoParaActual);
	        adaptadorUsuario.modificarUsuario(usuarioActual);
	    }
	}
	
	public void renombrarContacto(Contacto contacto, String nuevoNombre) {
	    if (contacto == null || nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

	    contacto.setNombre(nuevoNombre);

	    if (contacto instanceof ContactoIndividual) {
	        adaptadorContactoIndividual.modificarContacto((ContactoIndividual) contacto);
	    } else if (contacto instanceof Grupo) {
	        adaptadorGrupo.modificarGrupo((Grupo) contacto);
	    }

	    adaptadorUsuario.modificarUsuario(usuarioActual);
	}



	//Getters y Setters 
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