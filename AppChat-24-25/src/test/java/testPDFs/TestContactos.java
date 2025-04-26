package testPDFs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import javax.swing.ImageIcon;

import appChat.*;
import controlador.Controlador;
import org.junit.Before;
import org.junit.Test;
import persistencia.ContactoIndividualDAO;
import persistencia.UsuarioDAO;

public class TestContactos {

    private Usuario usuarioActual;
    private Usuario usuarioExistente;
    private ContactoIndividualDAO mockContactoDAO;
    private UsuarioDAO mockUsuarioDAO;
    private RepositorioUsuarios mockRepo;
    private Controlador controlador;

    @Before
    public void setUp() throws Exception {
        usuarioActual = new Usuario("Ana", new ImageIcon(), "pass", "1000", "Hola", LocalDate.of(1990, 1, 1));
        usuarioExistente = new Usuario("Carlos", new ImageIcon(), "1234", "2000", "Hey!", LocalDate.of(1992, 2, 2));

        mockRepo = mock(RepositorioUsuarios.class);
        mockContactoDAO = mock(ContactoIndividualDAO.class);
        mockUsuarioDAO = mock(UsuarioDAO.class);

        Controlador realControlador = Controlador.getInstancia();
        controlador = realControlador;

        java.lang.reflect.Field repoField = Controlador.class.getDeclaredField("repoUsuarios");
        java.lang.reflect.Field contactoField = Controlador.class.getDeclaredField("adaptadorContactoIndividual");
        java.lang.reflect.Field usuarioField = Controlador.class.getDeclaredField("adaptadorUsuario");

        repoField.setAccessible(true);
        contactoField.setAccessible(true);
        usuarioField.setAccessible(true);

        repoField.set(controlador, mockRepo);
        contactoField.set(controlador, mockContactoDAO);
        usuarioField.set(controlador, mockUsuarioDAO);

        when(mockRepo.buscarUsuario("2000")).thenReturn(Optional.of(usuarioExistente));
        when(mockRepo.buscarUsuario("9999")).thenReturn(Optional.empty());

        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1000")).thenReturn(usuarioActual);

        controlador.hacerLogin("1000", "pass");
    }


    @Test
    public void testAñadirContactoValido() {
        ContactoIndividual contacto = controlador.crearContacto("Carlos", "2000");

        assertNotNull(contacto);
        assertEquals("Carlos", contacto.getNombre());
        verify(mockContactoDAO).registrarContacto(contacto);
    }

    @Test
    public void testAñadirContactoConTelefonoInexistente() {
        ContactoIndividual contacto = controlador.crearContacto("Invalido", "9999");

        assertNull(contacto);
    }

    @Test
    public void testNoPermiteDuplicadoPorTelefono() {
        // Simular que ya existe un contacto con ese teléfono
        usuarioActual.añadirContacto(new ContactoIndividual("Carlos", usuarioExistente, "2000"));

        ContactoIndividual contacto = controlador.crearContacto("Carlos", "2000");

        assertNull(contacto);
    }

    @Test
    public void testAñadirConNombrePersonalizado() {
        ContactoIndividual contacto = controlador.crearContacto("Trabajo", "2000");

        assertNotNull(contacto);
        assertEquals("Trabajo", contacto.getNombre());
    }
}
