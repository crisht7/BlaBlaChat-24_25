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
        //TODO: Adaptarlo para crearlo con una imagen predeterminada dentro de super
    	super(nombre);
        this.integrantes = new LinkedList<>();
    }
    
	/**
	 * Crea un grupo con nombre e imagen
	 * 
	 * @param nombre del grupo
	 * @param imagen personalizada
	 */
	public Grupo(String nombre, ImageIcon imagen) {
		super(nombre, imagen);
		this.integrantes = new LinkedList<>();
	}
	
	//TODO: Agregar participantes
	//TODO: Eliminar participantes
    //TODO: Getters y setters
}
