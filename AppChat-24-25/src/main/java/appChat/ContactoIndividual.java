package appChat;

import java.util.LinkedList;

/**
 * Representa un contacto individual en la aplicación de chat, 
 * los cuales están vinculados a un usuario.
 */
public class ContactoIndividual extends Contacto {
    
	private Usuario	usuario;
	private String telefono;
	
	/**
	 * Crea un contacto individual con nombre, usuario asociado y teléfono
	 * @param nombre del contacto
	 * @param usuario asociado
	 * @param telefono de contacto
	 */
	public ContactoIndividual(String nombre, Usuario usuario, String telefono) {
        super(nombre);
        this.usuario = usuario;
        this.telefono = telefono;
    }
	
	/**
	 * Crea un contacto individual con nombre, mensajes, teléfono y usuario asociado
	 * @param nombre del contacto
	 * @param mensajes intercambiados
	 * @param telefono de contacto
	 * @param usuario asociado
	 */
	public ContactoIndividual(String nombre, LinkedList<Mensaje> mensajes, String telefono, Usuario usuario) {
        super(nombre, mensajes);
        this.usuario = usuario;
        this.telefono = telefono;
    }
	
	//TODO: Getters y setters
	//TODO: Obtener mensajes por telefoon
	//TODO: Obtener mensajes por texto
}

