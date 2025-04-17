package descuentos;

import appChat.Usuario;

/**
 * Esta interfaz define un método para calcular el porcentaje de descuento
 * aplicable a un usuario. Las clases que implementen esta interfaz deben
 * proporcionar su propia lógica para determinar el porcentaje de descuento
 * basado en la información del usuario.
 */
public interface Descuento {
    double obtenerPorcentajeDescuento(Usuario usuario);
}
