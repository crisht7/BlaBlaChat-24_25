package appChat;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

	//Getters y setters
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	
    /**
     * Filtra y obtiene los mensajes enviados o recibidos por un número de teléfono específico.
     * si es emisor comprueba el telefono y si es receptor se comprueba que sea contacto individual (tiene teléfono) y luego el telefono 
     * @param telefono Número de teléfono a buscar.
     * @return Lista de mensajes que involucran el teléfono dado.
     */
	public List<Mensaje> obtenerMensajesPorTelefono(String telefono) {
	    return getMensajes().stream()
	            .filter(mensaje -> 
	                mensaje.getEmisor().getTelefono().equals(telefono) || 
	                (mensaje.getReceptor() instanceof ContactoIndividual &&
	                 ((ContactoIndividual) mensaje.getReceptor()).getTelefono().equals(telefono))
	            )
	            .collect(Collectors.toList());
	}


    /**
     * Filtra y obtiene los mensajes que contienen un fragmento de texto específico.
     * @param texto Fragmento de texto a buscar en los mensajes.
     * @return Lista de mensajes que contienen el texto dado.
     */
    public List<Mensaje> obtenerMensajesPorTexto(String texto) {
        return getMensajes().stream()
                .filter(mensaje -> mensaje.getTexto().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
    }
	
    
}

