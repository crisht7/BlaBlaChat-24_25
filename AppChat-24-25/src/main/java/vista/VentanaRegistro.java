package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import com.toedter.calendar.JDateChooser;

import controlador.Controlador;

import javax.swing.JSplitPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Label;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import java.awt.ComponentOrientation;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZoneId;

import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class VentanaRegistro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JTextField textFieldApellidos;
	private JTextField textFieldTelefono;
	private JPasswordField passwordFieldContraseña;
	private JTextField textFieldSaludo;
	private JTextField textFieldURL;
	private JPasswordField passwordFieldRepContraseña;
	private JTextField textField_3;
	private JFrame frame;
	private JDateChooser dateChooserFechaNac;


	private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
	private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
	private final Color turquesa = Colores.TURQUESA.getColor();
	private final Color boton = Colores.NARANJA_BOTON.getColor();


	
	/**
	 * Create the frame.
	 */
	public VentanaRegistro() {
		setTitle("Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 901, 497);
		contentPane = new JPanel();
		contentPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		contentPane.setAlignmentX(Component.RIGHT_ALIGNMENT);
		contentPane.setBackground(naranjaClaro);
		contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{20, 0, 0, 0, 0, 20, 0};
		gbl_contentPane.rowHeights = new int[]{20, 0, 10, 0, 10, 0, 0, 10, 0, 10, 0, 10, 0, 0, 20, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNombre = new JLabel("Nombre: ");
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.WEST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 1;
		gbc_lblNombre.gridy = 1;
		contentPane.add(lblNombre, gbc_lblNombre);

		textFieldNombre = new JTextField();
		textFieldNombre.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textFieldNombre.setBackground(naranjaOscuro);
		GridBagConstraints gbc_textFieldNombre = new GridBagConstraints();
		gbc_textFieldNombre.gridwidth = 2;
		gbc_textFieldNombre.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNombre.gridx = 2;
		gbc_textFieldNombre.gridy = 1;
		contentPane.add(textFieldNombre, gbc_textFieldNombre);
		textFieldNombre.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridheight = 4;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 4;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		lblNewLabel.setAlignmentY(2.0f);
		lblNewLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBackground(naranjaClaro);
		lblNewLabel.setIcon(new ImageIcon(VentanaRegistro.class.getResource("/recursos/logo.PNG")));

		JLabel lblApellidos = new JLabel("Apellidos:");
		GridBagConstraints gbc_lblApellidos = new GridBagConstraints();
		gbc_lblApellidos.anchor = GridBagConstraints.WEST;
		gbc_lblApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_lblApellidos.gridx = 1;
		gbc_lblApellidos.gridy = 3;
		contentPane.add(lblApellidos, gbc_lblApellidos);

		textFieldApellidos = new JTextField();
		textFieldApellidos.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textFieldApellidos.setBackground(naranjaOscuro);
		GridBagConstraints gbc_textFieldApellidos = new GridBagConstraints();
		gbc_textFieldApellidos.gridwidth = 2;
		gbc_textFieldApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldApellidos.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldApellidos.gridx = 2;
		gbc_textFieldApellidos.gridy = 3;
		contentPane.add(textFieldApellidos, gbc_textFieldApellidos);
		textFieldApellidos.setColumns(10);
		
		// Crear JLabel para el logo
		JLabel lblLogo = new JLabel(new ImageIcon("ruta/del/logo.png"));

		// Ajustar GridBagConstraints para el logo
		GridBagConstraints gbcLogo = new GridBagConstraints();
		gbcLogo.gridx = 2; // Posición en la tercera columna
		gbcLogo.gridy = 0; // Empieza en la primera fila
		gbcLogo.gridwidth = 1; // No ocupa varias columnas
		gbcLogo.gridheight = 3; // Ocupará varias filas (ajusta según necesidad)
		gbcLogo.anchor = GridBagConstraints.NORTHEAST; // Anclar arriba a la derecha
		gbcLogo.weighty = 0; // No expande verticalmente
		gbcLogo.insets = new Insets(10, 10, 10, 10); // Margen

		contentPane.add(lblLogo, gbcLogo);


		JLabel lblNewLabelTelefono = new JLabel("Teléfono:");
		GridBagConstraints gbc_lblNewLabelTelefono = new GridBagConstraints();
		gbc_lblNewLabelTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelTelefono.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabelTelefono.gridx = 1;
		gbc_lblNewLabelTelefono.gridy = 5;
		contentPane.add(lblNewLabelTelefono, gbc_lblNewLabelTelefono);

		textFieldTelefono = new JTextField();
		textFieldTelefono.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textFieldTelefono.setBackground(naranjaOscuro);
		GridBagConstraints gbc_textFieldTelefono = new GridBagConstraints();
		gbc_textFieldTelefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTelefono.gridx = 2;
		gbc_textFieldTelefono.gridy = 5;
		contentPane.add(textFieldTelefono, gbc_textFieldTelefono);
		textFieldTelefono.setColumns(10);

		JLabel lblNewLabelContraseña = new JLabel("Contraseña:");
		GridBagConstraints gbc_lblNewLabelContraseña = new GridBagConstraints();
		gbc_lblNewLabelContraseña.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabelContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelContraseña.gridx = 1;
		gbc_lblNewLabelContraseña.gridy = 8;
		contentPane.add(lblNewLabelContraseña, gbc_lblNewLabelContraseña);

		passwordFieldContraseña = new JPasswordField();
		passwordFieldContraseña.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		passwordFieldContraseña.setBackground(naranjaOscuro);
		passwordFieldContraseña.setColumns(40);
		GridBagConstraints gbc_passwordFieldContraseña = new GridBagConstraints();
		gbc_passwordFieldContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldContraseña.gridx = 2;
		gbc_passwordFieldContraseña.gridy = 8;
		contentPane.add(passwordFieldContraseña, gbc_passwordFieldContraseña);

		JLabel lblNewLabelConfirmarContraseña = new JLabel("Repetir Contraseña:");
		GridBagConstraints gbc_lblNewLabelConfirmarContraseña = new GridBagConstraints();
		gbc_lblNewLabelConfirmarContraseña.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabelConfirmarContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelConfirmarContraseña.gridx = 3;
		gbc_lblNewLabelConfirmarContraseña.gridy = 8;
		contentPane.add(lblNewLabelConfirmarContraseña, gbc_lblNewLabelConfirmarContraseña);

		passwordFieldRepContraseña = new JPasswordField();
		passwordFieldRepContraseña.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		passwordFieldRepContraseña.setBackground(naranjaOscuro);
		passwordFieldRepContraseña.setColumns(40);
		GridBagConstraints gbc_passwordFieldRepContraseña = new GridBagConstraints();
		gbc_passwordFieldRepContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldRepContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldRepContraseña.gridx = 4;
		gbc_passwordFieldRepContraseña.gridy = 8;
		contentPane.add(passwordFieldRepContraseña, gbc_passwordFieldRepContraseña);

		JLabel lblNewLabelFechaNacimiento = new JLabel("Fecha Nacimiento:");
		GridBagConstraints gbc_lblNewLabelFechaNacimiento = new GridBagConstraints();
		gbc_lblNewLabelFechaNacimiento.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabelFechaNacimiento.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelFechaNacimiento.gridx = 1;
		gbc_lblNewLabelFechaNacimiento.gridy = 10;
		contentPane.add(lblNewLabelFechaNacimiento, gbc_lblNewLabelFechaNacimiento);

		dateChooserFechaNac = new JDateChooser();
		dateChooserFechaNac.setFocusTraversalPolicyProvider(true);
		dateChooserFechaNac.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		dateChooserFechaNac.setBackground(naranjaOscuro);
		
		
		GridBagConstraints gbc_dateChooserFechaNac = new GridBagConstraints();
		gbc_dateChooserFechaNac.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooserFechaNac.fill = GridBagConstraints.BOTH;
		gbc_dateChooserFechaNac.gridx = 2;
		gbc_dateChooserFechaNac.gridy = 10;
		contentPane.add(dateChooserFechaNac, gbc_dateChooserFechaNac);

		JLabel lblNewLabelFoto = new JLabel("URL imagen:");
		lblNewLabelFoto.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblNewLabelFoto = new GridBagConstraints();
		gbc_lblNewLabelFoto.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabelFoto.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelFoto.gridx = 3;
		gbc_lblNewLabelFoto.gridy = 10;
		contentPane.add(lblNewLabelFoto, gbc_lblNewLabelFoto);

		textFieldURL = new JTextField();
		textFieldURL.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textFieldURL.setBackground(naranjaOscuro);
		GridBagConstraints gbc_textFieldURL = new GridBagConstraints();
		gbc_textFieldURL.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldURL.gridx = 4;
		gbc_textFieldURL.gridy = 10;
		contentPane.add(textFieldURL, gbc_textFieldURL);
		textFieldURL.setColumns(10);

		JLabel lblNewLabelSalidaURL = new JLabel("");
		lblNewLabelSalidaURL.setIcon(new ImageIcon(VentanaRegistro.class.getResource("/recursos/account.png")));
		GridBagConstraints gbc_lblNewLabelSalidaURL = new GridBagConstraints();
		gbc_lblNewLabelSalidaURL.gridheight = 3;
		gbc_lblNewLabelSalidaURL.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelSalidaURL.gridx = 4;
		gbc_lblNewLabelSalidaURL.gridy = 11;
		contentPane.add(lblNewLabelSalidaURL, gbc_lblNewLabelSalidaURL);

		JLabel lblNewLabelSaludo = new JLabel("Saludo:");
		GridBagConstraints gbc_lblNewLabelSaludo = new GridBagConstraints();
		gbc_lblNewLabelSaludo.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNewLabelSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelSaludo.gridx = 1;
		gbc_lblNewLabelSaludo.gridy = 12;
		contentPane.add(lblNewLabelSaludo, gbc_lblNewLabelSaludo);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 12;
		contentPane.add(scrollPane, gbc_scrollPane);

		textFieldSaludo = new JTextField();
		textFieldSaludo.setBorder(null);
		scrollPane.setViewportView(textFieldSaludo);
		textFieldSaludo.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setBackground(naranjaOscuro);
		textField_3.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setViewportView(textField_3);
		textField_3.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBackground(naranjaClaro);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 13;
		contentPane.add(panel, gbc_panel);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setPreferredSize(new Dimension(87, 33));
		btnRegistrar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnRegistrar.setOpaque(true);
		btnRegistrar.setContentAreaFilled(true);
		btnRegistrar.setBackground(turquesa);
		btnRegistrar.addActionListener(new ActionListener() {
			/**
			 * Permite registrar
			 */
			public void actionPerformed(ActionEvent e) {
				if(!datosCorrectos()) {
					return;
				}
				boolean creado = Controlador.getInstancia().registrarUsuario(textFieldNombre.getText(), 
						dateChooserFechaNac.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 
						new ImageIcon(textFieldURL.getText()), textFieldTelefono.getText(), textFieldSaludo.getText(), 
						passwordFieldContraseña.getText());
				if (!creado) {
					JOptionPane.showMessageDialog(frame, "El usuario ya existe", "Crea una cuenta", JOptionPane.ERROR_MESSAGE);
				} else {
					//Registro exitoso
					JOptionPane.showMessageDialog(frame, "Usuario registrado correctamente", "Registro", JOptionPane.INFORMATION_MESSAGE);
					Toolkit.getDefaultToolkit().beep();
					VentanaRegistro.this.dispose();  // Cierra la ventana de registro correctamente
					VentanaLogin login = new VentanaLogin();
					login.frmLogin.setVisible(true);  // Asegura que la ventana login sea visible

				}
				
			}
		});
		panel.add(btnRegistrar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setPreferredSize(new Dimension(87, 33));
		btnCancelar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnCancelar.setBackground(boton);
		btnCancelar.setOpaque(true);
		btnCancelar.setContentAreaFilled(true);
		btnCancelar.addActionListener(e -> {
		    VentanaRegistro.this.dispose();  // Cierra la ventana de registro
		    VentanaLogin login = new VentanaLogin();
		    login.frmLogin.setVisible(true);  // Muestra la ventana de login
		});
		panel.add(btnCancelar);


	}
	private boolean datosCorrectos() {
		 String nombre = textFieldNombre.getText().trim();
		    String telefono = textFieldTelefono.getText().trim();
		    String contraseña = new String(passwordFieldContraseña.getPassword());
		    String repetirContraseña = new String(passwordFieldRepContraseña.getPassword());
		    

		    if (nombre.isEmpty()) {
		        JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
		    if (!telefono.matches("\\d{9}")) {
		        JOptionPane.showMessageDialog(this, "El teléfono debe contener 9 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
		    if (contraseña.length() < 6) {
		        JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
		    if (!contraseña.equals(repetirContraseña)) {
		        JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
		    if (dateChooserFechaNac == null) {
		        JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de nacimiento.", "Error", JOptionPane.ERROR_MESSAGE);
		        return false;
		    }

		    return true;  // Si pasa todas las validaciones
	}

}
