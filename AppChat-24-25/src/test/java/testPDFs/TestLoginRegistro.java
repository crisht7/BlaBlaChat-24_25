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

public class TestLoginRegistro {

    private Controlador controlador;
    private RepositorioUsuarios mockRepo;

    @Before
    public void setUp() throws Exception {
        mockRepo = mock(RepositorioUsuarios.class);
        Controlador realControlador = Controlador.getInstancia();

        java.lang.reflect.Field repoField = Controlador.class.getDeclaredField("repoUsuarios");
        repoField.setAccessible(true);
        repoField.set(realControlador, mockRepo);

        controlador = realControlador;
    }

    @Test
    public void testLoginExitoso() {
        Usuario usuario = new Usuario("Ana", new ImageIcon(), "123456", "111111111", "Hola", LocalDate.of(1990, 1, 1));
        when(mockRepo.getUsuario("111111111")).thenReturn(usuario);

        boolean login = controlador.hacerLogin("111111111", "123456");
        assertTrue(login);
    }

    @Test
    public void testLoginFallidoPorContraseña() {
        Usuario usuario = new Usuario("Ana", new ImageIcon(), "123456", "111111111", "Hola", LocalDate.of(1990, 1, 1));
        when(mockRepo.getUsuario("111111111")).thenReturn(usuario);

        boolean login = controlador.hacerLogin("111111111", "wrong");
        assertFalse(login);
    }

    @Test
    public void testRegistroNuevoUsuario() {
        when(mockRepo.getUsuario("2222")).thenReturn(null);

        Usuario nuevo = new Usuario("Carlos", new ImageIcon(), "abcd", "2222", "Hey!", LocalDate.now());
        when(mockRepo.existeUsuario(nuevo)).thenReturn(false);

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
