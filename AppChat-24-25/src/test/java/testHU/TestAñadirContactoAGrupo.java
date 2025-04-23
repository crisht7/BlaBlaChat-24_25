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

public class TestAñadirContactoAGrupo {

    @Test
    public void testAñadirContactoNuevoAlGrupo() throws Exception {
        // Mocks
        GrupoDAO mockGrupoDAO = mock(GrupoDAO.class);
        MensajeDAO mockMensajeDAO = mock(MensajeDAO.class);
        UsuarioDAO mockUsuarioDAO = mock(UsuarioDAO.class);
        ContactoIndividualDAO mockContactoDAO = mock(ContactoIndividualDAO.class);
        RepositorioUsuarios mockRepo = mock(RepositorioUsuarios.class);

        // Usuario actual
        Usuario usuario = new Usuario("Admin", new ImageIcon(), "pass", "1000", "Hola", LocalDate.of(1990, 1, 1));

        // Contacto a agregar
        Usuario userContacto = new Usuario("Carlos", new ImageIcon(), "pass2", "2000", "Hey", LocalDate.of(1992, 2, 2));
        ContactoIndividual contacto = new ContactoIndividual("Carlos", userContacto, "2000");

        // Grupo sin integrantes
        Grupo grupo = new Grupo("GrupoTest");
        usuario.añadirContacto(grupo);

        // Simulación de entorno
        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1000")).thenReturn(usuario);
        when(mockRepo.getUsuario("1000")).thenReturn(usuario);
        when(mockRepo.getUsuarios()).thenReturn(List.of(usuario, userContacto));

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

            Controlador controlador = Controlador.getInstancia();
            controlador.hacerLogin("1000", "pass");

            // Acción: agregar contacto al grupo
            boolean añadido = controlador.añadirContactoAGrupo("GrupoTest", contacto);

            assertTrue(añadido);
            assertTrue(grupo.getIntegrantes().contains(contacto));
            verify(mockGrupoDAO).modificarGrupo(grupo);
        }
    }

    @Test
    public void testNoAgregarContactoRepetido() throws Exception {
        // Mocks
        GrupoDAO mockGrupoDAO = mock(GrupoDAO.class);
        MensajeDAO mockMensajeDAO = mock(MensajeDAO.class);
        UsuarioDAO mockUsuarioDAO = mock(UsuarioDAO.class);
        ContactoIndividualDAO mockContactoDAO = mock(ContactoIndividualDAO.class);
        RepositorioUsuarios mockRepo = mock(RepositorioUsuarios.class);

        // Usuario actual
        Usuario usuario = new Usuario("Admin", new ImageIcon(), "pass", "1000", "Hola", LocalDate.of(1990, 1, 1));

        // Contacto ya existente en el grupo
        Usuario userContacto = new Usuario("Carlos", new ImageIcon(), "pass2", "2000", "Hey", LocalDate.of(1992, 2, 2));
        ContactoIndividual contacto = new ContactoIndividual("Carlos", userContacto, "2000");

        Grupo grupo = new Grupo("GrupoTest");
        grupo.agregarIntegrante(contacto);
        usuario.añadirContacto(grupo);

        // Simulación de entorno
        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1000")).thenReturn(usuario);
        when(mockRepo.getUsuario("1000")).thenReturn(usuario);
        when(mockRepo.getUsuarios()).thenReturn(List.of(usuario, userContacto));

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
            controlador.hacerLogin("1000", "pass");

            // Intento de agregar contacto ya existente
            boolean añadido = controlador.añadirContactoAGrupo("GrupoTest", contacto);

            assertFalse(añadido);
            verify(mockGrupoDAO, never()).modificarGrupo(any());
        }
    }
}
