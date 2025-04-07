package persistencia;

public abstract class FactoriaDAO {

	public static final String DAO_TDS = "persistencia.AdaptadorFactoriaDAO";
	
	private static FactoriaDAO unicaInstancia = null;

	public FactoriaDAO() {
	}
	/**
	 * Crea una isntancia de la fabrida DAO del tipo especificado en daoTds
	 * @param daoTds El nombre de la clase de la fabrica DAO a instanciar 
	 * @return	Una instancia unica de FactoriaDAO
	 * @throws DAOException Si ocurre un error instanciando la fabrica 
	 */
	public static FactoriaDAO getInstancia(String daoTds) throws DAOException {
	    if (unicaInstancia == null) {
	        try {
	            unicaInstancia = (FactoriaDAO) Class.forName(daoTds).getDeclaredConstructor().newInstance();
	        } catch (ClassNotFoundException e) {
	            throw new DAOException("No se encontró la clase: " + daoTds);
	        } catch (InstantiationException | IllegalAccessException e) {
	            throw new DAOException("No se pudo instanciar la clase: " + daoTds);
	        } catch (Exception e) {
	            throw new DAOException("Error desconocido en la instanciación de la factoría: " + e.getMessage());
	        }
	    }
	    return unicaInstancia;
	}
	/**
	 * Devuelve la instancia de la fabrica DAO para la gestion de usuarios
	 * @return Una implementación de UsuarioDAO
	 */
	public abstract UsuarioDAO getUsuarioDAO();

	/**
	 * Devuelve la instancia de la fabrica DAO para la gestion de Grupos
	 * @return Una implementación de GrupoDAO
	 */
	public abstract GrupoDAO getGrupoDAO();

	/**
	 * Devuelve la instancia de la fabrica DAO para la gestion de contactos individuales
	 * @return Una implementación de ContactoIndividuDAO
	 */	
	public abstract ContactoIndividualDAO getContactoIndividualDAO();
	
	/**
	 * Devuelve la instancia de la fabrica DAO para la gestion de mensajes
	 * @return Una implementación de MensajeDAO
	 */
	public abstract MensajeDAO getMensajeDAO();

	

}
