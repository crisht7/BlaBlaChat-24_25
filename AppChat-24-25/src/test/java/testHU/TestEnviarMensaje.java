package testHU;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

import javax.swing.ImageIcon;

import appChat.*;
import controlador.Controlador;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import persistencia.*;

public class TestEnviarMensaje {

    @Test
    public void testEnviarMensajeTextoAContacto() throws Exception {
        // Mocks
        MensajeDAO mockMensajeDAO = mock(MensajeDAO.class);
        ContactoIndividualDAO mockContactoDAO = mock(ContactoIndividualDAO.class);
        UsuarioDAO mockUsuarioDAO = mock(UsuarioDAO.class);
        GrupoDAO mockGrupoDAO = mock(GrupoDAO.class);
        RepositorioUsuarios mockRepo = mock(RepositorioUsuarios.class);

        // Usuarios y contacto
        Usuario usuarioActual = new Usuario("Alice", new ImageIcon(), "pass", "1111", "Hola", LocalDate.of(1990, 1, 1));
        Usuario usuarioDestino = new Usuario("Bob", new ImageIcon(), "pass2", "2222", "Hey", LocalDate.of(1992, 2, 2));
        ContactoIndividual contacto = new ContactoIndividual("Bob", usuarioDestino, "2222");

        usuarioActual.añadirContacto(contacto);

        // Configurar mocks
        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1111")).thenReturn(usuarioActual);
        when(mockRepo.getUsuario("1111")).thenReturn(usuarioActual);
        when(mockRepo.getUsuarios()).thenReturn(List.of(usuarioActual, usuarioDestino));
        when(mockRepo.buscarUsuario("2222")).thenReturn(Optional.of(usuarioDestino));

        FactoriaDAO mockFactoria = mock(FactoriaDAO.class);
        when(mockFactoria.getGrupoDAO()).thenReturn(mockGrupoDAO);
        when(mockFactoria.getMensajeDAO()).thenReturn(mockMensajeDAO);
        when(mockFactoria.getUsuarioDAO()).thenReturn(mockUsuarioDAO);
        when(mockFactoria.getContactoIndividualDAO()).thenReturn(mockContactoDAO);

        try (
            MockedStatic<FactoriaDAO> mockedStatic = Mockito.mockStatic(FactoriaDAO.class);
            MockedStatic<RepositorioUsuarios> mockedRepo = Mockito.mockStatic(RepositorioUsuarios.class)
        ) {
            mockedStatic.when(() -> FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS)).thenReturn(mockFactoria);
            mockedRepo.when(RepositorioUsuarios::getUnicaInstancia).thenReturn(mockRepo);

            // Resetear singleton
            Field instance = Controlador.class.getDeclaredField("unicaInstancia");
            instance.setAccessible(true);
            instance.set(null, null);

            // Instanciar y usar el controlador
            Controlador controlador = Controlador.getInstancia();
            controlador.hacerLogin("1111", "pass");

            // Enviar mensaje
            controlador.enviarMensaje("¡Hola Bob!", contacto);

            // Verificaciones
            verify(mockMensajeDAO, times(1)).registrarMensaje(any(Mensaje.class));
            verify(mockContactoDAO, times(1)).modificarContacto(contacto);
        }
    }

    @Test
    public void testEnviarMensajeVacioNoHaceNada() throws Exception {
        // Reutilizamos mocks básicos
        MensajeDAO mockMensajeDAO = mock(MensajeDAO.class);
        ContactoIndividualDAO mockContactoDAO = mock(ContactoIndividualDAO.class);
        UsuarioDAO mockUsuarioDAO = mock(UsuarioDAO.class);
        GrupoDAO mockGrupoDAO = mock(GrupoDAO.class);
        RepositorioUsuarios mockRepo = mock(RepositorioUsuarios.class);

        Usuario usuarioActual = new Usuario("Alice", new ImageIcon(), "pass", "1111", "Hola", LocalDate.of(1990, 1, 1));
        Usuario usuarioDestino = new Usuario("Bob", new ImageIcon(), "pass2", "2222", "Hey", LocalDate.of(1992, 2, 2));
        ContactoIndividual contacto = new ContactoIndividual("Bob", usuarioDestino, "2222");

        usuarioActual.añadirContacto(contacto);

        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1111")).thenReturn(usuarioActual);
        when(mockRepo.getUsuario("1111")).thenReturn(usuarioActual);
        when(mockRepo.getUsuarios()).thenReturn(List.of(usuarioActual, usuarioDestino));
        when(mockRepo.buscarUsuario("2222")).thenReturn(Optional.of(usuarioDestino));

        FactoriaDAO mockFactoria = mock(FactoriaDAO.class);
        when(mockFactoria.getGrupoDAO()).thenReturn(mockGrupoDAO);
        when(mockFactoria.getMensajeDAO()).thenReturn(mockMensajeDAO);
        when(mockFactoria.getUsuarioDAO()).thenReturn(mockUsuarioDAO);
        when(mockFactoria.getContactoIndividualDAO()).thenReturn(mockContactoDAO);

        try (
            MockedStatic<FactoriaDAO> mockedStatic = Mockito.mockStatic(FactoriaDAO.class);
            MockedStatic<RepositorioUsuarios> mockedRepo = Mockito.mockStatic(RepositorioUsuarios.class)
        ) {
            mockedStatic.when(() -> FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS)).thenReturn(mockFactoria);
            mockedRepo.when(RepositorioUsuarios::getUnicaInstancia).thenReturn(mockRepo);

            Field instance = Controlador.class.getDeclaredField("unicaInstancia");
            instance.setAccessible(true);
            instance.set(null, null);

            Controlador controlador = Controlador.getInstancia();
            controlador.hacerLogin("1111", "pass");

            // Enviar mensaje vacío
            controlador.enviarMensaje("   ", contacto);

            verify(mockMensajeDAO, never()).registrarMensaje(any());
            verify(mockContactoDAO, never()).modificarContacto(any());
        }
    }

    @Test
    public void testEnviarEmoticono() throws Exception {
        // Mocks como en los anteriores
        MensajeDAO mockMensajeDAO = mock(MensajeDAO.class);
        ContactoIndividualDAO mockContactoDAO = mock(ContactoIndividualDAO.class);
        UsuarioDAO mockUsuarioDAO = mock(UsuarioDAO.class);
        GrupoDAO mockGrupoDAO = mock(GrupoDAO.class);
        RepositorioUsuarios mockRepo = mock(RepositorioUsuarios.class);

        Usuario usuarioActual = new Usuario("Alice", new ImageIcon(), "pass", "1111", "Hola", LocalDate.of(1990, 1, 1));
        Usuario usuarioDestino = new Usuario("Bob", new ImageIcon(), "pass2", "2222", "Hey", LocalDate.of(1992, 2, 2));
        ContactoIndividual contacto = new ContactoIndividual("Bob", usuarioDestino, "2222");

        usuarioActual.añadirContacto(contacto);

        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1111")).thenReturn(usuarioActual);
        when(mockRepo.getUsuario("1111")).thenReturn(usuarioActual);
        when(mockRepo.getUsuarios()).thenReturn(List.of(usuarioActual, usuarioDestino));
        when(mockRepo.buscarUsuario("2222")).thenReturn(Optional.of(usuarioDestino));

        FactoriaDAO mockFactoria = mock(FactoriaDAO.class);
        when(mockFactoria.getGrupoDAO()).thenReturn(mockGrupoDAO);
        when(mockFactoria.getMensajeDAO()).thenReturn(mockMensajeDAO);
        when(mockFactoria.getUsuarioDAO()).thenReturn(mockUsuarioDAO);
        when(mockFactoria.getContactoIndividualDAO()).thenReturn(mockContactoDAO);

        try (
            MockedStatic<FactoriaDAO> mockedStatic = Mockito.mockStatic(FactoriaDAO.class);
            MockedStatic<RepositorioUsuarios> mockedRepo = Mockito.mockStatic(RepositorioUsuarios.class)
        ) {
            mockedStatic.when(() -> FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS)).thenReturn(mockFactoria);
            mockedRepo.when(RepositorioUsuarios::getUnicaInstancia).thenReturn(mockRepo);

            Field instance = Controlador.class.getDeclaredField("unicaInstancia");
            instance.setAccessible(true);
            instance.set(null, null);

            Controlador controlador = Controlador.getInstancia();
            controlador.hacerLogin("1111", "pass");

            controlador.enviarMensaje(5, contacto); // enviar emoticono con ID 5

            verify(mockMensajeDAO, times(1)).registrarMensaje(any(Mensaje.class));
            verify(mockContactoDAO).modificarContacto(contacto);
        }
    }
}
