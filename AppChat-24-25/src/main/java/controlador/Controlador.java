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
import descuentos.DescuentoFecha;
import descuentos.DescuentoMensaje;
import persistencia.*;

/**
 * Clase Controlador que maneja la lógica principal de la aplicación.
 * Implementa el patrón Singleton para garantizar una única instancia.
 */
public class Controlador {

	/**
	 *  Singleton: única instancia
	 */
	private static Controlador unicaInstancia = null;
	/**
	 *  Repositorio de usuarios
	 */
	private RepositorioUsuarios repoUsuarios;
	/**
	 * Adaptadores de grupos
	 */
	private GrupoDAO adaptadorGrupo;
	/**
	 * Adaptador de mensajes
	 */
	private MensajeDAO adaptadorMensaje;
	/**
	 * Adaptador de usuarios
	 */
	private UsuarioDAO adaptadorUsuario;
	/**
	 *  Adaptador de contactos individuales
	 */
	private ContactoIndividualDAO adaptadorContactoIndividual;
	/**
	 *  Usuario actual autenticado
	 */
	private Usuario usuarioActual;

	// ===================== Constructor e Inicialización =====================

	/**
	 * Constructor privado del controlador
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
	 * Devuelve la instancia única del controlador
	 * @return instancia del Controlador
	 */
	public static Controlador getInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new Controlador();
		}
		return unicaInstancia;
	}

	// ===================== Métodos de Autenticación =====================

	/**
	 * Realiza el login del usuario en la aplicación
	 * @param telefono número de teléfono
	 * @param contraseña contraseña
	 * @return true si el login es exitoso, false en caso contrario
	 */
	public boolean hacerLogin(String telefono, String contraseña) {
		if (telefono.isEmpty() || contraseña.isEmpty()) return false;

		Usuario usuario = adaptadorUsuario.recuperarUsuarioPorTelefono(telefono);
		if (usuario == null) return false;

		if (usuario.getContraseña().equals(contraseña)) {
			this.usuarioActual = usuario;
			return true;
		}

		return false;
	}

	/**
	 * Registra un nuevo usuario en la aplicación
	 * @param nombre nombre del usuario
	 * @param fechaRegistro fecha de registro
	 * @param foto foto de perfil
	 * @param telefono número de teléfono
	 * @param saludo mensaje de saludo
	 * @param contraseña contraseña
	 * @return true si el registro es exitoso, false en caso contrario
	 */
	public boolean registrarUsuario(String nombre, LocalDate fechaRegistro, ImageIcon foto, String telefono,
			String saludo, String contraseña) {
		Usuario usuarioExistente = repoUsuarios.getUsuario(telefono);
		if (usuarioExistente != null) return false;

		Usuario nuevoUsuario = new Usuario(nombre, foto, contraseña, telefono, saludo, fechaRegistro, false);

		if (!repoUsuarios.existeUsuario(nuevoUsuario)) {
			repoUsuarios.agregarUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);
			return hacerLogin(nuevoUsuario.getTelefono(), nuevoUsuario.getContraseña());
		}

		return false;
	}
	// ===================== Métodos de Chats =====================

	/**
	 * Abre un chat con un contacto específico por su número de teléfono.
	 * 
	 * @param telefono contacto con el que se quiere abrir el chat
	 * @return el contacto abierto o null si no se pudo abrir 
	 */
	public Contacto abrirChatConTelefono(String telefono) {
	    if (telefono == null || telefono.isBlank()) return null;

	    Usuario actual = getUsuarioActual();
	    if (actual == null) return null;

	    Optional<ContactoIndividual> existente = actual.getContactosIndividuales().stream()
	        .filter(c -> c.getTelefono().equals(telefono))
	        .findFirst();

	    if (existente.isPresent()) {
	        return existente.get();
	    } else {
	        Optional<Usuario> usuarioDestino = repoUsuarios.buscarUsuario(telefono);
	        if (usuarioDestino.isEmpty()) {
	            return null;
	        }

	        ContactoIndividual contactoNuevo = crearContacto(telefono, telefono);
	        return contactoNuevo;
	    }
	}


	// ===================== Métodos de Contactos =====================

	/**
	 * Obtiene la lista de mensajes de un contacto específico
	 * @param contacto contacto del que se quiere obtener los mensajes
	 * @return lista de mensajes
	 */
	public List<Mensaje> getMensajes(Contacto contacto) {
		if (contacto instanceof ContactoIndividual && !((ContactoIndividual) contacto).isUsuario(usuarioActual)) {
			return Stream.concat(
					((ContactoIndividual) contacto).getMensajesEnviados().stream(),
					((ContactoIndividual) contacto).getMensajesRecibidos(Optional.of(usuarioActual)).stream())
					.sorted()
					.filter(m -> !(m.getEmisor().getTelefono().equals(usuarioActual.getTelefono()) && m.isGroup()))
					.collect(Collectors.toList());
		} else {
			return contacto.getMensajesEnviados();
		}
	}

	/**
	 * Obtiene la lista de contactos del usuario actual
	 * @return lista de contactos
	 */
	public List<Contacto> getContactosUsuarioActual() {
		if (usuarioActual == null) return new LinkedList<>();

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
				repoUsuarios.buscarUsuario(cIndividual.getTelefono())
				.ifPresent(user -> {
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
	 * @param usuario usuario buscado
	 * @return contacto individual asociado
	 */
	public Optional<ContactoIndividual> getContactoDelUsuarioActual(Usuario usuario) {
		return getContactosUsuarioActual().stream()
				.filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c)
				.filter(c -> c.getUsuario() != null && c.getUsuario().getCodigo() == usuario.getCodigo())
				.findAny();
	}

	/**
	 * Crea un nuevo contacto individual
	 * @param nombre nombre del contacto
	 * @param numTelefono teléfono del contacto
	 * @return nuevo ContactoIndividual o null si ya existe
	 */
	public ContactoIndividual crearContacto(String nombre, String numTelefono) {
		if (numTelefono.equals(usuarioActual.getTelefono())) return null;

		boolean yaExistePorNombre = usuarioActual.tieneContactoIndividual(nombre);
		boolean yaExistePorTelefono = usuarioActual.getContactosIndividuales().stream()
				.anyMatch(c -> c.getTelefono() != null && c.getTelefono().trim().equals(numTelefono.trim()));

		if (yaExistePorNombre || yaExistePorTelefono) return null;

		Optional<Usuario> usuarioOpt = repoUsuarios.buscarUsuario(numTelefono);
		if (usuarioOpt.isPresent()) {
			Usuario usuarioContacto = usuarioOpt.get();
			ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioContacto, numTelefono);
			nuevoContacto.setFoto(usuarioContacto.getFotoPerfil());
			usuarioActual.añadirContacto(nuevoContacto);
			adaptadorContactoIndividual.registrarContacto(nuevoContacto);
			adaptadorUsuario.modificarUsuario(usuarioActual);
			return nuevoContacto;
		}
		return null;
	}
	
	/**
	 * Obtiene la lista de contactos ordenados por el último mensaje recibido
	 * 
	 * @return lista de contactos ordenados
	 */
	public List<Contacto> obtenerContactosOrdenadosPorUltimoMensaje() {
	    List<Contacto> contactos = Optional.ofNullable(getContactosUsuarioActual())
	            .orElse(new LinkedList<>());

	    contactos.sort((c1, c2) -> {
	        List<Mensaje> mensajes1 = getMensajes(c1);
	        List<Mensaje> mensajes2 = getMensajes(c2);

	        if (mensajes1.isEmpty() && mensajes2.isEmpty()) return 0;
	        if (mensajes1.isEmpty()) return 1;
	        if (mensajes2.isEmpty()) return -1;

	        Mensaje ultimo1 = mensajes1.get(mensajes1.size() - 1);
	        Mensaje ultimo2 = mensajes2.get(mensajes2.size() - 1);

	        return ultimo2.getHora().compareTo(ultimo1.getHora());
	    });

	    return contactos;
	}
	// ===================== Métodos de Grupos =====================

	/**
	 * Crea un nuevo grupo y lo añade a la lista de contactos del usuario actual
	 * @param nombre nombre del grupo
	 * @param integrantes lista de contactos individuales
	 * @return nuevo grupo creado, o null si hubo error
	 */
	public Grupo crearGrupo(String nombre, List<ContactoIndividual> integrantes) {
		if (integrantes == null || integrantes.isEmpty()) return null;
		if (usuarioActual.tieneGrupo(nombre)) return null;

		Grupo grupo = new Grupo(nombre);
		integrantes.forEach(grupo::agregarIntegrante);
		usuarioActual.añadirContacto(grupo);
		adaptadorGrupo.registrarGrupo(grupo);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		return grupo;
	}

	/**
	 * Añade un contacto a un grupo si no está ya presente
	 * @param nombreGrupo nombre del grupo
	 * @param contacto contacto a añadir
	 * @return true si fue añadido, false si ya existía o no se encontró el grupo
	 */
	public boolean añadirContactoAGrupo(String nombreGrupo, ContactoIndividual contacto) {
		if (usuarioActual == null || contacto == null || nombreGrupo == null || nombreGrupo.isBlank()) return false;

		Grupo grupo = getGruposPorNombre(nombreGrupo);
				

		if (grupo == null || grupo.getIntegrantes().contains(contacto)) return false;

		grupo.agregarIntegrante(contacto);
		adaptadorGrupo.modificarGrupo(grupo);
		return true;
	}


	/**
	 * Crea un grupo vacío con un nombre
	 * @param nombre nombre del grupo
	 * @return grupo creado o null si ya existía
	 */
	public Grupo añadirGrupo(String nombre) {
		if (!usuarioActual.tieneGrupo(nombre)) {
			Grupo nuevoGrupo = new Grupo(nombre);
			usuarioActual.añadirContacto(nuevoGrupo);
			adaptadorGrupo.registrarGrupo(nuevoGrupo);
			adaptadorUsuario.modificarUsuario(usuarioActual);
			return nuevoGrupo;
		}
		return null;
	}


	// ===================== Métodos de Mensajes =====================

	/**
	 * Envía un mensaje de texto a un contacto
	 * @param texto mensaje de texto
	 * @param contacto destinatario
	 */
	public void enviarMensaje(String texto, Contacto contacto) {
		if (usuarioActual == null || contacto == null || texto == null || texto.trim().isEmpty()) return;

		if (contacto instanceof ContactoIndividual) {
			enviarMensajeAContactoIndividual(texto, (ContactoIndividual) contacto);
		} else if (contacto instanceof Grupo) {
			enviarMensajeAGrupo(texto, (Grupo) contacto);
		}
	}

	/**
	 * Envía un mensaje de texto a un contacto individual
	 * 
	 * @param texto    mensaje de texto
	 * @param contacto destinatario
	 */
	private void enviarMensajeAContactoIndividual(String texto, ContactoIndividual contacto) {
		if (!isEnListaContactos(contacto)) {
			crearContactoAnonimo(contacto);
		}

		ContactoIndividual real = usuarioActual.getContactosIndividuales().stream()
				.filter(c -> c.getTelefono().equals(contacto.getTelefono()))
				.findFirst()
				.orElse(null);

		if (real != null) {
			Mensaje mensaje = new Mensaje(texto, LocalDateTime.now(), usuarioActual, real);
			real.enviarMensaje(mensaje);
			adaptadorMensaje.registrarMensaje(mensaje);
			adaptadorContactoIndividual.modificarContacto(real);
		} else {
			System.err.println("No se encontró contacto persistido con ese teléfono.");
		}
	}

	/**
	 * Envía un mensaje de texto a un grupo
	 * 
	 * @param texto mensaje de texto
	 * @param grupo destinatario
	 */
	private void enviarMensajeAGrupo(String texto, Grupo grupo) {
		for (ContactoIndividual integrante : grupo.getIntegrantes()) {
			if (!isEnListaContactos(integrante)) {
				crearContactoAnonimo(integrante);
			}

			ContactoIndividual realIntegrante = usuarioActual.getContactosIndividuales().stream()
					.filter(c -> c.getTelefono().equals(integrante.getTelefono()))
					.findFirst()
					.orElse(integrante);

			Mensaje mensajeGrupo = new Mensaje(texto, LocalDateTime.now(), usuarioActual, realIntegrante);
			mensajeGrupo.setGroup(true);
			realIntegrante.enviarMensaje(mensajeGrupo);
			adaptadorMensaje.registrarMensaje(mensajeGrupo);
			adaptadorContactoIndividual.modificarContacto(realIntegrante);
		}

		Mensaje mensaje = new Mensaje(texto, LocalDateTime.now(), usuarioActual, grupo);
		grupo.enviarMensaje(mensaje);
		adaptadorMensaje.registrarMensaje(mensaje);
		adaptadorGrupo.modificarGrupo(grupo);
	}

	/**
	 * Envía un emoticono como mensaje a un contacto
	 * @param emoticono código del emoticono
	 * @param contacto destinatario
	 */
	public void enviarMensaje(int emoticono, Contacto contacto) {
		if (usuarioActual == null || contacto == null) return;

		if (contacto instanceof ContactoIndividual) {
			enviarEmoticonoAContactoIndividual(emoticono, (ContactoIndividual) contacto);
		} else if (contacto instanceof Grupo) {
			enviarEmoticonoAGrupo(emoticono, (Grupo) contacto);
		}
	}

	/**
	 * Envía un emoticono a un contacto individual
	 * 
	 * @param emoticono código del emoticono
	 * @param contacto  destinatario
	 */
	private void enviarEmoticonoAContactoIndividual(int emoticono, ContactoIndividual contacto) {
		if (!isEnListaContactos(contacto)) {
			crearContactoAnonimo(contacto);
		}

		// Recuperar contacto actualizado desde lista de contactos del usuario actual
		ContactoIndividual real = getUsuarioActual().getContactosIndividuales().stream()
			.filter(c -> c.getTelefono().equals(contacto.getTelefono()))
			.findFirst()
			.orElse(null);

		if (real == null) {
			System.err.println("No se pudo encontrar el contacto persistido para enviar emoticono.");
			return;
		}

		Mensaje mensaje = new Mensaje(emoticono, LocalDateTime.now(), usuarioActual, real);
		real.enviarMensaje(mensaje);
		adaptadorMensaje.registrarMensaje(mensaje);
		adaptadorContactoIndividual.modificarContacto(real);
	}

	/**
	 * Envía un emoticono a un grupo
	 * 
	 * @param emoticono código del emoticono
	 * @param grupo     destinatario
	 */
	private void enviarEmoticonoAGrupo(int emoticono, Grupo grupo) {
		for (ContactoIndividual integrante : grupo.getIntegrantes()) {
			if (!isEnListaContactos(integrante)) {
				crearContactoAnonimo(integrante);
			}

			Mensaje mensajeGrupo = new Mensaje(emoticono, LocalDateTime.now(), usuarioActual, integrante);
			mensajeGrupo.setGroup(true);
			integrante.enviarMensaje(mensajeGrupo);
			adaptadorMensaje.registrarMensaje(mensajeGrupo);
			adaptadorContactoIndividual.modificarContacto(integrante);
		}

		Mensaje mensaje = new Mensaje(emoticono, LocalDateTime.now(), usuarioActual, grupo);
		grupo.enviarMensaje(mensaje);
		adaptadorMensaje.registrarMensaje(mensaje);
		adaptadorGrupo.modificarGrupo(grupo);
	}

	// ===================== Métodos de Descuentos =====================

	/**
	 * Calcula el mejor descuento aplicable al usuario actual
	 * @return porcentaje del mejor descuento encontrado
	 */
	public double calcularMejorDescuento() {
		if (usuarioActual == null) return 0.0;

		DescuentoFecha descuentoFecha = new DescuentoFecha(
				LocalDate.of(2024, 1, 1),
				LocalDate.of(2024, 12, 31)
				);
		DescuentoMensaje descuentoMensaje = new DescuentoMensaje(10);

		double d1 = descuentoFecha.obtenerPorcentajeDescuento(usuarioActual);
		double d2 = descuentoMensaje.obtenerPorcentajeDescuento(usuarioActual);

		return Math.max(d1, d2);
	}

	// ===================== Métodos Utilitarios =====================

	/**
	 * Verifica si el contacto ya está en la lista de contactos del usuario actual
	 * @param contacto contacto a verificar
	 * @return true si existe, false si no
	 */
	public boolean isEnListaContactos(Contacto contacto) {
		if (!(contacto instanceof ContactoIndividual)) return false;

		ContactoIndividual contactoIndividual = (ContactoIndividual) contacto;
		return usuarioActual.getContactos().stream()
				.filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c)
				.anyMatch(c -> c.getTelefono().equals(contactoIndividual.getTelefono()));
	}

	/**
	 * Crea un contacto anónimo para el usuario actual si no existe.
	 * @param contacto contacto individual que representa el emisor desconocido
	 */
	private void crearContactoAnonimo(ContactoIndividual contacto) {
		Optional<Usuario> usuarioOpt = repoUsuarios.buscarUsuario(contacto.getTelefono());
		if (usuarioOpt.isEmpty()) return;

		Usuario receptor = usuarioOpt.get();

		if (!receptor.tieneContactoIndividualPorTelefono(usuarioActual.getTelefono())) {
			ContactoIndividual anonimoParaReceptor = new ContactoIndividual(
					usuarioActual.getTelefono(), usuarioActual, usuarioActual.getTelefono());
			anonimoParaReceptor.setFoto(new ImageIcon(getClass().getResource("/account.png")));
			adaptadorContactoIndividual.registrarContacto(anonimoParaReceptor);
			receptor.añadirContacto(anonimoParaReceptor);
			adaptadorUsuario.modificarUsuario(receptor);
		}

		if (!usuarioActual.tieneContactoIndividualPorTelefono(receptor.getTelefono())) {
			ContactoIndividual anonimoParaActual = new ContactoIndividual(
					receptor.getTelefono(), receptor, receptor.getTelefono());
			anonimoParaActual.setFoto(receptor.getFotoPerfil());
			adaptadorContactoIndividual.registrarContacto(anonimoParaActual);
			usuarioActual.añadirContacto(anonimoParaActual);
			adaptadorUsuario.modificarUsuario(usuarioActual);
		}
	}

	/**
	 * Renombra un contacto o grupo
	 * @param contacto contacto o grupo a renombrar
	 * @param nuevoNombre nuevo nombre a asignar
	 */
	public void renombrarContacto(Contacto contacto, String nuevoNombre) {
	    if (contacto == null || nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

	    contacto.setNombre(nuevoNombre.trim());

	    if (contacto instanceof ContactoIndividual) {
	        ContactoIndividual ci = (ContactoIndividual) contacto;

	        // Validación clave: si el contacto no tiene código, no se puede modificar
	        if (ci.getCodigo() == 0) {
	            System.err.println("ERROR: El contacto '" + ci.getNombre() + "' no está registrado en la base de datos (código = 0).");
	            return;
	        }

	        adaptadorContactoIndividual.modificarContacto(ci);
	    } else if (contacto instanceof Grupo) {
	        adaptadorGrupo.modificarGrupo((Grupo) contacto);
	    }

	    adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	
	// ===================== Getters y Setters =====================
	/**
	 * Obtiene la instancia del repositorio de usuarios
	 * 
	 * @return repositorio de usuarios
	 */
	public RepositorioUsuarios getRepoUsuarios() {
		return this.repoUsuarios;
	}
	/**
	 * Obtiene el adaptador de contactos
	 * 
	 * @return adaptador de contactos
	 */
	public ContactoIndividualDAO getAdaptadorContactoIndividual() {
		return this.adaptadorContactoIndividual;
	}
	/**
	 * Obtiene el adaptador de grupo
	 * 
	 * @return adaptador de grupo
	 */
	public GrupoDAO getAdaptadorGrupo() {
		return this.adaptadorGrupo;
	}
	/**
	 * Obtiene el adaptador de usuario
	 * 
	 * @return	adaptador de usuario
	 */
	public UsuarioDAO getAdaptadorUsuario() {
		return this.adaptadorUsuario;
	}
	
	/**
	 * Obtiene el adaptador de mensajes
	 * 
	 * @return adaptador de mensajes
	 */
	public MensajeDAO getAdaptadorMensaje() {
		return this.adaptadorMensaje;
	}

	/**
	 * Obtiene el usuario actual autenticado
	 * 
	 * @return usuario actual
	 */
	public Usuario getUsuarioActual() {
		return this.usuarioActual;
	}

	/**
	 * Establece el usuario actual autenticado
	 * 
	 * @param usuario usuario a establecer
	 */
	public void setUsuarioActual(Usuario usuario) {
		this.usuarioActual = usuario;
	}
	
	/**
	 * Obtiene la lista de grupos del usuario actual
	 * 
	 * @return lista de grupos
	 */
	public List<Grupo> getGruposUsuarioActual() {
	    return getUsuarioActual().getGrupos(); 
	}
	
	/**
	 * Obtiene un usuario por su número de teléfono
	 * 
	 * @param telefono número de teléfono del usuario
	 * @return usuario encontrado o null si no existe
     */
	public Usuario getUsuarioPorTelefono(String telefono) {
	    return adaptadorUsuario.recuperarUsuarioPorTelefono(telefono);
	}
	
	
	
	
	/**
	 * Obtiene un grupo específico del usuario actual
	 * 
	 * @param nombreGrupo nombre del grupo a buscar
	 * @return grupo encontrado o null si no existe
	 */
	private Grupo getGruposPorNombre(String nombreGrupo) {
		return usuarioActual.getGrupos().stream()
				.filter(g -> g.getNombre().equals(nombreGrupo))
				.findFirst()
				.orElse(null);
	}

}
