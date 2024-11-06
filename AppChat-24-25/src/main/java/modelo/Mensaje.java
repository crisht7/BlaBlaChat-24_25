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

    public Mensaje(String string, Usuario javier, Usuario mapache) {
		// TODO Auto-generated constructor stub
	}

	public String getTexto() {
		// TODO Auto-generated method stub
		return null;
	}

	public Usuario getEmisor() {
		// TODO Auto-generated method stub
		return null;
	}

	public Usuario getReceptor() {
		// TODO Auto-generated method stub
		return null;
	}

    // Getters y setters
}
