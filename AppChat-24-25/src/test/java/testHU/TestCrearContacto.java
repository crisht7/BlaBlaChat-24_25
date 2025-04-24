package testHU;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import appChat.ContactoIndividual;
import appChat.RepositorioUsuarios;
import appChat.Usuario;
import controlador.Controlador;
import persistencia.UsuarioDAO;
import persistencia.ContactoIndividualDAO;
import persistencia.MensajeDAO;
import persistencia.GrupoDAO;

public class TestCrearContacto {

    private Controlador controlador;
    private RepositorioUsuarios mockRepo;
    private UsuarioDAO mockUsuarioDAO;
    private ContactoIndividualDAO mockContactoDAO;
    private MensajeDAO mockMensajeDAO;
    private GrupoDAO mockGrupoDAO;

    private final String telefonoUsuario = "111111111";
    private final String telefonoContacto = "222222222";
    private final String telefonoInexistente = "999999999";

    private Usuario usuarioPrincipal;
    private Usuario usuarioContacto;

    @Before
    public void setUp() throws Exception {
        // Crear mocks
        mockRepo = mock(RepositorioUsuarios.class);
        mockUsuarioDAO = mock(UsuarioDAO.class);
        mockContactoDAO = mock(ContactoIndividualDAO.class);
        mockMensajeDAO = mock(MensajeDAO.class);
        mockGrupoDAO = mock(GrupoDAO.class);

        // Crear usuarios reales (NO mock)
        usuarioPrincipal = new Usuario("Usuario Principal", new ImageIcon(), "clave1", telefonoUsuario, "", LocalDate.of(2000, 1, 1));
        usuarioContacto = new Usuario("Usuario Contacto", new ImageIcon(), "clave2", telefonoContacto, "", LocalDate.of(2001, 2, 2));

        // Mockear singleton de RepositorioUsuarios
        try (MockedStatic<RepositorioUsuarios> mockedStaticRepo = mockStatic(RepositorioUsuarios.class)) {
            mockedStaticRepo.when(RepositorioUsuarios::getUnicaInstancia).thenReturn(mockRepo);

            controlador = Controlador.getInstancia();

            // Inyectar mocks por reflexión
            setField("adaptadorUsuario", mockUsuarioDAO);
            setField("adaptadorContactoIndividual", mockContactoDAO);
            setField("adaptadorMensaje", mockMensajeDAO);
            setField("adaptadorGrupo", mockGrupoDAO);
            setField("repoUsuarios", mockRepo);

            // Configurar mocks
            when(mockUsuarioDAO.recuperarUsuarioPorTelefono(telefonoUsuario)).thenReturn(usuarioPrincipal);
            when(mockRepo.getUsuario(telefonoUsuario)).thenReturn(usuarioPrincipal);
            when(mockRepo.buscarUsuario(telefonoContacto)).thenReturn(Optional.of(usuarioContacto));
            when(mockRepo.buscarUsuario(telefonoInexistente)).thenReturn(Optional.empty());

            // Login del usuario principal
            controlador.hacerLogin(telefonoUsuario, "clave1");
        }
    }

    private void setField(String fieldName, Object value) throws Exception {
        var field = Controlador.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controlador, value);
    }

    @Test
    public void testCrearContactoValido() {
        ContactoIndividual contacto = controlador.crearContacto("Amigo", telefonoContacto);
        assertNotNull("El contacto debería crearse exitosamente", contacto);
        assertEquals("Amigo", contacto.getNombre());
        assertEquals(telefonoContacto, contacto.getTelefono());
    }

    @Test
    public void testCrearContactoDuplicado() {
        ContactoIndividual primero = controlador.crearContacto("Amigo", telefonoContacto);
        assertNotNull("El primer contacto debería haberse creado", primero);

        // Ver contactos después del primer insert
        System.out.println("Contactos actuales:");
        controlador.getUsuarioActual().getContactos().forEach(c ->
            System.out.println(" - " + c.getNombre() + ": " + ((ContactoIndividual)c).getTelefono())
        );

        // Intentar crear duplicado
        ContactoIndividual duplicado = controlador.crearContacto("Amigo2", telefonoContacto);
        assertNull("No debería poder crearse un contacto duplicado", duplicado);
    }

    @Test
    public void testCrearContactoInexistente() {
        ContactoIndividual noExiste = controlador.crearContacto("Desconocido", telefonoInexistente);
        assertNull("No debería poder crearse un contacto con teléfono inexistente", noExiste);
    }
}
