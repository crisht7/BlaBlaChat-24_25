package tds.swing.componentes;

public class Persona {
	private final String nombre;
	private final String apellidos;

	public Persona(String nombre, String apellidos) {
		this.nombre = nombre;
		this.apellidos = apellidos;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString() {
		return nombre + " " + apellidos;
	}
}
