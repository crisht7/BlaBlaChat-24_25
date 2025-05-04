package appChat;

import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * Clase que representa un grupo en la aplicación de chat,
 * compuesto por múltiples contactos individuales.
 */
public class Grupo extends Contacto {

	/**
	 * Lista de contactos individuales que forman parte del grupo.
	 */
    private List<ContactoIndividual> integrantes;

    // ===================== Constructores =====================

    /**
     * Crea un grupo con nombre e imagen predeterminada.
     * 
     * @param nombre nombre del grupo
     */
    public Grupo(String nombre) {
        super(nombre, new ImageIcon("src/main/resources/grupo.png"));
        this.integrantes = new LinkedList<>();
    }

    /**
     * Crea un grupo con nombre e imagen personalizada.
     * 
     * @param nombre nombre del grupo
     * @param imagen imagen personalizada
     */
    public Grupo(String nombre, ImageIcon imagen) {
        super(nombre, imagen);
        this.integrantes = new LinkedList<>();
    }

    // ===================== Métodos sobre Integrantes =====================

    /**
     * Añade un contacto individual al grupo.
     * 
     * @param contacto contacto a añadir
     */
    public void agregarIntegrante(ContactoIndividual contacto) {
        if (contacto != null) {
            this.integrantes.add(contacto);
        }
    }

    /**
     * Elimina un contacto individual del grupo.
     * 
     * @param contacto contacto a eliminar
     */
    public void eliminarIntegrante(ContactoIndividual contacto) {
        this.integrantes.remove(contacto);
    }

    /**
     * Obtiene una representación en texto de los participantes del grupo.
     * 
     * @return texto con los nombres de los participantes
     */
    public String getTextoParticipantes() {
        if (this.integrantes == null || this.integrantes.isEmpty()) {
            return "No hay integrantes";
        }

        StringBuilder textoIntegrantes = new StringBuilder();
        for (ContactoIndividual contacto : this.integrantes) {
            textoIntegrantes.append(contacto.getNombre()).append(", ");
        }

        // Eliminar el último ", "
        if (textoIntegrantes.length() >= 2) {
            textoIntegrantes.delete(textoIntegrantes.length() - 2, textoIntegrantes.length());
        }

        return textoIntegrantes.toString();
    }

    /**
     * Obtiene una lista de los participantes del grupo.
     * 
     * @return lista de contactos individuales
     */
    public List<ContactoIndividual> getIntegrantes() {
        return new LinkedList<>(integrantes);
    }

}
