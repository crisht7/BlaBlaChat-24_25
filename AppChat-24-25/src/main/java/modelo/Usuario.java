package modelo;

import java.util.ArrayList;
import java.util.List;

import com.toedter.calendar.JDateChooser;

public class Usuario {
    private String usuario;
    private String contraseña;
    private String telefono;
    private JDateChooser fechaNacimiento;
    private String imagen;
    private String saludo;

    // Constructor
    public Usuario(String usuario, String contraseña, String telefono, JDateChooser fechaNacimiento, String imagen, String saludo) {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.imagen = imagen;
        this.saludo = saludo;
    }

    //Constructor auxiliar mientras no se implementa la base de datos para recoger datos 
    public Usuario(String string, String string2, String string3) {
    	this.usuario = string;
    	this.contraseña = string2;
    	this.telefono = string3;
    }

	// Métodos para obtener el contacto individual y el chat de mensajes con otro usuario
    public Contacto getContactoIndividual(Usuario otroUsuario) {
        // Implementar la lógica para obtener un contacto individual
        return null;
    }

    public List<Mensaje> getChatMensajes(Usuario otroUsuario) {
        // Implementar la lógica para obtener mensajes
        return new ArrayList<>();
    }

	public String getNombre() {
		
		return this.usuario;
	}

    // Getters y setters
}
