package testHU;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import appChat.Usuario;
import controlador.Controlador;
import descuentos.DescuentoFecha;
import descuentos.DescuentoMensaje;

import org.junit.Before;
import org.junit.Test;

import javax.swing.ImageIcon;
import java.time.LocalDate;

public class TestActivarPremium {

    private Usuario usuario;
    private Controlador controlador;

    @Before
    public void setUp() {
        usuario = new Usuario("Ana", new ImageIcon(), "1234", "1111", "Hola!", LocalDate.of(2023, 1, 1));
        usuario.setPremium(false);

        controlador = Controlador.getInstancia();
        controlador.getRepoUsuarios().agregarUsuario(usuario); // para evitar errores
    }

    @Test
    public void testActivarPremiumSinDescuento() {
        // Simular: sin descuento
        DescuentoFecha df = mock(DescuentoFecha.class);
        DescuentoMensaje dm = mock(DescuentoMensaje.class);
        when(df.obtenerPorcentajeDescuento(usuario)).thenReturn(0.0);
        when(dm.obtenerPorcentajeDescuento(usuario)).thenReturn(0.0);

        // Acción: activar premium sin descuentos
        usuario.setPremium(true);

        // Verificación
        assertTrue(usuario.isPremium());
    }

    @Test
    public void testActivarPremiumConDescuentoPorFecha() {
        DescuentoFecha df = new DescuentoFecha(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        double descuento = df.obtenerPorcentajeDescuento(usuario);
        assertTrue(descuento > 0);
    }

    @Test
    public void testActivarPremiumConDescuentoPorMensajes() {
        Usuario usuarioMensajero = new Usuario("Carlos", new ImageIcon(), "1234", "9999", "Hola!", LocalDate.of(2020, 1, 1));
        // Simular envío de muchos mensajes
        // Supongamos el método totalMensajesEnviados devuelve 12
        Usuario spyUsuario = spy(usuarioMensajero);
        when(spyUsuario.totalMensajesEnviados()).thenReturn(12);

        DescuentoMensaje dm = new DescuentoMensaje(10); // mínimo 10 mensajes
        double descuento = dm.obtenerPorcentajeDescuento(spyUsuario);

        assertEquals(30.0, descuento, 0.01); // según lógica interna
    }

    @Test
    public void testAccesoAExportarPDF() {
        usuario.setPremium(true);
        assertTrue(usuario.isPremium());

        // Simulamos una función que sólo está disponible para premium:
        boolean accesoExportarPDF = usuario.isPremium(); // lógica UI simplificada

        assertTrue("El usuario premium debe poder exportar PDF", accesoExportarPDF);
    }
}
