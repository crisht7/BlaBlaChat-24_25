package appChat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.UsuarioDAO;

public class RepositorioUsuarios {
    private Map<String,Usuario> usuarios;
    private static RepositorioUsuarios instanciaUnica;
    private FactoriaDAO factoria;
    private UsuarioDAO daoUsuario;

    
    private RepositorioUsuarios() {
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			daoUsuario = factoria.getUsuarioDAO();
			usuarios = new HashMap<>();
			this.cargarRepositorio();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
    }
    
    /**
	 * Devuelve la instancia única del catálogo de usuarios.
	 *
	 * @return Instancia única de `UserCatalog`.
	 */
	public static RepositorioUsuarios getUnicaInstancia() {
		if (instanciaUnica == null)
			instanciaUnica = new RepositorioUsuarios();
		return instanciaUnica;
	}

    /**
     * Agrega un usuario al repositorio
     * 
     * @param telefono
     * @param usuario
     */
    public void agregarUsuario(int telefono, Usuario usuario) {
        usuarios.put(usuario.getNumero(), usuario);
    }
    
    /**
	 * Elimina un usuario del catálogo.
	 *
	 * @param user Usuario a eliminar.
	 */
	public void removeUser(Usuario user) {
		usuarios.remove(user.getNumero());
	}

	
	//TODO: Getters y setters
	//TODO: Buscar usuario
	//TODO: Existe usuario
	
	public Usuario getUsuario(String telefono) {
		return usuarios.get(telefono);
	}

    /**
	 * Carga todos los usuarios desde la base de datos y los almacena en el
	 * catálogo.
	 *
	 * @throws DAOException Si ocurre un error al interactuar con la capa de
	 *                      persistencia.
	 */	
	
    private void cargarRepositorio() throws DAOException {
		List<Usuario> usuariosBD = daoUsuario.recuperarTodosUsuarios();
		for (Usuario usuario : usuariosBD) {
			usuarios.put(usuario.getNumero(), usuario);
		}
	}
}
