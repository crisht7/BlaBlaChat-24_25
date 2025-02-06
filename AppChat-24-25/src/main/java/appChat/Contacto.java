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

    
  //TODO: Getters y setters
    public String getNombre() {
		// TODO Auto-generated method stub
		return null;
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

	public List<Mensaje> getMensajes() {
		return mensajes;
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

	

	//TODO: Envia un mensaje y lo añade a la lista
    public void enviarMensaje(Mensaje mensaje) {
        if (mensaje != null) {
            mensajes.add(mensaje);
        }
    }

  //TODO: eliminar mensajes enviados por un usuario  (??)
    public void eliminarMensajesEnviadosPor(Usuario usuario) {
        if (usuario != null) {
            mensajes.removeIf(mensaje -> mensaje.getEmisor().equals(usuario));
        }
    }
    
    
  //TODO: Elimina un mensaje de la lista
    public boolean eliminarMensaje(Mensaje mensaje) {
        return mensajes.remove(mensaje);
    }

  //TODO: Borra todos los mensajes
    public void borrarTodosLosMensajes() {
        mensajes.clear();
    }

    
  //TODO: Añade lista de mensajes al contacto
    public void añadirListaMensajes(List<Mensaje> nuevosMensajes) {
        if (nuevosMensajes != null) {
            mensajes.addAll(nuevosMensajes);
        }
    }
	
	
	
    
    
    
}
    
