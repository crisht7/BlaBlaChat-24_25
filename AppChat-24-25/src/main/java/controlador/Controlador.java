package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
			return resultado;
		}
		//Si están los dos campos llenos
		Usuario usuario = repoUsuarios.getUsuario(telefono); 
		
		//Comprobaciónes del usuario
		if (usuario ==null) {
			return resultado;
		}
		if (usuario.getContraseña().equals(Contraseña)) {
			this.usuarioActual = usuario;
			resultado = true;
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
}
	

