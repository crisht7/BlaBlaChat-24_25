package persistencia;

import java.util.List;

import appChat.Grupo;
import tds.driver.ServicioPersistencia;

public class AdaptadorGrupo implements GrupoDAO{

	private static AdaptadorGrupo unicaInstancia = null;
	private static ServicioPersistencia servPersistencia;
	
	/**
	 * Constructor privado Singleton
	 */
	public static AdaptadorGrupo getUnicaInstancia() {
		if (unicaInstancia == null)
            unicaInstancia = new AdaptadorGrupo();
        return unicaInstancia;
	}

	@Override
	public void registrarGrupo(Grupo grupo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void borrarGrupo(Grupo grupo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Grupo recuperarGrupo(int codigo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Grupo> recuperarTodosGrupos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modificarGrupo(Grupo grupo) {
		// TODO Auto-generated method stub
		
	}


}
