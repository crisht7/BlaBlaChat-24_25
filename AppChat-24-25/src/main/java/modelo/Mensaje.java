package modelo;

import java.sql.Time;
import java.util.Date;

public class Mensaje {
    private String texto;
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

    // Getters y setters
}
