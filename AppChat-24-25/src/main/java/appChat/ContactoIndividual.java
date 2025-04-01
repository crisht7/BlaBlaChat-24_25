package appChat;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
	
	public boolean isUsuario(Usuario otroUsuario) {
		return this.usuario.equals(otroUsuario);
	}
	
	public ContactoIndividual getContacto(Usuario usuario) {
		return this.usuario.getContactos().stream().filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c).
				filter(c -> c.getUsuario().equals(usuario)).findAny().orElse(null);
	}
    /**
     * Filtra y obtiene los mensajes enviados o recibidos por un número de teléfono específico.
     * si es emisor comprueba el telefono y si es receptor se comprueba que sea contacto individual (tiene teléfono) y luego el telefono 
     * @param telefono Número de teléfono a buscar.
     * @return Lista de mensajes que involucran el teléfono dado.
     */
	public List<Mensaje> obtenerMensajesPorTelefono(String telefono) {
		 List<Mensaje> lista = new LinkedList<>();
	        lista.stream().filter(m-> m.getEmisor().getTelefono().equals(telefono ));
	        return lista;
	}


    /**
     * Filtra y obtiene los mensajes que contienen un fragmento de texto específico.
     * @param texto Fragmento de texto a buscar en los mensajes.
     * @return Lista de mensajes que contienen el texto dado.
     */
    public List<Mensaje> obtenerMensajesPorTexto(String texto) {
        List<Mensaje> lista = new LinkedList<>();
        lista.stream().filter(m-> m.getTexto().contains(texto));
        return lista;
    }
	
	public List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario) {
		
        ContactoIndividual contacto = getContacto(usuario.orElse(null));
        if (contacto != null) 
        	return contacto.getMensajesEnviados();
        else
        	return new LinkedList<>();
    }
    
	
}

