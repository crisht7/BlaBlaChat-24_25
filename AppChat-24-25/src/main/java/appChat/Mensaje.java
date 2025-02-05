package appChat;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class Mensaje {
	private int codigo;
    private String texto;
    private Usuario emisor;
    private Contacto receptor;
    private int emoticono;
    private LocalDateTime hora;
    private boolean grupo = false;

    /**
     * Constructor que inicializa un mensaje con texto
     * 
     * @param texto que contiene el mensaje
     * @param hora en la que se envió
     * @param emisor del mensaje
     * @param receptor contacto del mensaje
     */
    public Mensaje(String texto, LocalDateTime hora, Usuario emisor, Contacto receptor) {
        this.codigo = 0;
        this.texto = texto;
        this.hora = hora;
        this.emisor = emisor;
        this.receptor = receptor;
    }

	/**
	 * Constructor que inicializa un mensaje con emoticono
	 * 
	 * @param emoticono identificador 
	 * @param hora en la que se envió
	 * @param emisor del mensaje
	 * @param receptor contacto del mensaje
	 */
	public Mensaje(int emoticono, LocalDateTime hora, Usuario emisor, Contacto receptor) {
		this.codigo = 0;
		this.texto = "";
		this.emoticono = emoticono;
		this.hora = hora;
		this.emisor = emisor;
		this.receptor = receptor;
	}
	
	/**
	 * Constructor que inicializa un mensaje con texto y emoticono
	 * 
	 * @param texto que contiene el mensaje
	 * @param emoticono identificador
	 * @param hora en la que se envió
	 */
	public Mensaje(String texto, int emoticono, LocalDateTime hora, Usuario emisor, Contacto receptor) {
		this.texto = texto;
		this.emoticono = emoticono;
		this.hora = hora;
	}

	//TODO: Getters y setters bien hechos
	//TODO: Comparar mensajes por fecha y hora
	//TODO: Verificar si el mensaje es de grupo
	//TODO: Establecer si el mensaje es de grupo
	
	public String getTexto() {
		// TODO Auto-generated method stub
		return null;
	}

	public Usuario getEmisor() {
		
		return this.emisor;
	}

	public Contacto getReceptor() {
		
		return this.receptor;
	}

    // Getters y setters
}
