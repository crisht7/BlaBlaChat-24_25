package testAFallos;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import javax.swing.ImageIcon;

import appChat.*;
import controlador.Controlador;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import persistencia.ContactoIndividualDAO;
import persistencia.MensajeDAO;
import persistencia.UsuarioDAO;

public class TestResponderDesconocido {

    private Usuario receptor;
    private Usuario emisor;
    private MensajeDAO mockMensajeDAO;
    private ContactoIndividualDAO mockContactoDAO;
    private UsuarioDAO mockUsuarioDAO;
    private RepositorioUsuarios mockRepo;
    private Controlador controlador;

    @Before
    public void setUp() throws Exception {
        receptor = new Usuario("Ana", new ImageIcon(), "pass", "1000", "Hola", LocalDate.of(1990, 1, 1));
        emisor = new Usuario("Carlos", new ImageIcon(), "1234", "2000", "Hey!", LocalDate.of(1991, 1, 1));

        mockMensajeDAO = mock(MensajeDAO.class);
        mockContactoDAO = mock(ContactoIndividualDAO.class);
        mockUsuarioDAO = mock(UsuarioDAO.class);
        mockRepo = mock(RepositorioUsuarios.class);

        // Mock estático de RepositorioUsuarios.getUnicaInstancia()
        try (MockedStatic<RepositorioUsuarios> mockedStaticRepo = mockStatic(RepositorioUsuarios.class)) {
            mockedStaticRepo.when(RepositorioUsuarios::getUnicaInstancia).thenReturn(mockRepo);

            controlador = Controlador.getInstancia();

            // Inyectamos los mocks usando reflexión
            java.lang.reflect.Field f1 = Controlador.class.getDeclaredField("adaptadorMensaje");
            java.lang.reflect.Field f2 = Controlador.class.getDeclaredField("adaptadorContactoIndividual");
            java.lang.reflect.Field f3 = Controlador.class.getDeclaredField("adaptadorUsuario");
            java.lang.reflect.Field f4 = Controlador.class.getDeclaredField("repoUsuarios");

            f1.setAccessible(true); f2.setAccessible(true); f3.setAccessible(true); f4.setAccessible(true);
            f1.set(controlador, mockMensajeDAO);
            f2.set(controlador, mockContactoDAO);
            f3.set(controlador, mockUsuarioDAO);
            f4.set(controlador, mockRepo);

            when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1000")).thenReturn(receptor);
            when(mockRepo.buscarUsuario("2000")).thenReturn(Optional.of(emisor));

            controlador.hacerLogin("1000", "pass");
        }
    }

    @Test
    public void testResponderMensajeDeDesconocido() {
        ContactoIndividual contactoAnonimo = new ContactoIndividual("2000", emisor, "2000");

        assertFalse(receptor.tieneContactoIndividualPorTelefono("2000"));

        controlador.enviarMensaje("¡Hola! No te tenía agregado", contactoAnonimo);

        ArgumentCaptor<ContactoIndividual> captor = ArgumentCaptor.forClass(ContactoIndividual.class);
        verify(mockContactoDAO, times(2)).registrarContacto(captor.capture());

        boolean emisorRegistrado = captor.getAllValues().stream()
            .anyMatch(c -> c.getTelefono().equals("2000"));
        boolean receptorRegistrado = captor.getAllValues().stream()
            .anyMatch(c -> c.getTelefono().equals("1000"));

        assertTrue(emisorRegistrado);
        assertTrue(receptorRegistrado);

        verify(mockMensajeDAO).registrarMensaje(any(Mensaje.class));
        verify(mockUsuarioDAO).modificarUsuario(receptor);
    }
}
