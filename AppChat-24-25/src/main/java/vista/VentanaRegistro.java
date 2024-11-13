package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import com.toedter.calendar.JDateChooser;
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

	
	/**
	 * Create the frame.
	 */
	public VentanaRegistro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 791, 497);
		contentPane = new JPanel();
		contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{20, 0, 0, 0, 0, 20, 0};
		gbl_contentPane.rowHeights = new int[]{20, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 0, 20, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNombre = new JLabel("Nombre: ");
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.WEST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 1;
		gbc_lblNombre.gridy = 1;
		contentPane.add(lblNombre, gbc_lblNombre);
		
		textFieldNombre = new JTextField();
		GridBagConstraints gbc_textFieldNombre = new GridBagConstraints();
		gbc_textFieldNombre.gridwidth = 3;
		gbc_textFieldNombre.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNombre.gridx = 2;
		gbc_textFieldNombre.gridy = 1;
		contentPane.add(textFieldNombre, gbc_textFieldNombre);
		textFieldNombre.setColumns(10);
		
		JLabel lblApellidos = new JLabel("Apellidos:");
		GridBagConstraints gbc_lblApellidos = new GridBagConstraints();
		gbc_lblApellidos.anchor = GridBagConstraints.WEST;
		gbc_lblApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_lblApellidos.gridx = 1;
		gbc_lblApellidos.gridy = 3;
		contentPane.add(lblApellidos, gbc_lblApellidos);
		
		textFieldApellidos = new JTextField();
		GridBagConstraints gbc_textFieldApellidos = new GridBagConstraints();
		gbc_textFieldApellidos.gridwidth = 3;
		gbc_textFieldApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldApellidos.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldApellidos.gridx = 2;
		gbc_textFieldApellidos.gridy = 3;
		contentPane.add(textFieldApellidos, gbc_textFieldApellidos);
		textFieldApellidos.setColumns(10);
		
		JLabel lblNewLabelTelefono = new JLabel("Teléfono:");
		GridBagConstraints gbc_lblNewLabelTelefono = new GridBagConstraints();
		gbc_lblNewLabelTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelTelefono.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabelTelefono.gridx = 1;
		gbc_lblNewLabelTelefono.gridy = 5;
		contentPane.add(lblNewLabelTelefono, gbc_lblNewLabelTelefono);
		
		textFieldTelefono = new JTextField();
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
		gbc_lblNewLabelContraseña.gridy = 7;
		contentPane.add(lblNewLabelContraseña, gbc_lblNewLabelContraseña);
		
		passwordFieldContraseña = new JPasswordField();
		passwordFieldContraseña.setColumns(40);
		GridBagConstraints gbc_passwordFieldContraseña = new GridBagConstraints();
		gbc_passwordFieldContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldContraseña.gridx = 2;
		gbc_passwordFieldContraseña.gridy = 7;
		contentPane.add(passwordFieldContraseña, gbc_passwordFieldContraseña);
		
		JLabel lblNewLabelConfirmarContraseña = new JLabel("Repetir Contraseña:");
		GridBagConstraints gbc_lblNewLabelConfirmarContraseña = new GridBagConstraints();
		gbc_lblNewLabelConfirmarContraseña.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabelConfirmarContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelConfirmarContraseña.gridx = 3;
		gbc_lblNewLabelConfirmarContraseña.gridy = 7;
		contentPane.add(lblNewLabelConfirmarContraseña, gbc_lblNewLabelConfirmarContraseña);
		
		passwordFieldRepContraseña = new JPasswordField();
		passwordFieldRepContraseña.setColumns(40);
		GridBagConstraints gbc_passwordFieldRepContraseña = new GridBagConstraints();
		gbc_passwordFieldRepContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldRepContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldRepContraseña.gridx = 4;
		gbc_passwordFieldRepContraseña.gridy = 7;
		contentPane.add(passwordFieldRepContraseña, gbc_passwordFieldRepContraseña);
		
		JLabel lblNewLabelFechaNacimiento = new JLabel("Fecha Nacimiento:");
		GridBagConstraints gbc_lblNewLabelFechaNacimiento = new GridBagConstraints();
		gbc_lblNewLabelFechaNacimiento.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabelFechaNacimiento.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelFechaNacimiento.gridx = 1;
		gbc_lblNewLabelFechaNacimiento.gridy = 9;
		contentPane.add(lblNewLabelFechaNacimiento, gbc_lblNewLabelFechaNacimiento);
		
		JDateChooser dateChooserFechaNac = new JDateChooser();
		GridBagConstraints gbc_dateChooserFechaNac = new GridBagConstraints();
		gbc_dateChooserFechaNac.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooserFechaNac.fill = GridBagConstraints.BOTH;
		gbc_dateChooserFechaNac.gridx = 2;
		gbc_dateChooserFechaNac.gridy = 9;
		contentPane.add(dateChooserFechaNac, gbc_dateChooserFechaNac);
		
		JLabel lblNewLabelFoto = new JLabel("Imagen:");
		GridBagConstraints gbc_lblNewLabelFoto = new GridBagConstraints();
		gbc_lblNewLabelFoto.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabelFoto.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelFoto.gridx = 3;
		gbc_lblNewLabelFoto.gridy = 9;
		contentPane.add(lblNewLabelFoto, gbc_lblNewLabelFoto);
		
		textFieldURL = new JTextField();
		GridBagConstraints gbc_textFieldURL = new GridBagConstraints();
		gbc_textFieldURL.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldURL.gridx = 4;
		gbc_textFieldURL.gridy = 9;
		contentPane.add(textFieldURL, gbc_textFieldURL);
		textFieldURL.setColumns(10);
		
		JLabel lblNewLabelSalidaURL = new JLabel("");
		lblNewLabelSalidaURL.setIcon(new ImageIcon(VentanaRegistro.class.getResource("/imagenes/usuario.png")));
		GridBagConstraints gbc_lblNewLabelSalidaURL = new GridBagConstraints();
		gbc_lblNewLabelSalidaURL.gridheight = 3;
		gbc_lblNewLabelSalidaURL.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelSalidaURL.gridx = 4;
		gbc_lblNewLabelSalidaURL.gridy = 10;
		contentPane.add(lblNewLabelSalidaURL, gbc_lblNewLabelSalidaURL);
		
		JLabel lblNewLabelSaludo = new JLabel("Saludo:");
		GridBagConstraints gbc_lblNewLabelSaludo = new GridBagConstraints();
		gbc_lblNewLabelSaludo.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNewLabelSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelSaludo.gridx = 1;
		gbc_lblNewLabelSaludo.gridy = 11;
		contentPane.add(lblNewLabelSaludo, gbc_lblNewLabelSaludo);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 11;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		textFieldSaludo = new JTextField();
		textFieldSaludo.setBorder(null);
		scrollPane.setViewportView(textFieldSaludo);
		textFieldSaludo.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setBorder(null);
		scrollPane.setViewportView(textField_3);
		textField_3.setColumns(10);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.SOUTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 12;
		contentPane.add(panel, gbc_panel);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
			}
		});
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(btnRegistrar);
		panel.add(btnRegistrar);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);
		
	
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); // Cierra la ventana actual (VentanaRegistro)
		        VentanaLogin ventanaLogin = new VentanaLogin(); // Crea una nueva instancia de VentanaLogin
		        ventanaLogin.frame.setVisible(true); // Muestra la ventana de login
			}
		});
		panel.add(btnCancelar);
		panel.add(btnCancelar);

		
	}

}
