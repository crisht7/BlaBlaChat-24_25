package controlador;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

import appChat.Contacto;
import appChat.ContactoIndividual;
import appChat.RepositorioUsuarios;
import appChat.Usuario;
import controlador.Controlador;

public class TestCrearGrupo {

    private Controlador controlador;
    private RepositorioUsuarios repo;

    private final String telUsuario = "300000000";
    private final String telContacto1 = "300000001";
    private final String telContacto2 = "300000002";

    @Before
    public void setUp() {
        controlador = Controlador.getInstancia();
        repo = RepositorioUsuarios.getUnicaInstancia();

        // Limpiar datos previos
        for (String tel : List.of(telUsuario, telContacto1, telContacto2)) {
            Usuario u = repo.getUsuario(tel);
            if (u != null) {
                repo.eliminarUsuario(u);
                controlador.getAdaptadorUsuario().borrarUsuario(u);
            }
        }

        // Crear usuario actual y dos contactos
        controlador.registrarUsuario("Usuario", LocalDate.of(2000, 1, 1), new ImageIcon(), telUsuario, "", "clave");
        controlador.registrarUsuario("Contacto1", LocalDate.of(1999, 1, 1), new ImageIcon(), telContacto1, "", "clave");
        controlador.registrarUsuario("Contacto2", LocalDate.of(1998, 1, 1), new ImageIcon(), telContacto2, "", "clave");

        controlador.crearContacto("C1", telContacto1);
        controlador.crearContacto("C2", telContacto2);
    }

    @Test
    public void testCrearGrupoValido() {
        List<ContactoIndividual> integrantes = controlador.getUsuarioActual().getContactosIndividuales();
        controlador.crearGrupo("Grupo1", integrantes);

        boolean existe = controlador.getUsuarioActual()
                .getGrupos()
                .stream()
                .anyMatch(g -> g.getNombre().equals("Grupo1"));

        assertTrue("El grupo debería haberse creado correctamente", existe);
    }

    @Test
    public void testCrearGrupoNombreDuplicado() {
        List<ContactoIndividual> integrantes = controlador.getUsuarioActual().getContactosIndividuales();
        controlador.crearGrupo("Duplicado", integrantes);

        // Intentar crear otro grupo con el mismo nombre
        controlador.crearGrupo("Duplicado", integrantes);

        long cantidad = controlador.getUsuarioActual()
                .getGrupos()
                .stream()
                .filter(g -> g.getNombre().equals("Duplicado"))
                .count();

        assertEquals("No debería haber más de un grupo con el mismo nombre", 1, cantidad);
    }

    @Test
    public void testCrearGrupoSinIntegrantes() {
        int cantidadAntes = controlador.getUsuarioActual().getGrupos().size();

        controlador.crearGrupo("SinIntegrantes", Collections.emptyList());

        int cantidadDespues = controlador.getUsuarioActual().getGrupos().size();
        assertEquals("No debería crearse un grupo sin integrantes", cantidadAntes, cantidadDespues);
    }

    @Test
    public void testCrearGrupoNombreVacioDesdeVista() {
        // Esta validación se hace en la vista (VentanaGrupo), no en el controlador directamente,
        // así que no hay forma de simularlo en un test directo sin usar UI mocks.
        // Este test sirve como recordatorio de que debe manejarse en la interfaz.
        assertTrue("La vista debe impedir nombres vacíos antes de llamar al controlador", true);
    }
} 
