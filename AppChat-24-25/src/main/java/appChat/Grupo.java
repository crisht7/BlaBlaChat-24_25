package appChat;

import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

public class Grupo extends Contacto {
	
    private List<ContactoIndividual> integrantes;

    /**
     * Crea un grupo con nombre e imagen predeterminada
     * @param nombre del grupo
     */
    public Grupo(String nombre) {
    	super(nombre, new ImageIcon("src/main/resources/grupo.png"));
        this.integrantes = new LinkedList<>();
    }
    
	/**
	 * Crea un grupo con nombre e imagen personalizada
	 * 
	 * @param nombre del grupo
	 * @param imagen personalizada
	 */
	public Grupo(String nombre, ImageIcon imagen) {
		super(nombre, imagen);
		this.integrantes = new LinkedList<>();
	}
	
	/**
	 * Añade un contacto individual al grupo
	 * 
	 * @param contacto
	 */
	public void agregarIntegrante(ContactoIndividual contacto) {
		this.integrantes.add(contacto);
	}
	
	/**
	 * Elimina un contacto individual del grupo
	 * 
	 * @param contacto
	 */
	public void eliminarIntegrante(ContactoIndividual contacto) {
		this.integrantes.remove(contacto);
	}
	
	public String getTextoParticipantes() {
		if (this.integrantes == null || this.integrantes.isEmpty()) {
			return "No hay intengrantes";
		}
		
		StringBuilder textodeIntegrantes = new StringBuilder();
		for (ContactoIndividual contacto : this.integrantes) {
			textodeIntegrantes.append(contacto.getNombre() + ", ");
		}
		//Elimina el último ", "
		if (textodeIntegrantes.length() > 2) {
			textodeIntegrantes.deleteCharAt(textodeIntegrantes.length() - 2);
		}
		
		return textodeIntegrantes.toString();
	}
	
	/**
	 * Obtiene una lista de los participantes 
	 * 
	 * @return lista de contactos individuales
	 */
	public List<ContactoIndividual> getIntegrantes() {
		return new LinkedList<>(integrantes);
	}


	
	
}
