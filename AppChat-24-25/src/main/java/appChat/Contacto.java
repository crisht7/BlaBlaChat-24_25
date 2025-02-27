package appChat;

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

    
  //Getters y setters
    public String getNombre() {
		return nombre;
	}
    
    public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public List<Mensaje> getMensajesEnviados() {
		
		return this.mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public ImageIcon getFoto() {
		return foto;
	}

	public void setFoto(ImageIcon foto) {
		this.foto = foto;
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
	
	
	
    
    
    
}
    
