package vista;

import java.awt.Color;

/**
 * Enumeración que define los colores utilizados en la aplicación.
 * 
 * @author Grupo 4
 */
public enum Colores {
	/**
	 * Color naranja oscuro
	 */
    NARANJA_OSCURO(new Color(236, 163, 96)),
    /**
     * Color naranja claro
     */
    NARANJA_CLARO(new Color(245, 210, 158)),
	/**
	 * Color naranja botón
	 */
    NARANJA_BOTON (new Color(244, 97, 34)),
    /**
     * Color turquesa oscuro
     */
    TURQUESA_OSCURO(new Color(16, 154, 137)),
    /**
     * Color turquesa 
     */
    TURQUESA(new Color(159, 213, 192));

	/**
	 * Color asociado a la constante.
	 */
    private final Color color;
    
    /**
     * Constructor de la enumeración Colores.
     * @param color Color asociado a la constante.
     */
    Colores(Color color) {
        this.color = color;
    }

	/**
	 * Devuelve el color asociado a la constante.
	 * 
	 * @return Color asociado a la constante.
	 */
    public Color getColor() {
        return color;
    }
}
