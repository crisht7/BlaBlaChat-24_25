package persistencia;

import java.util.Hashtable;

/**
 * Actúa como una caché de objetos en la base de datos.
 * Permite gestionar objetos en la base de datos
 */
public class PoolDAO {
	private static PoolDAO unicaInstancia; 
	private Hashtable<Integer, Object> pool; 

	/**
	 * Constructor privado para Singleton.
	 */
	private PoolDAO() {
		pool = new Hashtable<>();
	}

	/**
	 * Devuelve la instancia única del PoolDAO.
	 * 
	 * @return Instancia única de PoolDAO.
	 */
	public static PoolDAO getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new PoolDAO();
		}
		return unicaInstancia;
	}

	/**
	 * Recupera un objeto de la caché basado en su ID.
	 * 
	 * @param id único del objeto.
	 * @return objeto correspondiente al ID
	 */
	public Object getObjeto(int id) {
		return pool.get(id);
	}

	/**
	 * Agrega un objeto a la caché.
	 * 
	 * @param id único del objeto.
	 * @param objeto que se desea almacenar en la caché.
	 */
	public void añadirObjeto(int id, Object objeto) {
		pool.put(id, objeto);
	}

	/**
	 * Comprueba si la caché contiene un objeto con un ID específico.
	 * 
	 * @param id único del objeto.
	 * @return true si el objeto está en la caché, false en caso contrario.
	 */
	public boolean contieneID(int id) {
		return pool.containsKey(id);
	}

	/**
	 * Elimina un objeto de la caché basado en su ID.
	 * 
	 * @param id único del objeto a eliminar.
	 */
	public void EliminarObjeto(int id) {
		pool.remove(id);
	}
}

