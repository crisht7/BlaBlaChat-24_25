package vista;


import java.awt.Color;

public enum Colores {
    NARANJA_OSCURO(new Color(236, 163, 96)),
    NARANJA_CLARO(new Color(245, 210, 158)),
    NARANJA_BOTON (new Color(244, 97, 34)),
    TURQUESA_OSCURO(new Color(16, 154, 137)),
    TURQUESA(new Color(159, 213, 192));

    private final Color color;

    Colores(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
