package persistencia;

/**
 * Implementación concreta de la clase FactoriaDAO	
 * Mediante el patron Singleton proporciona instancias de los adaptadores de la base de datos
 * especificos para gestionarlos
 */
public class AdaptadorFactoriaDAO extends FactoriaDAO{

	/**
	 * Constructor, asegura que se mantenga una única instancia de la clase
	 */
	public AdaptadorFactoriaDAO() {
		
	}

	/**
	 * Devuelve la instancia de la fabrica DAO para la gestion de usuarios
	 * @return Una instancia unica de UsuarioDAO
	 */
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuario.getUnicaInstancia();
	}
	
	/**
	 * Devuelve la instancia de la fabrica DAO para la gestion de grupos
	 * 
	 * @return Una instancia unica de GrupoDAO
	 */
	@Override
	public GrupoDAO getGrupoDAO() {
		return AdaptadorGrupo.getUnicaInstancia();
	}

	/**
	 * Devuelve la instancia de la fabrica DAO para la gestion de contactos
	 * individuales
	 * 
	 * @return Una instancia unica de ContactoIndividualDAO
	 */
	@Override
	public ContactoIndividualDAO getContactoIndividualDAO() {
		return AdaptadorContactoIndividual.getUnicaInstancia();
	}

	/**
	 * Devuelve la instancia de la fabrica DAO para la gestion de mensajes
	 * 
	 * @return Una instancia unica de MensajeDAO
	 */
	@Override
	public MensajeDAO getMensajeDAO() {
		return AdaptadorMensaje.getUnicaInstancia();
	}
}
