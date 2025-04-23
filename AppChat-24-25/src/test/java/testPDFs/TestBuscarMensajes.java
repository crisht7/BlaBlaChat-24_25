package testPDFs;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import javax.swing.ImageIcon;

import appChat.*;
import filtros.*;
import org.junit.Before;
import org.junit.Test;

public class TestBuscarMensajes {

    private List<Mensaje> mensajes;
    private Usuario ana, carlos, luis;
    private ContactoIndividual contactoCarlos;
    private Grupo grupo;

    @Before
    public void setUp() {
        ana = new Usuario("Ana", new ImageIcon(), "pass", "1000", "Hola", LocalDate.of(1990, 1, 1));
        carlos = new Usuario("Carlos", new ImageIcon(), "1234", "2000", "Hey!", LocalDate.of(1992, 2, 2));
        luis = new Usuario("Luis", new ImageIcon(), "abcd", "3000", "Hey!", LocalDate.of(1993, 3, 3));

        contactoCarlos = new ContactoIndividual("Carlos", carlos, "2000");

        grupo = new Grupo("GrupoTest");
        grupo.agregarIntegrante(new ContactoIndividual("Luis", luis, "3000"));

        mensajes = Arrays.asList(
            new Mensaje("Hola Carlos", LocalDateTime.now().minusMinutes(10), ana, contactoCarlos),
            new Mensaje("¿Estás?", LocalDateTime.now().minusMinutes(9), carlos, contactoCarlos),
            new Mensaje("Reunión a las 10", LocalDateTime.now().minusMinutes(8), carlos, grupo),
            new Mensaje("Listo para la reunión", LocalDateTime.now().minusMinutes(7), luis, grupo),
            new Mensaje("Perfecto", LocalDateTime.now().minusMinutes(6), ana, contactoCarlos)
        );
    }

    @Test
    public void testFiltroPorTexto() {
        FiltroBusqueda filtro = new FiltroPorTexto("reunión");
        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertEquals(2, resultado.size());
        assertTrue(resultado.get(0).getTexto().toLowerCase().contains("reunión"));
    }

    @Test
    public void testFiltroPorNombre() {
        FiltroBusqueda filtro = new FiltroPorNombre("Carlos");
        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertTrue(resultado.size() >= 1);
        assertTrue(resultado.stream().anyMatch(m -> m.getEmisor().getNombre().equals("Carlos")
            || m.getReceptor().getNombre().equals("Carlos")));
    }

    @Test
    public void testFiltroPorTelefono() {
        FiltroBusqueda filtro = new FiltroPorTelefono("2000"); // Carlos
        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertTrue(resultado.size() >= 1);
        assertTrue(resultado.stream().allMatch(m ->
            m.getEmisor().getTelefono().equals("2000") ||
            (m.getReceptor() instanceof ContactoIndividual &&
             ((ContactoIndividual) m.getReceptor()).getTelefono().equals("2000"))
        ));
    }

    @Test
    public void testFiltroCombinadoTextoYNombre() {
        FiltroCombinado filtro = new FiltroCombinado();
        filtro.añadirFiltro(new FiltroPorTexto("Hola"));
        filtro.añadirFiltro(new FiltroPorNombre("Carlos"));

        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertEquals(1, resultado.size());
        assertEquals("Hola Carlos", resultado.get(0).getTexto());
    }

    @Test
    public void testFiltroVacioDevuelveTodos() {
        FiltroBusqueda filtro = new FiltroPorTexto("");
        List<Mensaje> resultado = filtro.filtrar(mensajes);

        assertEquals(mensajes.size(), resultado.size());
    }
}
