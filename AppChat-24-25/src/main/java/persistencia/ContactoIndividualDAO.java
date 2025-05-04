package persistencia;

import java.util.List;

import appChat.ContactoIndividual;

/**
 * Interfaz ContactoIndividualDAO que define los métodos para la persistencia de
 * contactos individuales.
 * 
 */
public interface ContactoIndividualDAO {

	
	/**
	 * Registra un contacto individual en la base de datos
	 * 
	 * @param contacto a registrar
	 */
	public void registrarContacto(ContactoIndividual contacto);

	/**
	 * Elimina un contacto individual de la base de datos
	 * 
	 * @param contacto a eliminar
	 */
	public void borrarContacto(ContactoIndividual contacto);

	/**
	 * Modifica un contacto individual de la base de datos
	 * 
	 * @param contacto a modificar
	 */
	public void modificarContacto(ContactoIndividual contacto);

	/**
	 * Recupera un contacto individual de la base de datos a traves del codigo
	 * 
	 * @param codigo del contacto a recuperar
	 * @return contacto
	 */
	public ContactoIndividual recuperarContacto(int codigo);

	/**
	 * Recupera los contactos individuales almacenados en la base de datos
	 * 
	 * @return lista de contactos individuales
	 */
	public List<ContactoIndividual> recuperarTodosContactosIndividuales();


}
