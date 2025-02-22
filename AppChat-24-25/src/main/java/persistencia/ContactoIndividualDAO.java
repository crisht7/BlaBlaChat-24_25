package persistencia;

import java.util.List;

import appChat.ContactoIndividual;

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
	 * @param contacto
	 */
	public void modificarContacto(ContactoIndividual contacto);

	/**
	 * Recupera un contacto individual de la base de datos a traves del codigo
	 * 
	 * @param codigo
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
