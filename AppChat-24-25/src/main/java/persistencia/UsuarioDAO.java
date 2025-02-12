package persistencia;

import java.util.List;

import appChat.Usuario;

/**
 * Interfaz que define los métodos que se pueden realizar sobre la tabla de
 * usuarios, sigue la interzar DAO 
 * Realiza operaciones CRUD y recupera usuarios almacenados
 *
 */
public interface UsuarioDAO {
	
	/**
	 * Registra un usuario en la base de datos
	 * 
	 * @param usuario
	 */
	public void registrarUsuario(Usuario usuario);
	
	/**
	 * Elimina un usuario de la base de datos
	 * 
	 * @param usuario
	 */
	public void borrarUsuario(Usuario usuario);

	/**
	 * Recupera el usuario de la base de datos a traves del codigo
	 * 
	 * @param codigo
	 */
	public void recuperarUsuario(int codigo);
	
	/**
	 * Recupera los usuarios almacenados en la base de datos
	 * 
	 * @return lista de usuarios registrados
	 */
	public List<Usuario> recuperarTodosUsuarios();
	
	/**
	 * Modifica un usuario de la base de datos
	 * 
	 * @param usuario
	 */
	public void modificarUsuario(Usuario usuario);

	
}
