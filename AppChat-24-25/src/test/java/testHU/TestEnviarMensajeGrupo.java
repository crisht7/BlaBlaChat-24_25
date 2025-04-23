package testHU;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import javax.swing.ImageIcon;

import appChat.*;
import controlador.Controlador;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import persistencia.*;

public class TestEnviarMensajeGrupo {

    @Test
    public void testEnviarMensajeATodosLosIntegrantesDelGrupo() throws Exception {
        // Mocks de DAOs y repositorio
        GrupoDAO mockGrupoDAO = mock(GrupoDAO.class);
        MensajeDAO mockMensajeDAO = mock(MensajeDAO.class);
        UsuarioDAO mockUsuarioDAO = mock(UsuarioDAO.class);
        ContactoIndividualDAO mockContactoDAO = mock(ContactoIndividualDAO.class);
        RepositorioUsuarios mockRepo = mock(RepositorioUsuarios.class);

        // Usuarios y contactos
        Usuario usuarioActual = new Usuario("Admin", new ImageIcon(), "pass", "1000", "Hola", LocalDate.of(1990, 1, 1));
        Usuario user1 = new Usuario("Ana", new ImageIcon(), "pass1", "2000", "Hey", LocalDate.of(1992, 2, 2));
        Usuario user2 = new Usuario("Leo", new ImageIcon(), "pass2", "3000", "Hi", LocalDate.of(1993, 3, 3));

        ContactoIndividual contacto1 = new ContactoIndividual("Ana", user1, "2000");
        ContactoIndividual contacto2 = new ContactoIndividual("Leo", user2, "3000");

        Grupo grupo = new Grupo("Amigos");
        grupo.agregarIntegrante(contacto1);
        grupo.agregarIntegrante(contacto2);

        usuarioActual.añadirContacto(grupo);
        usuarioActual.añadirContacto(contacto1);
        usuarioActual.añadirContacto(contacto2);

        // Comportamiento esperado de mocks
        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1000")).thenReturn(usuarioActual);
        when(mockRepo.getUsuario("1000")).thenReturn(usuarioActual);
        when(mockRepo.getUsuarios()).thenReturn(Arrays.asList(usuarioActual, user1, user2));
        when(mockRepo.buscarUsuario("2000")).thenReturn(Optional.of(user1));
        when(mockRepo.buscarUsuario("3000")).thenReturn(Optional.of(user2));

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

            // Reiniciar singleton del controlador
            Field instance = Controlador.class.getDeclaredField("unicaInstancia");
            instance.setAccessible(true);
            instance.set(null, null);

            // Obtener nuevo controlador con mocks activos
            Controlador controlador = Controlador.getInstancia();
            controlador.hacerLogin("1000", "pass");

            // Acción: enviar mensaje al grupo
            controlador.enviarMensaje("¡Hola a todos!", grupo);

            // Verificación
            verify(mockMensajeDAO, times(3)).registrarMensaje(any(Mensaje.class)); // 2 individuales + 1 al grupo
            verify(mockContactoDAO).modificarContacto(contacto1);
            verify(mockContactoDAO).modificarContacto(contacto2);
            verify(mockGrupoDAO).modificarGrupo(grupo);
        }
    }
}