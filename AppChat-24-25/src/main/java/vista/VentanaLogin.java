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
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPasswordField;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaLogin {

	JFrame frame;
	private String UsuarioUrl;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
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
		frame.setBounds(100, 100, 720, 480); //Establece el tamaño de la ventana
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Establece la operación por defecto cuando se cierra la ventana
		
		JPanel panelBot = new JPanel();
		frame.getContentPane().add(panelBot, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Registrar");
		btnNewButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/angel_3434431 (2).png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        frame.dispose(); // Cierra la ventana actual
		        VentanaRegistro ventanaRegistro = new VentanaRegistro(); // Crea una nueva instancia de VentanaRegistro
		        ventanaRegistro.setVisible(true); // Muestra la ventana de registro
			}
		});
		panelBot.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Login");
		btnNewButton_1.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/thinking_3434449.png")));
		panelBot.add(btnNewButton_1);
		
		JPanel panelTop = new JPanel();
		frame.getContentPane().add(panelTop, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/scared_3434441.png")));
		panelTop.add(lblNewLabel);
		
		JPanel panelMid = new JPanel();
		frame.getContentPane().add(panelMid, BorderLayout.CENTER);
		GridBagLayout gbl_panelMid = new GridBagLayout();
		gbl_panelMid.columnWidths = new int[]{30, 90, 275, 50, 0};
		gbl_panelMid.rowHeights = new int[]{10, 0, 2, 0, 10, 0};
		gbl_panelMid.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelMid.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelMid.setLayout(gbl_panelMid);
		
		JLabel lblNewLabel_1 = new JLabel("Teléfono:  ");
		lblNewLabel_1.addKeyListener(new KeyAdapter() {
			//Al pulsar la tecla "enter" se salta al siguiente campo
			
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					passwordField.requestFocus();
				}
			}
		});
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		panelMid.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.SOUTH;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		panelMid.add(textField, gbc_textField);
		
		JLabel lblNewLabel_2 = new JLabel("Contraseña:  ");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 3;
		panelMid.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		passwordField = new JPasswordField();
		passwordField.setMinimumSize(new Dimension(15, 20));
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.anchor = GridBagConstraints.NORTH;
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 3;
		panelMid.add(passwordField, gbc_passwordField);
		
		
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

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
