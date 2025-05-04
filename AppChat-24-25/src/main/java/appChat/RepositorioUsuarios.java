package appChat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.UsuarioDAO;

/**
 * Repositorio de usuarios de la aplicación. 
 * Implementa el patrón Singleton para mantener una única instancia.
 */
public class RepositorioUsuarios {

	/**
	 * Mapa que almacena los usuarios, donde la clave es el número de teléfono.
	 */
    private Map<String, Usuario> usuarios;
	/**
	 * Instancia única del repositorio de usuarios.
	 */
    private static RepositorioUsuarios instanciaUnica;
    /**
     * Fábrica de DAOs para acceder a la base de datos.
     */
    private FactoriaDAO factoria;
	/**
	 * DAO para acceder a los usuarios en la base de datos.
	 */
    private UsuarioDAO daoUsuario;

    // ===================== Constructor =====================

    /**
     * Constructor privado.
     * Carga los usuarios de la base de datos.
     */
    private RepositorioUsuarios() {
        try {
            this.factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
            if (factoria == null) {
                throw new RuntimeException("Error: FactoriaDAO no está inicializada correctamente.");
            }
            this.daoUsuario = factoria.getUsuarioDAO();
            this.usuarios = new HashMap<>();
            this.cargarRepositorio();
        } catch (DAOException eDAO) {
            eDAO.printStackTrace();
        }
    }

    /**
     * Devuelve la instancia única del repositorio de usuarios.
     * 
     * @return instancia única
     */
    public static RepositorioUsuarios getUnicaInstancia() {
        if (instanciaUnica == null)
            instanciaUnica = new RepositorioUsuarios();
        return instanciaUnica;
    }

    // ===================== Operaciones CRUD =====================

    /**
     * Agrega un usuario al repositorio.
     * 
     * @param usuario usuario a agregar
     */
    public void agregarUsuario(Usuario usuario) {
        usuarios.put(usuario.getTelefono(), usuario);
    }

    /**
     * Elimina un usuario del repositorio.
     * 
     * @param user usuario a eliminar
     */
    public void eliminarUsuario(Usuario user) {
        usuarios.remove(user.getTelefono());
    }

    /**
     * Comprueba si un usuario existe en el repositorio.
     * 
     * @param usuario usuario a comprobar
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean existeUsuario(Usuario usuario) {
        return usuarios.containsKey(usuario.getTelefono());
    }

    // ===================== Métodos de Búsqueda =====================

    /**
     * Busca un usuario por su número de teléfono.
     * 
     * @param telefono número de teléfono
     * @return Optional que contiene el usuario si existe
     */
    public Optional<Usuario> buscarUsuario(String telefono) {
        return usuarios.values().stream()
                .filter(u -> u.getTelefono().equals(telefono))
                .findFirst();
    }

    /**
     * Devuelve una lista con todos los usuarios del repositorio.
     * 
     * @return lista de usuarios
     */
    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    /**
     * Devuelve un usuario del repositorio por su código.
     * 
     * @param codigo código del usuario
     * @return usuario encontrado, o null si no existe
     */
    public Usuario getUsuario(int codigo) {
        return usuarios.values().stream()
                .filter(u -> u.getCodigo() == codigo)
                .findAny()
                .orElse(null);
    }

    /**
     * Devuelve un usuario del repositorio por su teléfono.
     * 
     * @param telefono número de teléfono
     * @return usuario encontrado, o null si no existe
     */
    public Usuario getUsuario(String telefono) {
        return usuarios.get(telefono);
    }

    /**
     * Devuelve un usuario del repositorio por número de teléfono (como Optional).
     * 
     * @param numTelefono número de teléfono
     * @return Optional de usuario encontrado
     */
    public Optional<Usuario> getUsuarioNumTelf(String numTelefono) {
        return Optional.ofNullable(usuarios.get(numTelefono));
    }

    // ===================== Métodos Internos =====================

    /**
     * Carga todos los usuarios desde la base de datos y los almacena en el repositorio.
     * 
     * @throws DAOException si ocurre un error al interactuar con la base de datos
     */
    private void cargarRepositorio() throws DAOException {
        List<Usuario> usuariosBD = daoUsuario.recuperarTodosUsuarios();
        for (Usuario usuario : usuariosBD) {
            usuarios.put(usuario.getTelefono(), usuario);
        }
    }
}
