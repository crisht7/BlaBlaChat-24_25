package vista;

import controlador.Controlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class VentanaLogin {
	/**
	 * Ventana de login
	 */
	public JFrame frmLogin;
	private JTextField textFieldTelefono;
	private JPasswordField passwordFieldContraseña;

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
		frmLogin.setBounds(100, 100, 720, 480);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frmLogin.getContentPane().setLayout(new BorderLayout());

		crearPanelBotones();
		crearPanelSuperior();
		crearPanelCentral();
	}

	private void crearPanelBotones() {
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
		btnLogin.setOpaque(true);
		btnLogin.setBackground(turquesa);
		btnLogin.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
		btnLogin.setForeground(Color.BLACK);
		btnLogin.setPreferredSize(new Dimension(250, 40));
		btnLogin.setIcon(new ImageIcon(VentanaLogin.class.getResource("/recursos/login.png")));
		btnLogin.addActionListener(this::accionLogin);
		panel_1.add(btnLogin);

		JPanel panel = new JPanel();
		panel.setBackground(naranjaClaro);
		panelBot.add(panel, BorderLayout.SOUTH);

		JLabel lblNewLabel_1 = new JLabel("No tienes cuenta?");
		lblNewLabel_1.setFont(new Font("Sitka Subheading", Font.BOLD, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblNewLabel_1);

		JButton btnRegistrar = new JButton("Reg\u00EDstrate");
		btnRegistrar.setOpaque(true);
		btnRegistrar.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
		btnRegistrar.setBackground(boton);
		btnRegistrar.setIcon(new ImageIcon(VentanaLogin.class.getResource("/recursos/register.png")));
		btnRegistrar.addActionListener(e -> {
			frmLogin.dispose();
			VentanaRegistro ventanaRegistro = new VentanaRegistro();
			ventanaRegistro.setVisible(true);
		});
		panel.add(btnRegistrar);
	}

	private void crearPanelSuperior() {
		JPanel panelTop = new JPanel();
		panelTop.setBackground(naranjaClaro);
		frmLogin.getContentPane().add(panelTop, BorderLayout.NORTH);
		panelTop.setLayout(new GridBagLayout());

		GridBagConstraints gbc_lblFotoLogin = new GridBagConstraints();
		gbc_lblFotoLogin.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblFotoLogin.insets = new Insets(0, 0, 0, 5);
		gbc_lblFotoLogin.gridx = 1;
		gbc_lblFotoLogin.gridy = 0;

		JLabel lblFotoLogin = new JLabel("");
		lblFotoLogin.setIcon(new ImageIcon(VentanaLogin.class.getResource("/recursos/chat150v2.PNG")));
		panelTop.add(lblFotoLogin, gbc_lblFotoLogin);

		JLabel lblNewLabel = new JLabel("BlaBlaChat");
		lblNewLabel.setFont(new Font("Constantia", Font.BOLD, 35));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 3;
		gbc_lblNewLabel.gridy = 0;
		panelTop.add(lblNewLabel, gbc_lblNewLabel);
	}

	private void crearPanelCentral() {
		JPanel panelMid = new JPanel();
		panelMid.setBackground(naranjaClaro);
		panelMid.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Solo márgenes laterales
		frmLogin.getContentPane().add(panelMid, BorderLayout.CENTER);
		panelMid.setLayout(new GridBagLayout());

		GridBagConstraints gbc;

		JLabel lblTelefono = new JLabel("Tel\u00E9fono:  ");
		lblTelefono.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 11));
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 1;
		panelMid.add(lblTelefono, gbc);

		textFieldTelefono = new JTextField();
		textFieldTelefono.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		textFieldTelefono.setBackground(new Color(237, 165, 112));
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		panelMid.add(textFieldTelefono, gbc);

		JLabel lblContraseña = new JLabel("Contrase\u00F1a:  ");
		lblContraseña.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 11));
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 3;
		panelMid.add(lblContraseña, gbc);

		passwordFieldContraseña = new JPasswordField();
		passwordFieldContraseña.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		passwordFieldContraseña.setBackground(new Color(237, 165, 112));
		passwordFieldContraseña.setMinimumSize(new Dimension(15, 20));
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.weightx = 1.0;
		panelMid.add(passwordFieldContraseña, gbc);
	}

	private void accionLogin(ActionEvent e) {
		String telefono = textFieldTelefono.getText();
		@SuppressWarnings("deprecation")
		String password = passwordFieldContraseña.getText();

		boolean login = Controlador.getInstancia().hacerLogin(telefono, password);

		if (login) {
			VentanaMain ventanaMain = new VentanaMain();
			ventanaMain.actualizarListaContactos();
			ventanaMain.setVisible(true);
			frmLogin.setVisible(false);
		} else {
			mostrarDialogoError();
		}
	}

	private void mostrarDialogoError() {
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

	public void setVisible(boolean visible) {
		frmLogin.setVisible(visible);
	}
}
