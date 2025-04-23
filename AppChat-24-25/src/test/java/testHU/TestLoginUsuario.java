package testHU;

import static org.junit.Assert.*;

import java.time.LocalDate;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

import appChat.RepositorioUsuarios;
import appChat.Usuario;
import controlador.Controlador;

public class TestLoginUsuario {

    private Controlador controlador;
    private RepositorioUsuarios repo;

    private final String telefonoTest = "123456789";
    private final String passwordCorrecta = "clave123";
    private final String passwordIncorrecta = "incorrecta";

    @Before
    public void setUp() {
        controlador = Controlador.getInstancia();
        repo = RepositorioUsuarios.getUnicaInstancia();

        // Eliminar usuario de test si ya existe
        Usuario existente = repo.getUsuario(telefonoTest);
        if (existente != null) {
            repo.eliminarUsuario(existente);
            controlador.getAdaptadorUsuario().borrarUsuario(existente);
        }
    }

    @Test
    public void testLoginCorrecto() {
        // Registrar usuario nuevo
        boolean registroExitoso = controlador.registrarUsuario(
            "Usuario Test",
            LocalDate.of(2000, 1, 1),
            new ImageIcon(),
            telefonoTest,
            "Hola!",
            passwordCorrecta
        );

        assertTrue("El registro debería ser exitoso", registroExitoso);

        // Probar login correcto
        boolean loginExitoso = controlador.hacerLogin(telefonoTest, passwordCorrecta);
        assertTrue("El login con credenciales correctas debería ser exitoso", loginExitoso);

        // Verificar que el usuario actual es el que registramos
        Usuario usuarioActual = controlador.getUsuarioActual();
        assertNotNull("El usuario actual no debería ser null luego de un login exitoso", usuarioActual);
        assertEquals("El teléfono del usuario logueado debe coincidir", telefonoTest, usuarioActual.getTelefono());
    }

    @Test
    public void testLoginContrasenaIncorrecta() {
        // Registrar usuario
        controlador.registrarUsuario(
            "Usuario Test",
            LocalDate.of(2000, 1, 1),
            new ImageIcon(),
            telefonoTest,
            "Hola!",
            passwordCorrecta
        );

        // Intentar login con contraseña incorrecta
        boolean login = controlador.hacerLogin(telefonoTest, passwordIncorrecta);
        assertFalse("El login con contraseña incorrecta debe fallar", login);
    }

    @Test
    public void testLoginTelefonoInexistente() {
        // Intentar login con teléfono que no existe
        boolean login = controlador.hacerLogin("999999999", passwordCorrecta);
        assertFalse("El login con teléfono inexistente debe fallar", login);
    }
}
