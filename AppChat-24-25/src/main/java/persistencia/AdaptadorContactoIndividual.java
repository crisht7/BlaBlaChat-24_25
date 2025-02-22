package persistencia;


import java.util.List;

import appChat.ContactoIndividual;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Clase que implementa el Adaptador de ContactoIndividual para base de datos 
 * con DAO
 */
public class AdaptadorContactoIndividual implements ContactoIndividualDAO {

	private static AdaptadorContactoIndividual unicaInstancia = null;
	private static ServicioPersistencia servPersistencia;
	
	/**
	 * Constructor privado Singleton
	 */
	private AdaptadorContactoIndividual() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	/**
	 * Devuelve la única instancia unica de la clase 
	 * Garantiza Singleton
	 * 
	 * @return unicaInstancia
	 */
	public static AdaptadorContactoIndividual getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorContactoIndividual();
		return unicaInstancia;
	}
	

	public void registrarContacto(ContactoIndividual contacto) {
		//TODO Auto-generate method stub
    }


	@Override
	public void borrarContacto(ContactoIndividual contacto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modificarContacto(ContactoIndividual contacto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContactoIndividual recuperarContacto(int codigo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactoIndividual> recuperarTodosContactosIndividuales() {
		// TODO Auto-generated method stub
		return null;
	}



}



