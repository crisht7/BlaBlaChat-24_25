package appChat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        usuarios.put(usuario.getTelefono(), usuario);
    }
    
    /**
	 * Elimina un usuario del catálogo.
	 *
	 * @param user Usuario a eliminar.
	 */
	public void removeUser(Usuario user) {
		usuarios.remove(user.getTelefono());
	}
	
	/**
	 * Busca un usuario en el repositorio por su teléfono.
	 * 
	 * @param telefono
	 * @return Un optional que tiene el usuario si existe.
	 */
	public Optional<Usuario> buscarUsuario(String telefono) {
		return usuarios.values().stream().
				filter(u -> u.getTelefono().equals(telefono)).
				findFirst();
	}
	/**
	 * Devuelve una lista con todos los usuarios del repositorio.
	 * 
	 * @return Lista de usuarios.
	 */
	public List<Usuario> getUsuarios() {
		return new ArrayList<>(usuarios.values());
	}
	
	/**
	 * Devuelve un usuario del repositorio por el codigo.
	 * 
	 * @param codigo
	 * @return Usuario
	 */
	public Usuario getUsuario(int codigo) {
		return usuarios.values().stream().
				filter(u -> u.getCodigo() == codigo).
				findAny().orElse(null);
	}

	/**
	 * Devuelve un usuario del repositorio por el telefono.
	 * 
	 * @param codigo
	 * @return Usuario
	 */
	public Usuario getUsuario(String telefono) {
		return usuarios.values().stream()
				.filter(u -> u.getTelefono().equals(telefono))
				.findAny().orElse(null);
	}
	/**
	 * Comprueba si un usuario existe en el repositorio.
	 *
	 * @param usuario Usuario a comprobar.
	 * @return `true` si el usuario existe, `false` en caso contrario.
	 */
	
	public boolean existeUsuario(Usuario usuario) {
		return usuarios.containsKey(usuario.getTelefono());
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
			usuarios.put(usuario.getTelefono(), usuario);
		}
	}
}
