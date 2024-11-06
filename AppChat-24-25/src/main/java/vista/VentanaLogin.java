package vista;

import controlador.ControladorAppChat;
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

import controlador.ControladorAppChat;

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
	private JTextField textFieldTelefono;
	private JPasswordField passwordFieldContraseña;

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
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/angel_3434431 (2).png")));
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        frame.dispose(); // Cierra la ventana actual
		        VentanaRegistro ventanaRegistro = new VentanaRegistro(); // Crea una nueva instancia de VentanaRegistro
		        ventanaRegistro.setVisible(true); // Muestra la ventana de registro
			}
		});
		panelBot.add(btnRegistrar);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Recuperar datos 
				String telefono = textFieldTelefono.getText();
				@SuppressWarnings("deprecation")
				String password = passwordFieldContraseña.getText();
				
				// Ejecutar negocio por controlador 
				ControladorAppChat controlador = new ControladorAppChat(null);
				boolean login = controlador.hacerLogin(telefono, password);
				
				if (login) {
					VentanaMain ventanaMain = new VentanaMain();
					ventanaMain.frame.setVisible(true);
					VentanaLogin.this.frame.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(frame, "Login incorrecto", "Login", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnLogin.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/thinking_3434449.png")));
		panelBot.add(btnLogin);
		
		JPanel panelTop = new JPanel();
		frame.getContentPane().add(panelTop, BorderLayout.NORTH);
		
		JLabel lblFotoLogin = new JLabel("");
		lblFotoLogin.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/scared_3434441.png")));
		panelTop.add(lblFotoLogin);
		
		JPanel panelMid = new JPanel();
		frame.getContentPane().add(panelMid, BorderLayout.CENTER);
		GridBagLayout gbl_panelMid = new GridBagLayout();
		gbl_panelMid.columnWidths = new int[]{30, 90, 275, 50, 0};
		gbl_panelMid.rowHeights = new int[]{10, 0, 2, 0, 10, 0};
		gbl_panelMid.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelMid.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelMid.setLayout(gbl_panelMid);
		
		JLabel lblTelefono = new JLabel("Teléfono:  ");
		
		GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
		gbc_lblTelefono.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lblTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelefono.gridx = 1;
		gbc_lblTelefono.gridy = 1;
		panelMid.add(lblTelefono, gbc_lblTelefono);
		
		textFieldTelefono = new JTextField();
		GridBagConstraints gbc_textFieldTelefono = new GridBagConstraints();
		gbc_textFieldTelefono.anchor = GridBagConstraints.SOUTH;
		gbc_textFieldTelefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTelefono.gridx = 2;
		gbc_textFieldTelefono.gridy = 1;
		panelMid.add(textFieldTelefono, gbc_textFieldTelefono);
		
		JLabel lblContraseña = new JLabel("Contraseña:  ");
		GridBagConstraints gbc_lblContraseña = new GridBagConstraints();
		gbc_lblContraseña.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_lblContraseña.gridx = 1;
		gbc_lblContraseña.gridy = 3;
		panelMid.add(lblContraseña, gbc_lblContraseña);
		
		passwordFieldContraseña = new JPasswordField();
		passwordFieldContraseña.setMinimumSize(new Dimension(15, 20));
		GridBagConstraints gbc_passwordFieldContraseña = new GridBagConstraints();
		gbc_passwordFieldContraseña.anchor = GridBagConstraints.NORTH;
		gbc_passwordFieldContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldContraseña.gridx = 2;
		gbc_passwordFieldContraseña.gridy = 3;
		panelMid.add(passwordFieldContraseña, gbc_passwordFieldContraseña);
		
		
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
