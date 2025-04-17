package controlador;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Optional;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

import appChat.ContactoIndividual;
import appChat.RepositorioUsuarios;
import appChat.Usuario;
import controlador.Controlador;

public class TestCrearContacto {

    private Controlador controlador;
    private RepositorioUsuarios repo;
    private final String telefonoUsuario = "111111111";
    private final String telefonoContacto = "222222222";
    private final String telefonoInexistente = "999999999";

    @Before
    public void setUp() {
        controlador = Controlador.getInstancia();
        repo = RepositorioUsuarios.getUnicaInstancia();

        // Limpiar posibles restos de datos
        Usuario existente1 = repo.getUsuario(telefonoUsuario);
        Usuario existente2 = repo.getUsuario(telefonoContacto);

        if (existente1 != null) {
            repo.eliminarUsuario(existente1);
            controlador.getAdaptadorUsuario().borrarUsuario(existente1);
        }

        if (existente2 != null) {
            repo.eliminarUsuario(existente2);
            controlador.getAdaptadorUsuario().borrarUsuario(existente2);
        }

        // Registrar usuario actual y otro usuario válido como contacto
        controlador.registrarUsuario("Usuario Principal", LocalDate.of(2000, 1, 1), new ImageIcon(), telefonoUsuario, "", "clave1");
        controlador.registrarUsuario("Usuario Contacto", LocalDate.of(2001, 2, 2), new ImageIcon(), telefonoContacto, "", "clave2");
    }

    @Test
    public void testCrearContactoValido() {
        ContactoIndividual contacto = controlador.crearContacto("Amigo", telefonoContacto);
        assertNotNull("El contacto debería crearse exitosamente", contacto);
        assertEquals("El nombre del contacto debe coincidir", "Amigo", contacto.getNombre());
        assertEquals("El teléfono debe coincidir", telefonoContacto, contacto.getTelefono());
    }

    @Test
    public void testCrearContactoDuplicado() {
        controlador.crearContacto("Amigo", telefonoContacto);
        ContactoIndividual duplicado = controlador.crearContacto("Amigo2", telefonoContacto);
        assertNull("No debería poder crearse un contacto duplicado", duplicado);
    }

    @Test
    public void testCrearContactoInexistente() {
        ContactoIndividual noExiste = controlador.crearContacto("Desconocido", telefonoInexistente);
        assertNull("No debería poder crearse un contacto con teléfono inexistente", noExiste);
    }
} 
