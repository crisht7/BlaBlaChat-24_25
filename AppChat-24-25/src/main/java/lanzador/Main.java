package lanzador;

import java.awt.EventQueue;

import vista.VentanaLogin;

public class Main {
	/**
	 * Launch the application.
	 */
	//
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //Clase anónima que implementa la interfaz Runnable privada para ejecutar el código en un hilo separado
			public void run() { 
				try {
					VentanaLogin window = new VentanaLogin(); //Constructor de la clase actual
					window.frame.setVisible(true); //Hace visible 
					
					// VER VENTANAS DIALOGO PAGINA 73
					// int JOptionPane.showMessageDialog(window, frame, "Login", JOptionPane.PLAIN_MESSAGE);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
