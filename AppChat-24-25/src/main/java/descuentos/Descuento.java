package descuentos;

import appChat.Usuario;

/**
 * Esta interfaz define un método para calcular el porcentaje de descuento
 * aplicable a un usuario. Las clases que implementen esta interfaz deben
 * proporcionar su propia lógica para determinar el porcentaje de descuento
 * basado en la información del usuario.
 */
public interface Descuento {
	/**
	 * Devuelve el porcentaje de descuento aplicable a un usuario.
	 * 
	 * @param usuario Usuario al que se le evaluará el descuento
	 * @return porcentaje de descuento aplicable
	 */
    double obtenerPorcentajeDescuento(Usuario usuario);
}
