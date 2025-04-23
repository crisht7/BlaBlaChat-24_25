package testHU;
import static org.junit.Assert.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import javax.swing.ImageIcon;

import appChat.*;
import filtros.*;
import org.junit.Before;
import org.junit.Test;

public class TestFiltroMensajes {

    private List<Mensaje> mensajes;
    private Usuario carlos, ana, diana;
    private ContactoIndividual contactoCarlos, contactoAna, contactoDiana;
    private Grupo grupo;

    @Before
    public void setUp() {
        // Usuarios
        carlos = new Usuario("Carlos", new ImageIcon(), "pass", "1111", "Hola", LocalDate.of(1990, 1, 1));
        ana = new Usuario("Ana", new ImageIcon(), "pass", "2222", "Hey", LocalDate.of(1991, 2, 2));
        diana = new Usuario("Diana", new ImageIcon(), "pass", "3333", "Hola", LocalDate.of(1992, 3, 3));

        contactoCarlos = new ContactoIndividual("Carlos", carlos, "1111");
        contactoAna = new ContactoIndividual("Ana", ana, "2222");
        contactoDiana = new ContactoIndividual("Diana", diana, "3333");

        grupo = new Grupo("Grupo1");
        grupo.agregarIntegrante(contactoCarlos);
        grupo.agregarIntegrante(contactoAna);

        // Mensajes simulados
        mensajes = Arrays.asList(
            new Mensaje("Hola", LocalDateTime.now(), carlos, contactoAna),
            new Mensaje("¿Todo bien?", LocalDateTime.now(), ana, contactoCarlos),
            new Mensaje("Reunión mañana", LocalDateTime.now(), carlos, grupo),
            new Mensaje("Hola Carlos", LocalDateTime.now(), diana, contactoCarlos),
            new Mensaje("Reunión confirmada", LocalDateTime.now(), carlos, grupo),
            new Mensaje("Hola grupo", LocalDateTime.now(), ana, grupo)
        );
    }

    @Test
    public void testFiltroPorTexto() {
        FiltroBusqueda filtro = new FiltroPorTexto("Reunión");
        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(m -> m.getTexto().contains("Reunión")));
    }

    @Test
    public void testFiltroPorTelefonoEmisor() {
        FiltroBusqueda filtro = new FiltroPorTelefono("1111"); // Carlos
        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertEquals(3, resultado.size());
        assertTrue(resultado.stream().allMatch(m -> m.getEmisor().getTelefono().equals("1111")));
    }

    @Test
    public void testFiltroPorTelefonoEnGrupo() {
        FiltroBusqueda filtro = new FiltroPorTelefono("2222"); // Ana está en grupo
        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertTrue(resultado.size() >= 1);
    }

    @Test
    public void testFiltroPorNombreEmisor() {
        FiltroBusqueda filtro = new FiltroPorNombre("Diana");
        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertEquals(1, resultado.size());
        assertEquals("Hola Carlos", resultado.get(0).getTexto());
    }

    @Test
    public void testFiltroPorNombreGrupo() {
        FiltroBusqueda filtro = new FiltroPorNombre("Carlos"); // Carlos está como emisor y también como integrante de grupo
        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertTrue(resultado.size() >= 1);
    }

    @Test
    public void testFiltroCombinadoTextoYNombre() {
        FiltroCombinado filtro = new FiltroCombinado();
        filtro.añadirFiltro(new FiltroPorTexto("Hola"));
        filtro.añadirFiltro(new FiltroPorNombre("Carlos"));

        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertEquals(2, resultado.size());
        assertEquals("Hola", resultado.get(0).getTexto());
    }

    @Test
    public void testFiltroCombinadoVacio() {
        FiltroCombinado filtro = new FiltroCombinado(); // sin filtros

        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertEquals(mensajes.size(), resultado.size());
    }
}
