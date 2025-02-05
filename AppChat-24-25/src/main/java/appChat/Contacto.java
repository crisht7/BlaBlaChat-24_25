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
    //TODO: eliminar mensajes enviados 
    //TODO: Añade lista de mensajes al contacto
    //TODO: Envia un mensaje y lo añade a la lista
    //TODO: Elimina un mensaje de la lista
    //TODO: Borra todos los mensajes
    
    public String getNombre() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
    
