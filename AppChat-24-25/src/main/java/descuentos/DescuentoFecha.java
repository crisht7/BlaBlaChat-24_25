package descuentos;

import appChat.Usuario;
import java.time.LocalDate;

/**
 * Clase que implementa una estrategia de descuento basada en un rango de fechas.
 * Si la fecha de registro de un usuario está dentro del rango especificado,
 * se aplica un porcentaje de descuento fijo.
 */
public class DescuentoFecha implements Descuento {

    private LocalDate inicio;
    private LocalDate fin;
    private static final double PORCENTAJE_DESCUENTO = 15.0;

    /**
     * Constructor que define el rango de fechas del descuento.
     *
     * @param inicio Fecha de inicio del rango
     * @param fin    Fecha de fin del rango
     */
    public DescuentoFecha(LocalDate inicio, LocalDate fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    /**
     * Devuelve el porcentaje de descuento aplicable según la fecha de registro.
     *
     * @param usuario Usuario al que se le evaluará el descuento
     * @return porcentaje de descuento aplicable 
     */
    @Override
    public double obtenerPorcentajeDescuento(Usuario usuario) {
        LocalDate fechaRegistro = usuario.getFechaRegistro();

        if (fechaRegistro != null &&
            (fechaRegistro.isEqual(inicio) || fechaRegistro.isAfter(inicio)) &&
            (fechaRegistro.isEqual(fin) || fechaRegistro.isBefore(fin))) {
            return PORCENTAJE_DESCUENTO;
        }

        return 0.0;
    }
}
