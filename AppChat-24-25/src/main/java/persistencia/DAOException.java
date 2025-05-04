package persistencia;

/**
 * Clase que representa una excepción específica para la capa de acceso a datos
 * (DAO).
 * 
 * Esta clase hereda de {@link Exception} y se utiliza para indicar errores
 * relacionados con la persistencia de datos en la aplicación.
 *
 * @see Exception
 */
@SuppressWarnings("serial")
public class DAOException extends Exception{
	/**
	 * Constructor que crea una nueva instancia de {@link DAOException} con un
	 * mensaje de error específico.
	 *
	 * @param mensaje El mensaje que describe el error.
	 */
	public DAOException(String mensaje) {
		super(mensaje);
	}

}
