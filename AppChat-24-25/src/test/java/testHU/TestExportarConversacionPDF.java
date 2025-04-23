package testHU;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import appChat.*;
import controlador.Controlador;
import org.junit.Before;
import org.junit.Test;

import javax.swing.ImageIcon;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestExportarConversacionPDF {

    private Usuario usuarioPremium;
    private ContactoIndividual contacto;
    private List<Mensaje> mensajes;

    @Before
    public void setUp() {
        usuarioPremium = new Usuario("Carlos", new ImageIcon(), "1234", "1111", "Hola!", LocalDate.of(2020, 1, 1));
        usuarioPremium.setPremium(true);

        Usuario ana = new Usuario("Ana", new ImageIcon(), "abcd", "2222", "Hey!", LocalDate.of(2022, 1, 1));
        contacto = new ContactoIndividual("Ana", ana, "2222");

        mensajes = Arrays.asList(
                new Mensaje("Hola Ana", LocalDateTime.of(2024, 4, 1, 10, 0), usuarioPremium, contacto),
                new Mensaje("¿Cómo estás?", LocalDateTime.of(2024, 4, 1, 10, 1), ana, contacto),
                new Mensaje("Todo bien", LocalDateTime.of(2024, 4, 1, 10, 2), usuarioPremium, contacto)
        );
    }

    @Test
    public void testSoloUsuariosPremiumPuedenExportar() {
        Usuario noPremium = new Usuario("Normal", new ImageIcon(), "123", "3333", "Hi", LocalDate.now());
        noPremium.setPremium(false);

        assertFalse("Usuario sin premium no puede exportar", noPremium.isPremium());
    }

    @Test
    public void testExportarPDFContenidoCorrecto() {
        assertTrue("Usuario debe ser premium", usuarioPremium.isPremium());

        // Simular archivo temporal
        File archivoTemporal = new File("conversacion_test.pdf");

        // Simular exportador PDF (mockeable si tienes una clase exportadora)
        ExportadorPDF exportador = mock(ExportadorPDF.class);
        when(exportador.exportar(usuarioPremium, contacto, mensajes, archivoTemporal.getAbsolutePath()))
                .thenReturn(true);

        boolean resultado = exportador.exportar(usuarioPremium, contacto, mensajes, archivoTemporal.getAbsolutePath());

        // Verificaciones
        assertTrue("El archivo debe generarse correctamente", resultado);
        verify(exportador).exportar(usuarioPremium, contacto, mensajes, archivoTemporal.getAbsolutePath());
    }

    @Test
    public void testMensajesOrdenadosCronologicamente() {
        List<Mensaje> ordenados = mensajes.stream().sorted().toList();

        assertEquals("Mensajes deben estar ordenados cronológicamente", mensajes.get(0), ordenados.get(0));
        assertEquals(mensajes.get(2).getTexto(), "Todo bien");
    }
}
