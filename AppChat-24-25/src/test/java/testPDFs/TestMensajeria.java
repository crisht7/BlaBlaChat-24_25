package testPDFs;

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
import org.mockito.ArgumentCaptor;

import persistencia.ContactoIndividualDAO;
import persistencia.GrupoDAO;
import persistencia.MensajeDAO;
import persistencia.UsuarioDAO;

public class TestMensajeria {

    private Usuario emisor;
    private Usuario receptor;
    private Usuario grupoMiembro;
    private ContactoIndividual contacto;
    private Grupo grupo;
    private Controlador controlador;
    private MensajeDAO mockMensajeDAO;
    private ContactoIndividualDAO mockContactoDAO;
    private GrupoDAO mockGrupoDAO;
    private UsuarioDAO mockUsuarioDAO;
    private RepositorioUsuarios mockRepo;

    @Before
    public void setUp() throws Exception {
        emisor = new Usuario("Ana", new ImageIcon(), "pass", "1111", "Hola", LocalDate.of(1990, 1, 1));
        receptor = new Usuario("Luis", new ImageIcon(), "pass2", "2222", "Hola!", LocalDate.of(1992, 2, 2));
        grupoMiembro = new Usuario("Carlos", new ImageIcon(), "pass3", "3333", "Hola!", LocalDate.of(1993, 3, 3));

        contacto = new ContactoIndividual("Luis", receptor, "2222");

        grupo = new Grupo("MiGrupo");
        grupo.agregarIntegrante(new ContactoIndividual("Carlos", grupoMiembro, "3333"));

        mockMensajeDAO = mock(MensajeDAO.class);
        mockContactoDAO = mock(ContactoIndividualDAO.class);
        mockGrupoDAO = mock(GrupoDAO.class);
        mockUsuarioDAO = mock(UsuarioDAO.class);
        mockRepo = mock(RepositorioUsuarios.class);

        controlador = Controlador.getInstancia();

        java.lang.reflect.Field m1 = Controlador.class.getDeclaredField("adaptadorMensaje");
        java.lang.reflect.Field m2 = Controlador.class.getDeclaredField("adaptadorContactoIndividual");
        java.lang.reflect.Field m3 = Controlador.class.getDeclaredField("adaptadorGrupo");
        java.lang.reflect.Field m4 = Controlador.class.getDeclaredField("adaptadorUsuario");
        java.lang.reflect.Field m5 = Controlador.class.getDeclaredField("repoUsuarios");

        m1.setAccessible(true); m2.setAccessible(true); m3.setAccessible(true); m4.setAccessible(true); m5.setAccessible(true);

        m1.set(controlador, mockMensajeDAO);
        m2.set(controlador, mockContactoDAO);
        m3.set(controlador, mockGrupoDAO);
        m4.set(controlador, mockUsuarioDAO);
        m5.set(controlador, mockRepo);

        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1111")).thenReturn(emisor);
        controlador.hacerLogin("1111", "pass");

        emisor.añadirContacto(contacto);
        emisor.añadirContacto(grupo);
    }

    @Test
    public void testEnviarMensajeAContacto() {
        controlador.enviarMensaje("Hola!", contacto);
        verify(mockMensajeDAO).registrarMensaje(any(Mensaje.class));
        verify(mockContactoDAO).modificarContacto(contacto);
    }

    @Test
    public void testNoEnviarMensajeVacio() {
        controlador.enviarMensaje("   ", contacto);
        verify(mockMensajeDAO, never()).registrarMensaje(any());
    }

    @Test
    public void testEnviarMensajeAGrupo() {
        controlador.enviarMensaje("Mensaje grupal", grupo);
        verify(mockMensajeDAO, times(2)).registrarMensaje(any()); // 1 por integrante + 1 al grupo
        verify(mockGrupoDAO).modificarGrupo(grupo);
    }

    @Test
    public void testEnviarEmoticono() {
        controlador.enviarMensaje(4, contacto);
        verify(mockMensajeDAO).registrarMensaje(any());
    }

    @Test
    public void testMensajeRegistraHora() {
        controlador.enviarMensaje("Con hora", contacto);

        ArgumentCaptor<Mensaje> captor = ArgumentCaptor.forClass(Mensaje.class);
        verify(mockMensajeDAO).registrarMensaje(captor.capture());

        Mensaje enviado = captor.getValue();
        assertNotNull(enviado.getHora());
        assertTrue(enviado.getHora().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
