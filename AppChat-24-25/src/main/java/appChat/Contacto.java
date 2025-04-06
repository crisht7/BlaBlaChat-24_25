package appChat;

import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

public class Contacto {
    private String nombre;
    private int codigo;
    private List<Mensaje> mensajes;
    private ImageIcon foto;
    
    /**
     * Constructor que inicializa un contacto sin foto de perfil
     * 
     * @param nombre de contacto
     */
    public Contacto(String nombre) {
        this.nombre = nombre;
        this.foto = null;
        this.mensajes = new LinkedList<>();
    }
    
	/**
	 * Constructor que inicializa un contacto con foto de perfil
	 * 
	 * @param nombre de contacto
	 * @param foto de perfil
	 */
    public Contacto(String nombre, ImageIcon foto) {
    	this(nombre);
    	this.foto = foto;
    	this.mensajes = new LinkedList<>();
    }
    
    /**
     * Constructor que inicializa un contacto con nombre y mensajes
     * 
     * @param nombre de contacto
     * @param mensajes de contacto
     */
    public Contacto(String nombre, List<Mensaje> mensajes) {
    	this(nombre);
    	this.mensajes = mensajes;
    	this.foto = null;
    }
	
	  /**
     * Añade un mensaje a la lista de mensajes del contacto.
     * 
     * @param mensaje Mensaje a añadir.
     */
    public void enviarMensaje(Mensaje mensaje) {
        if (mensaje != null) {
            mensajes.add(mensaje);
        }
    }
        
    /**
     * Elimina un mensaje específico de la lista.
     * 
     * @param mensaje Mensaje a eliminar.
     * @return true si se eliminó con éxito, false en caso contrario.
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
     * @param usuario Usuario cuyos mensajes enviados serán eliminados.
     */
    public void eliminarMensajesEnviadosPor(Usuario usuario) {
        if (usuario != null) {
            mensajes.removeIf(mensaje -> mensaje.getEmisor().equals(usuario));
        }
    }

    /**
     * Añade una lista de mensajes al contacto.
     * 
     * @param nuevosMensajes Lista de mensajes a añadir.
     */
    public void añadirListaMensajes(List<Mensaje> nuevosMensajes) {
        if (nuevosMensajes != null) {
            mensajes.addAll(nuevosMensajes);
        }
    }
	
	/**
	 * Devuelve el nombre del contacto.
	 * 
	 * @return Nombre del contacto.
	 */
    public String getNombre() {
		return nombre;
	}
    
    /**
     * Establece el nombre del contacto.
     * 
     * @param nombre
     */
    public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
	/**
	 * Devuelve el código del contacto.
	 * 
	 * @return Código del contacto.
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Establece el código del contacto.
	 * 
	 * @param codigo Código del contacto.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * Devuelve la lista de mensajes enviados por el contacto.
	 * 
	 * @return Lista de mensajes enviados.
	 */
	public List<Mensaje> getMensajesEnviados() {
		if (this.mensajes == null) {
			return new LinkedList<Mensaje>();
		}
		return this.mensajes;
	}
	
	/**
	 * Devuelve la foto de perfil del contacto.
	 * @return Foto de perfil.
	 */
	public ImageIcon getFoto() {
		return foto;
	}

	/**
	 * Establece la foto de perfil del contacto.
	 * 
	 * @param foto Foto de perfil.
	 */
	public void setFoto(ImageIcon foto) {
		this.foto = foto;
	}

}  
