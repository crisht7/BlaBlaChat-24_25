package descuentos;

import appChat.Usuario;

/**
 * Clase que implementa una estrategia de descuento basada en la cantidad de
 * mensajes enviados por un usuario. Si el usuario ha enviado un número mínimo
 * de mensajes, se aplica un porcentaje de descuento fijo.
 */
public class DescuentoMensaje implements Descuento {

    private static final double PORCENTAJE_DESCUENTO = 30.0;
    private int minimoMensajes;

    /**
     * Constructor que define el número mínimo de mensajes requeridos.
     *
     * @param minimoMensajes cantidad mínima de mensajes para aplicar el descuento
     */
    public DescuentoMensaje(int minimoMensajes) {
        this.minimoMensajes = minimoMensajes;
    }

    /**
     * Devuelve el porcentaje de descuento aplicable según los mensajes enviados.
     *
     * @param usuario Usuario al que se le evaluará el descuento
     * @return porcentaje de descuento aplicable 
     */
    @Override
    public double obtenerPorcentajeDescuento(Usuario usuario) {
        int totalMensajes = usuario.totalMensajesEnviados();

        if (totalMensajes >= minimoMensajes) {
            return PORCENTAJE_DESCUENTO;
        }

        return 0.0;
    }
}
