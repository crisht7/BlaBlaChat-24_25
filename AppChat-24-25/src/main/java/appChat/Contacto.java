package appChat;

import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * Clase Contacto que representa un contacto en la aplicación.
 */
public class Contacto {

    /**
     * Nombre del contacto.
     */
    private String nombre;
	/**
	 * Código único del contacto.
	 */
    private int codigo;
	/**
	 * Lista de mensajes enviados por el contacto.
	 */
    private List<Mensaje> mensajes;
	/**
	 * Foto de perfil del contacto.
	 */
    private ImageIcon foto;

    // ===================== Constructores =====================

    /**
     * Constructor que inicializa un contacto sin foto de perfil
     * 
     * @param nombre nombre del contacto
     */
    public Contacto(String nombre) {
        this.nombre = nombre;
        this.foto = null;
        this.mensajes = new LinkedList<>();
    }

    /**
     * Constructor que inicializa un contacto con foto de perfil
     * 
     * @param nombre nombre del contacto
     * @param foto foto de perfil
     */
    public Contacto(String nombre, ImageIcon foto) {
        this(nombre);
        this.foto = foto;
    }

    /**
     * Constructor que inicializa un contacto con nombre y mensajes
     * 
     * @param nombre nombre del contacto
     * @param mensajes lista de mensajes del contacto
     */
    public Contacto(String nombre, List<Mensaje> mensajes) {
        this(nombre);
        this.mensajes = mensajes;
        this.foto = null;
    }

    // ===================== Métodos sobre Mensajes =====================

    /**
     * Añade un mensaje a la lista de mensajes del contacto.
     * 
     * @param mensaje mensaje a añadir
     */
    public void enviarMensaje(Mensaje mensaje) {
        if (mensaje != null) {
            mensajes.add(mensaje);
        }
    }

    /**
     * Elimina un mensaje específico de la lista.
     * 
     * @param mensaje mensaje a eliminar
     * @return true si se eliminó con éxito, false en caso contrario
     */
    public boolean eliminarMensaje(Mensaje mensaje) {
        return mensajes.remove(mensaje);
    }

    /**
     * Borra todos los mensajes del contacto.
     */
    public void borrarTodosLosMensajes() {
        mensajes.clear();
    }

    /**
     * Elimina todos los mensajes enviados por un usuario específico.
     * 
     * @param usuario usuario cuyos mensajes enviados serán eliminados
     */
    public void eliminarMensajesEnviadosPor(Usuario usuario) {
        if (usuario != null) {
            mensajes.removeIf(mensaje -> mensaje.getEmisor().equals(usuario));
        }
    }

    /**
     * Añade una lista de mensajes al contacto.
     * 
     * @param nuevosMensajes lista de mensajes a añadir
     */
    public void añadirListaMensajes(List<Mensaje> nuevosMensajes) {
        if (nuevosMensajes != null) {
            mensajes.addAll(nuevosMensajes);
        }
    }

    // ===================== Getters y Setters =====================

    /**
     * Devuelve el nombre del contacto.
     * 
     * @return nombre del contacto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del contacto.
     * 
     * @param nombre nuevo nombre del contacto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el código del contacto.
     * 
     * @return código del contacto
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el código del contacto.
     * 
     * @param codigo nuevo código del contacto
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Devuelve la lista de mensajes enviados por el contacto.
     * 
     * @return lista de mensajes enviados
     */
    public List<Mensaje> getMensajesEnviados() {
        if (this.mensajes == null) {
            return new LinkedList<>();
        }
        return this.mensajes;
    }

    /**
     * Devuelve la foto de perfil del contacto.
     * 
     * @return foto de perfil
     */
    public ImageIcon getFoto() {
        return foto;
    }

    /**
     * Establece la foto de perfil del contacto.
     * 
     * @param foto nueva foto de perfil
     */
    public void setFoto(ImageIcon foto) {
        this.foto = foto;
    }

}
