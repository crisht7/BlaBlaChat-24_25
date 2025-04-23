package testPDFs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import javax.swing.ImageIcon;

import appChat.*;
import testHU.ExportadorPDF;

import org.junit.Before;
import org.junit.Test;

public class TestExportarConversacion {

    private Usuario premium;
    private Usuario noPremium;
    private ContactoIndividual contacto;
    private List<Mensaje> mensajes;

    @Before
    public void setUp() {
        premium = new Usuario("Ana", new ImageIcon(), "pass", "1000", "Hola", LocalDate.of(1990, 1, 1));
        premium.setPremium(true);

        noPremium = new Usuario("Luis", new ImageIcon(), "1234", "2000", "Hola", LocalDate.of(1992, 2, 2));
        noPremium.setPremium(false);

        contacto = new ContactoIndividual("Luis", noPremium, "2000");

        mensajes = Arrays.asList(
            new Mensaje("Hola!", LocalDateTime.of(2024, 4, 1, 10, 0), premium, contacto),
            new Mensaje("¿Cómo estás?", LocalDateTime.of(2024, 4, 1, 10, 2), noPremium, contacto),
            new Mensaje("Bien, gracias", LocalDateTime.of(2024, 4, 1, 10, 4), premium, contacto)
        );
    }

    @Test
    public void testSoloUsuariosPremiumPuedenExportar() {
        assertTrue(premium.isPremium());
        assertFalse(noPremium.isPremium());
    }

    @Test
    public void testMensajesOrdenadosAntesDeExportar() {
        List<Mensaje> copia = new ArrayList<>(mensajes);
        copia.sort(Comparator.naturalOrder());

        assertEquals("Hola!", copia.get(0).getTexto());
        assertEquals("¿Cómo estás?", copia.get(1).getTexto());
        assertEquals("Bien, gracias", copia.get(2).getTexto());
    }

    @Test
    public void testExportarPDFSimulado() {
        File destino = new File("chat_prueba_exportado.pdf");

        ExportadorPDF exportador = mock(ExportadorPDF.class);
        when(exportador.exportar(premium, contacto, mensajes, destino.getAbsolutePath())).thenReturn(true);

        boolean resultado = exportador.exportar(premium, contacto, mensajes, destino.getAbsolutePath());

        assertTrue("El PDF debe generarse correctamente", resultado);
        verify(exportador).exportar(premium, contacto, mensajes, destino.getAbsolutePath());
    }
}
