package persistencia;

import java.util.List;

import appChat.Usuario;

public interface UsuarioDAO {

	public List<Usuario> recuperarTodosUsuarios();

}
