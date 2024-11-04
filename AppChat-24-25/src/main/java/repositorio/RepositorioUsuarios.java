package repositorio;

import java.util.ArrayList;
import java.util.List;

import modelo.Usuario;

public class RepositorioUsuarios {
    private List<Usuario> usuarios;
    private Usuario usuarioActual;

    public RepositorioUsuarios() {
        usuarios = new ArrayList<>();
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    // Otros métodos para gestionar usuarios
}
