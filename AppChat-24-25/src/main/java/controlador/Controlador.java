package controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;

import appChat.Contacto;
import appChat.Mensaje;
import appChat.RepositorioUsuarios;
import appChat.Usuario;

public class Controlador {
	private static Controlador unicaInstancia = null;

	private RepositorioUsuarios repoUsuarios;

	private Controlador() {

	}
	
    public Controlador(RepositorioUsuarios repoUsuarios) {
        this.repoUsuarios = repoUsuarios;
    }
    //Uso del patron Singleton
    //(Una clase tiene una única instancia con acceso global)
    public static Controlador getInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new Controlador();
		return unicaInstancia;
	}
    

	public boolean hacerLogin(String Usuario, String Contraseña) {
		this.repoUsuarios = repoUsuarios.getUnicaInstancia();
		return true;
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
