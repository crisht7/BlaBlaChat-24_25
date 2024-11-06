package controlador;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;

import modelo.Mensaje;
import modelo.Usuario;
import repositorio.RepositorioUsuarios;

public class ControladorAppChat {
    private RepositorioUsuarios repoUsuarios;

    public ControladorAppChat(RepositorioUsuarios repoUsuarios) {
        this.repoUsuarios = repoUsuarios;
    }

    // Métodos para coordinar las acciones de la app
    
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
		Usuario Pedro = new Usuario("Mapache2", "1234", "12345678");
		ArrayList<Mensaje> resultado = new ArrayList<Mensaje>();
		resultado.add(new Mensaje("Hola Mapache",Mapache, Javier));
		resultado.add(new Mensaje("Hola Javier",Javier, Mapache));
 		
		return resultado;
	}
}
