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
	
	/**
	 * Devuelve el texto del mensaje
	 * @return
	 */
	public String getTexto() {
		return this.texto;
	}
	
	/**
	 * Devuelve el emisor del mensaje
	 * @return
	 */
	public Usuario getEmisor() {
		return this.emisor;
	}

	/** 
	 * Devuelve el receptor del mensaje
	 * @return
	 */
	public Contacto getReceptor() {
		return this.receptor;
	}
	
	/**
	 * Devuelve la fecha y hora en la que se envió el mensaje
	 * @return
	 */
	public LocalDateTime getHora() {
		return this.hora;
	}
	
	/**
	 * Devuelve el identificador del emoticono del mensaje
	 * @return
	 */
	public int getEmoticono() {
		return this.emoticono;
	}
	
	/**
	 * Devuelve el usuario asociaod al recceptor del mensaje
	 * @return
	 */
	public Usuario getUsuarioReceptor() {
        return ((ContactoIndividual) this.receptor).getUsuario();
    }
	
	/**
	 * Devuelve el codigo unico del mensaje
	 * @return
	 */
	public int getCodigo() {
		return this.codigo;
	}
	
	/**
	 * Establece el codigo unico del mensaje
	 * @param codigo
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	/**
	 * Establece el contacto que reicibira el mensaje
	 * @param receptor
	 */
	public void setReceptor(Contacto receptor) {
		this.receptor = receptor;
	}
	
	/**
	 * Establece el texto del mensaje
	 * @param texto
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	/**
	 * Establece el emisor del mensaje
	 * @param emisor
	 */
	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}
	
	/**
	 * Establece si el mensaje a un grupo
	 * 
	 * @param grupo
	 */
	public void setGroup(boolean grupo) {
		this.grupo = grupo;
	}

	/**
	 * Devuelve si el mensaje pertenece a un grupo
	 * 
	 * @return
	 */
	public boolean isGroup() {
		return this.grupo;
	}
	
	/**
	 * Representa el mensaje como una cadena de texto.
	 *
	 * @return una representación textual del mensaje.
	 */
	@Override
	public String toString() {
		return "Message [codigo=" + codigo + ", texto=" + texto + ", emisor=" + emisor + ", receptor=" + receptor
				+ ", hora=" + hora + ", emoticono=" + emoticono + "]";
	}
	
	/**
	 * Compara dos mensajes por la fecha y hora en que fueron enviados.
	 *
	 * @param o el mensaje a comparar.
	 * @return un valor negativo, cero o positivo dependiendo del orden temporal.
	 */
	public int compareTo(Mensaje o) {
		return this.hora.compareTo(o.hora);
	}
}
