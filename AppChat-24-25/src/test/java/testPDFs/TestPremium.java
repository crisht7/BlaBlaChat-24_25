package testPDFs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import javax.swing.ImageIcon;

import appChat.Usuario;
import descuentos.DescuentoFecha;
import descuentos.DescuentoMensaje;
import org.junit.Before;
import org.junit.Test;

public class TestPremium {

    private Usuario usuario;
    private Usuario usuarioMensajero;

    @Before
    public void setUp() {
        usuario = new Usuario("Ana", new ImageIcon(), "pass", "1000", "Hola", LocalDate.of(2024, 1, 15));
        usuario.setPremium(false);

        usuarioMensajero = new Usuario("Carlos", new ImageIcon(), "1234", "2000", "Hey!", LocalDate.of(2023, 6, 1));
        usuarioMensajero.setPremium(false);
    }

    @Test
    public void testActivarPremiumDirectamente() {
        usuario.setPremium(true);
        assertTrue(usuario.isPremium());
    }

    @Test
    public void testDescuentoPorFecha() {
        DescuentoFecha descuento = new DescuentoFecha(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        double porcentaje = descuento.obtenerPorcentajeDescuento(usuario);
        assertTrue(porcentaje > 0);
    }

    @Test
    public void testDescuentoPorMensajes() {
        Usuario espia = spy(usuarioMensajero);
        when(espia.totalMensajesEnviados()).thenReturn(25);

        DescuentoMensaje descuento = new DescuentoMensaje(20);
        double porcentaje = descuento.obtenerPorcentajeDescuento(espia);

        assertEquals(30.0, porcentaje, 0.01);
    }

    @Test
    public void testAccesoAExportarPDF() {
        usuario.setPremium(true);

        boolean puedeExportar = usuario.isPremium();
        assertTrue(puedeExportar);
    }

    @Test
    public void testBloqueoExportarSiNoEsPremium() {
        usuario.setPremium(false);

        boolean puedeExportar = usuario.isPremium();
        assertFalse(puedeExportar);
    }
}
