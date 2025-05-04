package lanzador;

import java.awt.EventQueue;

import vista.VentanaLogin;
/**
 * Clase principal para lanzar la aplicación.
 */
public class Main {
	/**
	 * Constructor de la clase Main por defecto.
	 */
	public Main() {
        // Constructor vacío
    }
	/**
	 * Lanza la aplicacion.
	 * 
	 * @param args argumentos de la línea de comandos
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //Clase anónima que implementa la interfaz Runnable privada para ejecutar el código en un hilo separado
			public void run() { 
				try {
					VentanaLogin.getInstancia().setVisible(true); //Llama a la ventana de login
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
