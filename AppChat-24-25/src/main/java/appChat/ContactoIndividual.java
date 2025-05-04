package appChat;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Representa un contacto individual en la aplicación de chat,
 * los cuales están vinculados a un usuario.
 */
public class ContactoIndividual extends Contacto {

	/**
	 * Usuario asociado al contacto individual.
	 */
    private Usuario usuario;
	/**
	 * Número de teléfono del contacto individual.
	 */
    private String telefono;

    // ===================== Constructores =====================

    /**
     * Crea un contacto individual con nombre, usuario asociado y teléfono.
     * 
     * @param nombre nombre del contacto
     * @param usuario usuario asociado
     * @param telefono número de teléfono
     */
    public ContactoIndividual(String nombre, Usuario usuario, String telefono) {
        super(nombre);
        this.usuario = usuario;
        this.telefono = telefono;
    }

    /**
     * Crea un contacto individual con nombre, mensajes, teléfono y usuario asociado.
     * 
     * @param nombre nombre del contacto
     * @param mensajes mensajes intercambiados
     * @param telefono número de teléfono
     * @param usuario usuario asociado
     */
    public ContactoIndividual(String nombre, LinkedList<Mensaje> mensajes, String telefono, Usuario usuario) {
        super(nombre, mensajes);
        this.usuario = usuario;
        this.telefono = telefono;
    }

    // ===================== Métodos sobre Mensajes =====================

    /**
     * Filtra y obtiene los mensajes enviados o recibidos por un número de teléfono específico.
     * 
     * @param telefono número de teléfono a buscar
     * @return lista de mensajes que involucran el teléfono dado
     */
    public List<Mensaje> obtenerMensajesPorTelefono(String telefono) {
        List<Mensaje> lista = new LinkedList<>();
        for (Mensaje m : getMensajesEnviados()) {
            if (m.getEmisor().getTelefono().equals(telefono)) {
                lista.add(m);
            }
        }
        return lista;
    }

    /**
     * Filtra y obtiene los mensajes que contienen un fragmento de texto específico.
     * 
     * @param texto fragmento de texto a buscar en los mensajes
     * @return lista de mensajes que contienen el texto dado
     */
    public List<Mensaje> obtenerMensajesPorTexto(String texto) {
        List<Mensaje> lista = new LinkedList<>();
        for (Mensaje m : getMensajesEnviados()) {
            if (m.getTexto() != null && m.getTexto().contains(texto)) {
                lista.add(m);
            }
        }
        return lista;
    }

    /**
     * Filtra y obtiene los mensajes recibidos por un usuario específico.
     * 
     * @param usuario usuario cuyos mensajes recibidos serán recuperados
     * @return lista de mensajes recibidos
     */
    public List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario) {
        ContactoIndividual contacto = getContacto(usuario.orElse(null));
        if (contacto != null) {
            return contacto.getMensajesEnviados();
        }
        return new LinkedList<>();
    }

    // ===================== Métodos de Usuario y Teléfono =====================

    /**
     * Obtiene el usuario asociado a este contacto individual.
     * 
     * @return usuario asociado
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario asociado a este contacto individual.
     * 
     * @param usuario nuevo usuario asociado
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el número de teléfono del contacto individual.
     * 
     * @return número de teléfono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Comprueba si este contacto pertenece al usuario dado.
     * 
     * @param otroUsuario usuario a comparar
     * @return true si es el mismo usuario, false si no
     */
    public boolean isUsuario(Usuario otroUsuario) {
        return this.usuario.equals(otroUsuario);
    }

    /**
     * Obtiene el contacto individual asociado a un usuario específico.
     * 
     * @param usuario usuario a buscar
     * @return contacto individual asociado, o null si no existe
     */
    public ContactoIndividual getContacto(Usuario usuario) {
        if (usuario == null) return null;
        return this.usuario.getContactos().stream()
                .filter(c -> c instanceof ContactoIndividual)
                .map(c -> (ContactoIndividual) c)
                .filter(c -> c.getUsuario().equals(usuario))
                .findAny()
                .orElse(null);
    }

    /**
     * Devuelve una representación en cadena del contacto individual.
     * 
     * @return nombre del contacto
     */
    @Override
    public String toString() {
        return getNombre();
    }
}
