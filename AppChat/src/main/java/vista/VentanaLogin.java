package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.SwingConstants;

public class VentanaLogin {

	private JFrame frame;
	private String UsuarioUrl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //Clase anónima que implementa la interfaz Runnable privada para ejecutar el código en un hilo separado
			public void run() { 
				try {
					VentanaLogin window = new VentanaLogin(); //Constructor de la clase actual
					window.frame.setVisible(true); //Hace visible 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VentanaLogin() {
		initialize(); //Inicializa la ventana
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 670, 449); //Establece el tamaño de la ventana
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Establece la operación por defecto cuando se cierra la ventana
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Registrar");
		btnNewButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/angel_3434431 (2).png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Login");
		btnNewButton_1.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/thinking_3434449.png")));
		panel.add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/scared_3434441.png")));
		panel_1.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		
		JLabel lblNewLabel_1 = new JLabel("Login");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		scrollPane.setColumnHeaderView(lblNewLabel_1);
		
		
		/*
		String path = UsuarioUrl;
		try {	
			url = new URL(UsuarioUrl)
			ImageIcon icon = new ImageIcon(path);
			JLabel label = new JLabel(icon);
			panel_1.add(label);
		}
			catch (Exception e) {
            e.printStackTrace();
        }
	
	*/
	}
}
