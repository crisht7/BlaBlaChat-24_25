package persistencia;

import java.util.List;

import appChat.Grupo;

public interface GrupoDAO {

	/**
	 * Registra un grupo en la base de datos
	 * 
	 * @param grupo
	 */
	public void registrarGrupo(Grupo grupo);

	/**
	 * Elimina un grupo de la base de datos
	 * 
	 * @param grupo
	 */
	public void borrarGrupo(Grupo grupo);

	/**
	 * Recupera un grupo de la base de datos a traves del codigo
	 * 
	 * @param codigo
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
	 * @param grupo
	 */
	public void modificarGrupo(Grupo grupo);
	
}
