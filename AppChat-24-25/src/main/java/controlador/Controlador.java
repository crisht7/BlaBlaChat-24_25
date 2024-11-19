package controlador;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;

import modelo.Mensaje;
import modelo.Usuario;
import repositorio.RepositorioUsuarios;

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
		this.repoUsuarios = new RepositorioUsuarios();
		return true;
	}
	public static Object getUsuarioActual() {
		// TODO Auto-generated method stub
		return "Mapache";

	}

	public static List<Mensaje> getMensajesRecientesPorUsuario() {
		Usuario Mapache = new Usuario("Mapache", "1234", "12345678");
		Usuario Javier = new Usuario("Mapache2", "1234", "12345678");
		ArrayList<Mensaje> resultado = new ArrayList<Mensaje>();
		resultado.add(new Mensaje("Hola Mapache",Mapache, Javier));
		resultado.add(new Mensaje("Hola Javier",Javier, Mapache));
 		
		return resultado;
	}
}
