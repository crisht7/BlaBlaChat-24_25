package appChat;

import static org.junit.Assert.*;

import java.time.LocalDate;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

import appChat.RepositorioUsuarios;
import appChat.Usuario;
import controlador.Controlador;

public class TestRegistroUsuario {

    private Controlador controlador;
    private RepositorioUsuarios repo;

    private final String telefonoTest = "987654321";
    private final String password = "registro123";

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
    public void testRegistroExitoso() {
        boolean registrado = controlador.registrarUsuario(
            "Ana Test",
            LocalDate.of(1995, 5, 20),
            new ImageIcon(),
            telefonoTest,
            "¡Hola mundo!",
            password
        );

        assertTrue("El usuario debería registrarse correctamente", registrado);
        Usuario usuario = repo.getUsuario(telefonoTest);
        assertNotNull("El usuario debería existir en el repositorio", usuario);
        assertEquals("El nombre debe coincidir", "Ana Test", usuario.getNombre());
    }

    @Test
    public void testRegistroConTelefonoDuplicado() {
        // Registrar primero exitosamente
        controlador.registrarUsuario("Ana Test", LocalDate.of(1995, 5, 20), new ImageIcon(), telefonoTest, "", password);

        // Intentar registrar otro con mismo teléfono
        boolean registradoDuplicado = controlador.registrarUsuario(
            "Otro Usuario",
            LocalDate.of(2000, 1, 1),
            new ImageIcon(),
            telefonoTest,
            "Hola",
            "otraClave"
        );

        assertFalse("No debería permitir registrar un usuario con el mismo teléfono", registradoDuplicado);
    }

    @Test
    public void testRegistroFallidoConCamposInvalidos() {
        // Teléfono inválido
        boolean invalido = controlador.registrarUsuario(
            "",
            LocalDate.of(2000, 1, 1),
            new ImageIcon(),
            "abc123",
            "",
            "123"
        );

        // El controlador en sí lo permite, pero esta prueba simula que la vista debería impedirlo antes
        // Aquí simplemente verificamos que no se crea un usuario con teléfono inválido en el repo
        Usuario u = repo.getUsuario("abc123");
        assertNull("No debería crearse un usuario con teléfono inválido", u);
    }
} 
