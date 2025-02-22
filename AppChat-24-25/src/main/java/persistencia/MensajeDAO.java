package persistencia;

import java.util.List;

import appChat.Mensaje;

public interface MensajeDAO {

	
	/**
	 * Registra un mensaje en la base de datos
	 * 
	 * @param mensaje a registrar
	 */
	public void registrarMensaje(Mensaje mensaje);
	/**
	 * Elimina un mensaje de la base de datos
	 * 
	 * @param mensaje a eliminar
	 */
	public void borrarMensaje(Mensaje mensaje);
	
	/**
	 * Modifica un mensaje de la base de datos
	 * 
	 * @param mensaje
	 */
	public void modificarMensaje(Mensaje mensaje);

	/**
     * Recupera un mensaje de la base de datos a traves del codigo
     * 
     * @param codigo
     * @return mensaje
     */
	public Mensaje recuperarMensaje(int codigo);

	/**
	 * Recupera los mensajes almacenados en la base de datos
	 * 
	 * @return lista de mensajes 
	 */
	public List<Mensaje> recuperarTodosMensajes();
	

}
