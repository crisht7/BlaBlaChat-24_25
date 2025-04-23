package testPDFs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import javax.swing.ImageIcon;

import appChat.*;
import controlador.Controlador;
import org.junit.Before;
import org.junit.Test;
import persistencia.ContactoIndividualDAO;
import persistencia.GrupoDAO;
import persistencia.UsuarioDAO;

public class TestGrupos {

    private Controlador controlador;
    private Usuario usuarioActual;
    private ContactoIndividual contacto1;
    private ContactoIndividual contacto2;
    private GrupoDAO mockGrupoDAO;
    private ContactoIndividualDAO mockContactoDAO;
    private UsuarioDAO mockUsuarioDAO;
    private RepositorioUsuarios mockRepo;

    @Before
    public void setUp() throws Exception {
        usuarioActual = new Usuario("Ana", new ImageIcon(), "pass", "1000", "Hola", LocalDate.of(1990, 1, 1));
        contacto1 = new ContactoIndividual("Carlos", new Usuario("Carlos", null, "1234", "2000", "Hola", LocalDate.now()), "2000");
        contacto2 = new ContactoIndividual("Luis", new Usuario("Luis", null, "abcd", "3000", "Hey", LocalDate.now()), "3000");

        mockGrupoDAO = mock(GrupoDAO.class);
        mockContactoDAO = mock(ContactoIndividualDAO.class);
        mockUsuarioDAO = mock(UsuarioDAO.class);
        mockRepo = mock(RepositorioUsuarios.class);

        controlador = Controlador.getInstancia();

        java.lang.reflect.Field f1 = Controlador.class.getDeclaredField("adaptadorGrupo");
        java.lang.reflect.Field f2 = Controlador.class.getDeclaredField("adaptadorContactoIndividual");
        java.lang.reflect.Field f3 = Controlador.class.getDeclaredField("adaptadorUsuario");
        java.lang.reflect.Field f4 = Controlador.class.getDeclaredField("repoUsuarios");

        f1.setAccessible(true); f2.setAccessible(true); f3.setAccessible(true); f4.setAccessible(true);
        f1.set(controlador, mockGrupoDAO);
        f2.set(controlador, mockContactoDAO);
        f3.set(controlador, mockUsuarioDAO);
        f4.set(controlador, mockRepo);

        when(mockUsuarioDAO.recuperarUsuarioPorTelefono("1000")).thenReturn(usuarioActual);
        controlador.hacerLogin("1000", "pass");

        usuarioActual.añadirContacto(contacto1);
        usuarioActual.añadirContacto(contacto2);
    }

    @Test
    public void testCrearGrupoConContactos() {
        List<ContactoIndividual> integrantes = Arrays.asList(contacto1, contacto2);
        controlador.crearGrupo("GrupoTest", integrantes);

        Grupo grupo = usuarioActual.getGrupos().stream()
                .filter(g -> g.getNombre().equals("GrupoTest"))
                .findFirst().orElse(null);

        assertNotNull(grupo);
        assertEquals(2, grupo.getIntegrantes().size());
        verify(mockGrupoDAO).registrarGrupo(grupo);
    }

    @Test
    public void testAñadirContactoAGrupoNuevo() {
        Grupo grupo = controlador.añadirGrupo("DevTeam");
        assertNotNull(grupo);

        boolean añadido = controlador.añadirContactoAGrupo("DevTeam", contacto1);
        assertTrue(añadido);

        assertTrue(grupo.getIntegrantes().contains(contacto1));
        verify(mockGrupoDAO).modificarGrupo(grupo);
    }

    @Test
    public void testNoAñadirContactoRepetidoAGrupo() {
        Grupo grupo = controlador.añadirGrupo("DevTeam");
        grupo.agregarIntegrante(contacto1);

        boolean añadido = controlador.añadirContactoAGrupo("DevTeam", contacto1);
        assertFalse(añadido); // ya estaba
        verify(mockGrupoDAO, never()).modificarGrupo(grupo);
    }

    @Test
    public void testNoAñadirSiGrupoNoExiste() {
        boolean resultado = controlador.añadirContactoAGrupo("NoExiste", contacto1);
        assertFalse(resultado);
    }
}
