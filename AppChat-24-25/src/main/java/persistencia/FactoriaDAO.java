package persistencia;

public abstract class FactoriaDAO {

	public static final String DAO_TDS = null;
	
	private static FactoriaDAO unicaInstancia = null;

	public static FactoriaDAO getInstancia(String daoTds) {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract UsuarioDAO getUsuarioDAO();

	

}
