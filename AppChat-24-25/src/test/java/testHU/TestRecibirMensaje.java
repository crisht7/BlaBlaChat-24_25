package testHU;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import javax.swing.ImageIcon;

import appChat.*;
import controlador.Controlador;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import persistencia.*;

public class TestRecibirMensaje {

    private Controlador controlador;
    private Usuario usuarioActual;
    private Usuario otroUsuario;
    private ContactoIndividualDAO mockContactoDAO;
    private MensajeDAO mockMensajeDAO;
    private UsuarioDAO mockUsuarioDAO;
    private GrupoDAO mockGrupoDAO;
    private RepositorioUsuarios mockRepo;

    @Before
    public void setUp() {
        mockContactoDAO = mock(ContactoIndividualDAO.class);
        mockMensajeDAO = mock(MensajeDAO.class);
        mockUsuarioDAO = mock(UsuarioDAO.class);
        mockGrupoDAO = mock(GrupoDAO.class);
        mockRepo = mock(RepositorioUsuarios.class);

        usuarioActual = new Usuario("Alice", new ImageIcon(), "1234", "1111", "Hola", LocalDate.of(2000, 1, 1));
        otroUsuario = new Usuario("Bob", new ImageIcon(), "5678", "2222", "Hola!", LocalDate.of(1995, 5, 15));
        otroUsuario.setCodigo(2);

        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1111")).thenReturn(usuarioActual);
        when(mockRepo.getUsuario("1111")).thenReturn(usuarioActual);
        when(mockRepo.getUsuarios()).thenReturn(Arrays.asList(usuarioActual, otroUsuario));
        when(mockRepo.buscarUsuario("2222")).thenReturn(Optional.of(otroUsuario));

        FactoriaDAO mockFactoria = mock(FactoriaDAO.class);
        when(mockFactoria.getContactoIndividualDAO()).thenReturn(mockContactoDAO);
        when(mockFactoria.getMensajeDAO()).thenReturn(mockMensajeDAO);
        when(mockFactoria.getUsuarioDAO()).thenReturn(mockUsuarioDAO);
        when(mockFactoria.getGrupoDAO()).thenReturn(mockGrupoDAO);

        try (MockedStatic<FactoriaDAO> mockedStatic = Mockito.mockStatic(FactoriaDAO.class);
             MockedStatic<RepositorioUsuarios> mockedRepo = Mockito.mockStatic(RepositorioUsuarios.class)) {

            mockedStatic.when(() -> FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS)).thenReturn(mockFactoria);
            mockedRepo.when(RepositorioUsuarios::getUnicaInstancia).thenReturn(mockRepo);

            controlador = Controlador.getInstancia();
            controlador.hacerLogin("1111", "1234");
        }
    }

    @Test
    public void testRecibirMensajeDeUsuarioDesconocido() {
        ContactoIndividual contactoReceptor = new ContactoIndividual("1111", usuarioActual, "1111");
        Mensaje mensaje = new Mensaje("Hola Alice!", LocalDateTime.now(), otroUsuario, contactoReceptor);

        when(mockMensajeDAO.getMensajesEnviadosPor("2222")).thenReturn(Collections.singletonList(mensaje));

        assertFalse(usuarioActual.tieneContactoIndividualPorTelefono("2222"));

        List<Contacto> recientes = controlador.getContactosUsuarioActual();

        Optional<Contacto> bobContacto = recientes.stream()
                .filter(c -> c instanceof ContactoIndividual)
                .filter(c -> ((ContactoIndividual) c).getTelefono().equals("2222"))
                .findFirst();

        assertTrue(bobContacto.isPresent());
        ContactoIndividual contactoBob = (ContactoIndividual) bobContacto.get();
        assertEquals("2222", contactoBob.getTelefono());

        when(mockRepo.buscarUsuario("2222")).thenReturn(Optional.of(otroUsuario));
        ContactoIndividual nuevoContacto = controlador.crearContacto("Bob", "2222");

        assertNotNull(nuevoContacto);
        assertEquals("Bob", nuevoContacto.getNombre());
        verify(mockContactoDAO).registrarContacto(nuevoContacto);
    }
}
