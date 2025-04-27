package appChat;

import java.time.LocalDateTime;

/**
 * Representa un mensaje en la aplicación de chat.
 */
public class Mensaje implements Comparable<Mensaje> {

    private int codigo;
    private String texto;
    private Usuario emisor;
    private Contacto receptor;
    private int emoticono;
    private LocalDateTime hora;
    private boolean grupo = false;

    // ===================== Constructores =====================

    /**
     * Constructor que inicializa un mensaje con texto.
     * 
     * @param texto texto del mensaje
     * @param hora fecha y hora en que se envió
     * @param emisor emisor del mensaje
     * @param receptor receptor del mensaje
     */
    public Mensaje(String texto, LocalDateTime hora, Usuario emisor, Contacto receptor) {
        this.codigo = 0;
        this.texto = texto;
        this.hora = hora;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    /**
     * Constructor que inicializa un mensaje con emoticono.
     * 
     * @param emoticono identificador del emoticono
     * @param hora fecha y hora en que se envió
     * @param emisor emisor del mensaje
     * @param receptor receptor del mensaje
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
     * Constructor que inicializa un mensaje con texto y emoticono.
     * 
     * @param texto texto del mensaje
     * @param emoticono identificador del emoticono
     * @param hora fecha y hora en que se envió
     */
    public Mensaje(String texto, int emoticono, LocalDateTime hora) {
        this.texto = texto;
        this.emoticono = emoticono;
        this.hora = hora;
    }

    // ===================== Getters =====================

    /**
     * Devuelve el texto del mensaje.
     * 
     * @return texto del mensaje
     */
    public String getTexto() {
        return this.texto;
    }

    /**
     * Devuelve el emisor del mensaje.
     * 
     * @return emisor del mensaje
     */
    public Usuario getEmisor() {
        return this.emisor;
    }

    /**
     * Devuelve el receptor del mensaje.
     * 
     * @return receptor del mensaje
     */
    public Contacto getReceptor() {
        return this.receptor;
    }

    /**
     * Devuelve la fecha y hora en la que se envió el mensaje.
     * 
     * @return fecha y hora del mensaje
     */
    public LocalDateTime getHora() {
        return this.hora;
    }

    /**
     * Devuelve el identificador del emoticono del mensaje.
     * 
     * @return identificador del emoticono
     */
    public int getEmoticono() {
        return this.emoticono;
    }

    /**
     * Devuelve el usuario asociado al receptor del mensaje.
     * 
     * @return usuario receptor
     */
    public Usuario getUsuarioReceptor() {
        return ((ContactoIndividual) this.receptor).getUsuario();
    }

    /**
     * Devuelve el código único del mensaje.
     * 
     * @return código del mensaje
     */
    public int getCodigo() {
        return this.codigo;
    }

    /**
     * Devuelve si el mensaje pertenece a un grupo.
     * 
     * @return true si es un mensaje de grupo, false si es individual
     */
    public boolean isGroup() {
        return this.grupo;
    }

    // ===================== Setters =====================

    /**
     * Establece el código único del mensaje.
     * 
     * @param codigo nuevo código
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Establece el contacto que recibirá el mensaje.
     * 
     * @param receptor nuevo receptor
     */
    public void setReceptor(Contacto receptor) {
        this.receptor = receptor;
    }

    /**
     * Establece el texto del mensaje.
     * 
     * @param texto nuevo texto
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Establece el emisor del mensaje.
     * 
     * @param emisor nuevo emisor
     */
    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    /**
     * Establece si el mensaje pertenece a un grupo.
     * 
     * @param grupo true si es grupo, false si es individual
     */
    public void setGroup(boolean grupo) {
        this.grupo = grupo;
    }

    // ===================== Métodos de utilidad =====================

    /**
     * Representa el mensaje como una cadena de texto.
     * 
     * @return representación textual del mensaje
     */
    @Override
    public String toString() {
        return "Mensaje [codigo=" + codigo + ", texto=" + texto + ", emisor=" + emisor
                + ", receptor=" + receptor + ", hora=" + hora + ", emoticono=" + emoticono + "]";
    }

    /**
     * Compara dos mensajes por la fecha y hora en que fueron enviados.
     * 
     * @param o mensaje a comparar
     * @return un valor negativo, cero o positivo dependiendo del orden temporal
     */
    @Override
    public int compareTo(Mensaje o) {
        return this.hora.compareTo(o.hora);
    }
}
