package persistencia;

import java.util.List;

import appChat.Mensaje;

/**
 * Interfaz MensajeDAO que define los métodos para la persistencia de mensajes.
 * 
 */
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
	 * @param mensaje a modificar
	 */
	public void modificarMensaje(Mensaje mensaje);

	/**
     * Recupera un mensaje de la base de datos a traves del codigo
     * 
     * @param codigo codigo del mensaje a recuperar
     * @return mensaje
     */
	public Mensaje recuperarMensaje(int codigo);

	/**
	 * Recupera los mensajes almacenados en la base de datos
	 * 
	 * @return lista de mensajes 
	 */
	public List<Mensaje> recuperarTodosMensajes();
	
	/**
	 * Devuelve todos los mensajes enviados por un número de teléfono específico.
	 *
	 * @param telefono Teléfono del emisor.
	 * @return Lista de mensajes enviados por ese teléfono.
	 */
	public List<Mensaje> getMensajesEnviadosPor(String telefono);

}
