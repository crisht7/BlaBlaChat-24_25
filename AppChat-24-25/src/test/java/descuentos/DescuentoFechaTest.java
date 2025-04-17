package descuentos;

import appChat.Usuario;
import org.junit.Test;
import javax.swing.ImageIcon;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class DescuentoFechaTest {

    @Test
    public void testDescuentoAplicaDentroDelRango() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 12, 31);

        Usuario usuario = new Usuario("Test", new ImageIcon(), "1234", "123456789", "hola",
                LocalDate.of(2024, 5, 15));

        DescuentoFecha descuento = new DescuentoFecha(inicio, fin);
        double porcentaje = descuento.obtenerPorcentajeDescuento(usuario);

        assertEquals(15.0, porcentaje, 0.001);
    }

    @Test
    public void testDescuentoNoAplicaFueraDelRango() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 12, 31);

        Usuario usuario = new Usuario("Test", new ImageIcon(), "1234", "123456789", "hola",
                LocalDate.of(2023, 10, 15));

        DescuentoFecha descuento = new DescuentoFecha(inicio, fin);
        double porcentaje = descuento.obtenerPorcentajeDescuento(usuario);

        assertEquals(0.0, porcentaje, 0.001);
    }
}
