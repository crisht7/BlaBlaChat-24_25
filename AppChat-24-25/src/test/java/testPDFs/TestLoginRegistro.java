package testPDFs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import javax.swing.ImageIcon;

import appChat.RepositorioUsuarios;
import appChat.Usuario;
import controlador.Controlador;
import org.junit.Before;
import org.junit.Test;
import persistencia.UsuarioDAO;

public class TestLoginRegistro {

    private Controlador controlador;
    private RepositorioUsuarios mockRepo;
    private UsuarioDAO mockAdaptadorUsuario;

    @Before
    public void setUp() throws Exception {
        mockRepo = mock(RepositorioUsuarios.class);
        mockAdaptadorUsuario = mock(UsuarioDAO.class);

        Controlador realControlador = Controlador.getInstancia();

        // Inyectar mock de repositorio
        java.lang.reflect.Field repoField = Controlador.class.getDeclaredField("repoUsuarios");
        repoField.setAccessible(true);
        repoField.set(realControlador, mockRepo);

        // Inyectar mock de adaptadorUsuario
        java.lang.reflect.Field adaptadorUsuarioField = Controlador.class.getDeclaredField("adaptadorUsuario");
        adaptadorUsuarioField.setAccessible(true);
        adaptadorUsuarioField.set(realControlador, mockAdaptadorUsuario);

        controlador = realControlador;
    }

    @Test
    public void testLoginExitoso() {
        Usuario usuario = new Usuario("Ana", new ImageIcon(), "123456", "111111111", "Hola", LocalDate.of(1990, 1, 1));

        // 🔥 Mock necesario para hacerLogin
        when(mockAdaptadorUsuario.recuperarUsuarioPorTelefono("111111111")).thenReturn(usuario);

        boolean login = controlador.hacerLogin("111111111", "123456");
        assertTrue(login);
    }

    @Test
    public void testLoginFallidoPorContraseña() {
        Usuario usuario = new Usuario("Ana", new ImageIcon(), "123456", "111111111", "Hola", LocalDate.of(1990, 1, 1));

        // 🔥 Mock necesario para hacerLogin
        when(mockAdaptadorUsuario.recuperarUsuarioPorTelefono("111111111")).thenReturn(usuario);

        boolean login = controlador.hacerLogin("111111111", "wrong");
        assertFalse(login);
    }

    @Test
    public void testRegistroNuevoUsuario() {
        when(mockRepo.getUsuario("2222")).thenReturn(null);

        Usuario nuevo = new Usuario("Carlos", new ImageIcon(), "abcd", "2222", "Hey!", LocalDate.now());
        when(mockRepo.existeUsuario(nuevo)).thenReturn(false);

        // 🔥 Mock necesario porque registrarUsuario llama a hacerLogin internamente
        when(mockAdaptadorUsuario.recuperarUsuarioPorTelefono("2222")).thenReturn(nuevo);

        boolean registrado = controlador.registrarUsuario(
                nuevo.getNombre(), nuevo.getFechaRegistro(), nuevo.getFotoPerfil(),
                nuevo.getTelefono(), nuevo.getSaludo(), nuevo.getContraseña());

        assertTrue(registrado);
    }

    @Test
    public void testRegistroFallidoUsuarioExistente() {
        Usuario existente = new Usuario("Luis", new ImageIcon(), "pass", "3333", "Hola", LocalDate.now());

        when(mockRepo.getUsuario("3333")).thenReturn(existente);

        boolean registrado = controlador.registrarUsuario(
                existente.getNombre(), existente.getFechaRegistro(), existente.getFotoPerfil(),
                existente.getTelefono(), existente.getSaludo(), existente.getContraseña());

        assertFalse(registrado);
    }
}
