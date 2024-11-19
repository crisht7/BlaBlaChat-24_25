package modelo;

import java.sql.Time;
import java.util.Date;

public class Mensaje {
    private String texto;
    private Usuario emisor;
    private Usuario receptor;
    private String emoticono;
    private Date fecha;
    private Time hora;

    // Constructor
    public Mensaje(String texto, String emoticono, Date fecha, Time hora) {
        this.texto = texto;
        this.emoticono = emoticono;
        this.fecha = fecha;
        this.hora = hora;
    }

    //Constructor auxiliar mientras no se implementa la base de datos para asignar mensajes al usuario
    public Mensaje(String string, Usuario us, Usuario us1) {
		this.texto = string;
		this.emisor = us;
		this.receptor = us1;
	}

	public String getTexto() {
		// TODO Auto-generated method stub
		return null;
	}

	public Usuario getEmisor() {
		
		return this.emisor;
	}

	public Usuario getReceptor() {
		
		return this.receptor;
	}

    // Getters y setters
}
