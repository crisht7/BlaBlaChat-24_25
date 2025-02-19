package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JList;

import appChat.Contacto;
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
	
	public boolean registrarUsuario(String nombre, LocalDate fecha, ImageIcon foto, String numero, 
									String saludo, String contraseña) {
		Usuario usuarioExistente = repoUsuarios.getUsuario(numero);
		if (usuarioExistente != null) {
			return false;
		}
		
		LocalDate fechaRegistro = LocalDate.now();
		
		Usuario nuevoUsuario = new Usuario(nombre, fecha, foto, contraseña, numero, saludo, fechaRegistro, false);
		
		//Añadimos al repositorio si no existe
		if (!repoUsuarios.existeUsuario(nuevoUsuario)) {
			repoUsuarios.agregarUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);
			
			//Devuelve true si se ha registrado correctamente
			return hacerLogin(nuevoUsuario.getTelefono(), nuevoUsuario.getContraseña());
		}
		return false;
	}
	
	public static Object getUsuarioActual() {
		// TODO Auto-generated method stub
		return "Mapache";

	}

	public static List<Mensaje> getMensajesRecientesPorUsuario() {
		Usuario Mapache = new Usuario("Mapache", null, null, "1234", "12345678", null, null);
		Contacto Javier = new Contacto("Javier");
		ArrayList<Mensaje> resultado = new ArrayList<Mensaje>();
		resultado.add(new Mensaje("Hola Mapache",0, LocalDateTime.now(), Mapache, Javier));
		resultado.add(new Mensaje("Hola Javier",0 , LocalDateTime.now(), Mapache, Javier));
 		
		return resultado;
	}
}
