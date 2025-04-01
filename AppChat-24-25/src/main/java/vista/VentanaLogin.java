package vista;

import controlador.Controlador;
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

import controlador.Controlador;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import java.awt.Cursor;
import java.awt.ComponentOrientation;

public class VentanaLogin {
	/**
	 * Ventana de login
	 */
	public JFrame frmLogin;
	
	private JTextField textFieldTelefono;
	
	private JPasswordField passwordFieldContraseña;
	
	private Controlador controlador;
	
	
	private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
	private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
	private final Color turquesa = Colores.TURQUESA.getColor();
	private final Color boton = Colores.NARANJA_BOTON.getColor();

	/**
	 * Create the application.
	 */
	public VentanaLogin() {
		
		initialize(); 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 720, 480); //Establece el tamaño de la ventana
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelBot = new JPanel();
		panelBot.setBackground(new Color(236, 215, 176));
		frmLogin.getContentPane().add(panelBot, BorderLayout.SOUTH);
		panelBot.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(naranjaClaro);
		panelBot.add(panel_1, BorderLayout.NORTH);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		btnLogin.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.add(btnLogin);
		btnLogin.setOpaque(true);
		btnLogin.setBackground(turquesa);
		btnLogin.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
		btnLogin.setForeground(new Color(0, 0, 0));
		btnLogin.setPreferredSize(new Dimension(250, 40)); // 150px de ancho, 40px de alto

		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Recuperar datos ADAPTAR A BASE DE DATOS 
				
				String telefono = textFieldTelefono.getText();
				@SuppressWarnings("deprecation")
				String password = passwordFieldContraseña.getText(); 
				
				// Ejecutar negocio por controlador terminar 
				Controlador controlador = Controlador.getInstancia();
				boolean login = controlador.hacerLogin(telefono, password);
				
				
				if (login) {
					VentanaMain ventanaMain = new VentanaMain();
				    ventanaMain.actualizarListaContactos();
					ventanaMain.frame.setVisible(true);
					VentanaLogin.this.frmLogin.setVisible(false);
				} else {
					
					JPanel panel = new JPanel(new BorderLayout());
					JLabel mensaje = new JLabel("Login incorrecto", SwingConstants.CENTER);
					ImageIcon icono = new ImageIcon(VentanaLogin.class.getResource("/recursos/cancel.png"));
					panel.setBackground(naranjaClaro);
					panel.add(mensaje, BorderLayout.CENTER);
					panel.add(new JLabel(icono), BorderLayout.WEST);
					UIManager.put("Button.background", new Color(244, 97, 34)); 
					UIManager.put("Panel.background", naranjaClaro); 
					UIManager.put("OptionPane.background", naranjaClaro);
					JOptionPane.showMessageDialog(frmLogin, panel, "Login failed", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		
		btnLogin.setIcon(new ImageIcon(VentanaLogin.class.getResource("/recursos/login.png")));
		
		JPanel panel = new JPanel();
		panel.setBackground(naranjaClaro);
		panelBot.add(panel, BorderLayout.SOUTH);
		
		
		JLabel lblNewLabel_1 = new JLabel("No tienes cuenta?");
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Sitka Subheading", Font.BOLD, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		
		JButton btnRegistrar = new JButton("Regístrate");
		panel.add(btnRegistrar);
		btnRegistrar.setOpaque(true);
		btnRegistrar.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
		btnRegistrar.setBackground(boton);
		btnRegistrar.setIcon(new ImageIcon(VentanaLogin.class.getResource("/recursos/register.png")));
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        frmLogin.dispose(); // Cierra la ventana actual
		        VentanaRegistro ventanaRegistro = new VentanaRegistro(); // Crea una nueva instancia de VentanaRegistro
		        ventanaRegistro.setVisible(true); // Muestra la ventana de registro
			}
		});
		
		
		JPanel panelTop = new JPanel();
		panelTop.setBackground(naranjaClaro);
		frmLogin.getContentPane().add(panelTop, BorderLayout.NORTH);
		
		panelTop.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;  // Expande horizontalmente
		gbc.weighty = 1.0;  // Expande verticalmente
		gbc.anchor = GridBagConstraints.CENTER; // Centra los elementos
		gbc.insets = new Insets(10, 10, 10, 10); // Margen

		
		
		
		JLabel lblFotoLogin = new JLabel("");
		
		
		lblFotoLogin.setIcon(new ImageIcon(VentanaLogin.class.getResource("/recursos/chat150v2.PNG")));
		GridBagConstraints gbc_lblFotoLogin = new GridBagConstraints();
		gbc_lblFotoLogin.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblFotoLogin.insets = new Insets(0, 0, 0, 5);
		gbc_lblFotoLogin.gridx = 1;
		gbc_lblFotoLogin.gridy = 0;
		panelTop.add(lblFotoLogin, gbc_lblFotoLogin);
		
		JLabel lblNewLabel = new JLabel("BlaBlaChat");
		lblNewLabel.setFont(new Font("Constantia", Font.BOLD, 35));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 3;
		gbc_lblNewLabel.gridy = 0;
		panelTop.add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel panelMid = new JPanel();
		panelMid.setBackground(naranjaClaro);
		frmLogin.getContentPane().add(panelMid, BorderLayout.CENTER);
		GridBagLayout gbl_panelMid = new GridBagLayout();
		gbl_panelMid.columnWidths = new int[]{30, 90, 275, 50, 0};
		gbl_panelMid.rowHeights = new int[]{10, 0, 2, 0, 10, 0};
		gbl_panelMid.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelMid.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelMid.setLayout(gbl_panelMid);
		
		JLabel lblTelefono = new JLabel("Teléfono:  ");
		lblTelefono.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 11));
		
		GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
		gbc_lblTelefono.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lblTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelefono.gridx = 1;
		gbc_lblTelefono.gridy = 1;
		panelMid.add(lblTelefono, gbc_lblTelefono);
		
		textFieldTelefono = new JTextField();
		textFieldTelefono.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textFieldTelefono.setBackground(new Color(237, 165, 112));
		GridBagConstraints gbc_textFieldTelefono = new GridBagConstraints();
		gbc_textFieldTelefono.anchor = GridBagConstraints.SOUTH;
		gbc_textFieldTelefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTelefono.gridx = 2;
		gbc_textFieldTelefono.gridy = 1;
		panelMid.add(textFieldTelefono, gbc_textFieldTelefono);
		
		JLabel lblContraseña = new JLabel("Contraseña:  ");
		lblContraseña.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 11));
		GridBagConstraints gbc_lblContraseña = new GridBagConstraints();
		gbc_lblContraseña.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_lblContraseña.gridx = 1;
		gbc_lblContraseña.gridy = 3;
		panelMid.add(lblContraseña, gbc_lblContraseña);
		
		passwordFieldContraseña = new JPasswordField();
		passwordFieldContraseña.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		passwordFieldContraseña.setBackground(new Color(237, 165, 112));
		passwordFieldContraseña.setMinimumSize(new Dimension(15, 20));
		GridBagConstraints gbc_passwordFieldContraseña = new GridBagConstraints();
		gbc_passwordFieldContraseña.anchor = GridBagConstraints.NORTH;
		gbc_passwordFieldContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldContraseña.gridx = 2;
		gbc_passwordFieldContraseña.gridy = 3;
		panelMid.add(passwordFieldContraseña, gbc_passwordFieldContraseña);
		
		
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
