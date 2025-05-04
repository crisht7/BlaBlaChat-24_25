package persistencia;

import java.util.List;

import appChat.Grupo;

/**
 * Interfaz GrupoDAO que define los métodos para la persistencia de grupos.
 * 
 */
public interface GrupoDAO {

	/**
	 * Registra un grupo en la base de datos
	 * 
	 * @param grupo a registrar
	 */
	public void registrarGrupo(Grupo grupo);

	/**
	 * Elimina un grupo de la base de datos
	 * 
	 * @param grupo a eliminar
	 */
	public void borrarGrupo(Grupo grupo);

	/**
	 * Recupera un grupo de la base de datos a traves del codigo
	 * 
	 * @param codigo codigo del grupo a recuperar
	 * @return grupo
	 */
	public Grupo recuperarGrupo(int codigo);

	/**
	 * Recupera los grupos almacenados en la base de datos
	 * 
	 * @return lista de grupos
	 */
	public List<Grupo> recuperarTodosGrupos();

	/**
	 * Modifica un grupo de la base de datos
	 * 
	 * @param grupo a modificar
	 */
	public void modificarGrupo(Grupo grupo);
	
}
