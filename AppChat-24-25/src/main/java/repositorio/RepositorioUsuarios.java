package repositorio;

import java.util.HashMap;
import java.util.Map;

import modelo.Usuario;

public class RepositorioUsuarios {
    private Map< Integer,Usuario> usuarios;
    private Usuario usuarioActual;

    public RepositorioUsuarios() {
        usuarios = new HashMap<>();
    }

    public void agregarUsuario(int telefono, Usuario usuario) {
        usuarios.put(telefono, usuario);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    // Otros métodos para gestionar usuarios
}
