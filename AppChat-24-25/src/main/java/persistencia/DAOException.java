package persistencia;

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
