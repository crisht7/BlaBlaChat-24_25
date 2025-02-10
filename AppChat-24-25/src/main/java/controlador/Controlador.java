package controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	private ContactoIndividualDAO adaptadorContacto;
	
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
		// TODO Auto-generated method stub
	}

	private void inicializarRepositorios() {
		// TODO Auto-generated method stub
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
