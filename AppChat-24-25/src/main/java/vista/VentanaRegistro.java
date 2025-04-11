package vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.toedter.calendar.JDateChooser;
import controlador.Controlador;

public class VentanaRegistro extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre, textFieldApellidos, textFieldTelefono, textFieldSaludo, textFieldURL;
	private JPasswordField passwordFieldContraseña, passwordFieldRepContraseña;
	private JDateChooser dateChooserFechaNac;
	private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
	private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
	private final Color turquesa = Colores.TURQUESA.getColor();
	private final Color boton = Colores.NARANJA_BOTON.getColor();
	private JLabel lblIcon;
	private BufferedImage imagenPerfil;  // Guardamos la imagen real aquí


	
	public VentanaRegistro() {
		setTitle("Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 901, 497);
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		contentPane = new JPanel();
		contentPane.setBackground(naranjaClaro);
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{20, 0, 0, 0, 0, 20, 0};
		gbl_contentPane.rowHeights = new int[]{20, 0, 10, 0, 10, 0, 0, 10, 0, 10, 0, 10, 0, 0, 20, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		// Nombre
		JLabel lblNombre = new JLabel("Nombre: ");
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.WEST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 1;
		gbc_lblNombre.gridy = 1;
		contentPane.add(lblNombre, gbc_lblNombre);

		textFieldNombre = new JTextField();
		textFieldNombre.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		textFieldNombre.setBackground(naranjaOscuro);
		textFieldNombre.setColumns(10);
		GridBagConstraints gbc_textFieldNombre = new GridBagConstraints();
		gbc_textFieldNombre.gridwidth = 2;
		gbc_textFieldNombre.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNombre.gridx = 2;
		gbc_textFieldNombre.gridy = 1;
		contentPane.add(textFieldNombre, gbc_textFieldNombre);

		JLabel lblLogo = new JLabel("");
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setBackground(naranjaClaro);
		lblLogo.setIcon(new ImageIcon(VentanaRegistro.class.getResource("/logo.PNG")));
		GridBagConstraints gbc_lblLogo = new GridBagConstraints();
		gbc_lblLogo.gridheight = 4;
		gbc_lblLogo.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogo.gridx = 4;
		gbc_lblLogo.gridy = 1;
		contentPane.add(lblLogo, gbc_lblLogo);

		// Apellidos
		JLabel lblApellidos = new JLabel("Apellidos:");
		GridBagConstraints gbc_lblApellidos = new GridBagConstraints();
		gbc_lblApellidos.anchor = GridBagConstraints.WEST;
		gbc_lblApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_lblApellidos.gridx = 1;
		gbc_lblApellidos.gridy = 3;
		contentPane.add(lblApellidos, gbc_lblApellidos);

		textFieldApellidos = new JTextField();
		textFieldApellidos.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		textFieldApellidos.setBackground(naranjaOscuro);
		textFieldApellidos.setColumns(10);
		GridBagConstraints gbc_textFieldApellidos = new GridBagConstraints();
		gbc_textFieldApellidos.gridwidth = 2;
		gbc_textFieldApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldApellidos.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldApellidos.gridx = 2;
		gbc_textFieldApellidos.gridy = 3;
		contentPane.add(textFieldApellidos, gbc_textFieldApellidos);

		// Teléfono
		JLabel lblTelefono = new JLabel("Teléfono:");
		GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
		gbc_lblTelefono.anchor = GridBagConstraints.WEST;
		gbc_lblTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelefono.gridx = 1;
		gbc_lblTelefono.gridy = 5;
		contentPane.add(lblTelefono, gbc_lblTelefono);

		textFieldTelefono = new JTextField();
		textFieldTelefono.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		textFieldTelefono.setBackground(naranjaOscuro);
		textFieldTelefono.setColumns(10);
		GridBagConstraints gbc_textFieldTelefono = new GridBagConstraints();
		gbc_textFieldTelefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTelefono.gridx = 2;
		gbc_textFieldTelefono.gridy = 5;
		contentPane.add(textFieldTelefono, gbc_textFieldTelefono);

		// Contraseñas
		JLabel lblContraseña = new JLabel("Contraseña:");
		GridBagConstraints gbc_lblContraseña = new GridBagConstraints();
		gbc_lblContraseña.anchor = GridBagConstraints.WEST;
		gbc_lblContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_lblContraseña.gridx = 1;
		gbc_lblContraseña.gridy = 8;
		contentPane.add(lblContraseña, gbc_lblContraseña);

		passwordFieldContraseña = new JPasswordField();
		passwordFieldContraseña.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		passwordFieldContraseña.setBackground(naranjaOscuro);
		GridBagConstraints gbc_passwordFieldContraseña = new GridBagConstraints();
		gbc_passwordFieldContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldContraseña.gridx = 2;
		gbc_passwordFieldContraseña.gridy = 8;
		contentPane.add(passwordFieldContraseña, gbc_passwordFieldContraseña);

		JLabel lblRepContraseña = new JLabel("Repetir Contraseña:");
		GridBagConstraints gbc_lblRepContraseña = new GridBagConstraints();
		gbc_lblRepContraseña.anchor = GridBagConstraints.EAST;
		gbc_lblRepContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_lblRepContraseña.gridx = 3;
		gbc_lblRepContraseña.gridy = 8;
		contentPane.add(lblRepContraseña, gbc_lblRepContraseña);

		passwordFieldRepContraseña = new JPasswordField();
		passwordFieldRepContraseña.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		passwordFieldRepContraseña.setBackground(naranjaOscuro);
		GridBagConstraints gbc_passwordFieldRepContraseña = new GridBagConstraints();
		gbc_passwordFieldRepContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldRepContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldRepContraseña.gridx = 4;
		gbc_passwordFieldRepContraseña.gridy = 8;
		contentPane.add(passwordFieldRepContraseña, gbc_passwordFieldRepContraseña);

		// Fecha nacimiento
		JLabel lblFecha = new JLabel("Fecha Nacimiento:");
		GridBagConstraints gbc_lblFecha = new GridBagConstraints();
		gbc_lblFecha.anchor = GridBagConstraints.WEST;
		gbc_lblFecha.insets = new Insets(0, 0, 5, 5);
		gbc_lblFecha.gridx = 1;
		gbc_lblFecha.gridy = 10;
		contentPane.add(lblFecha, gbc_lblFecha);

		dateChooserFechaNac = new JDateChooser();
		dateChooserFechaNac.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		dateChooserFechaNac.setBackground(naranjaOscuro);
		GridBagConstraints gbc_dateChooserFechaNac = new GridBagConstraints();
		gbc_dateChooserFechaNac.fill = GridBagConstraints.BOTH;
		gbc_dateChooserFechaNac.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooserFechaNac.gridx = 2;
		gbc_dateChooserFechaNac.gridy = 10;
		contentPane.add(dateChooserFechaNac, gbc_dateChooserFechaNac);
		
				
		
		
		lblIcon = new JLabel("");
				
						
		lblIcon.setIcon(new ImageIcon(VentanaRegistro.class.getResource("/account.png")));
		GridBagConstraints gbc_lblIcon = new GridBagConstraints();
		gbc_lblIcon.gridheight = 3;
		gbc_lblIcon.insets = new Insets(0, 0, 5, 5);
		gbc_lblIcon.gridx = 4;
		gbc_lblIcon.gridy = 10;
		contentPane.add(lblIcon, gbc_lblIcon);

		// Saludo
		JLabel lblSaludo = new JLabel("Saludo:");
		GridBagConstraints gbc_lblSaludo = new GridBagConstraints();
		gbc_lblSaludo.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_lblSaludo.gridx = 1;
		gbc_lblSaludo.gridy = 12;
		contentPane.add(lblSaludo, gbc_lblSaludo);

		textFieldSaludo = new JTextField();
		textFieldSaludo.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		textFieldSaludo.setBackground(naranjaOscuro);
		textFieldSaludo.setColumns(10);
		GridBagConstraints gbc_textFieldSaludo = new GridBagConstraints();
		gbc_textFieldSaludo.fill = GridBagConstraints.BOTH;
		gbc_textFieldSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldSaludo.gridx = 2;
		gbc_textFieldSaludo.gridy = 12;
		contentPane.add(textFieldSaludo, gbc_textFieldSaludo);

		agregarBotones();
	}

	private void agregarBotones() {
		JPanel panel = new JPanel();
		panel.setBackground(naranjaClaro);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 2;
		gbc.gridy = 13;
		contentPane.add(panel, gbc);

		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setPreferredSize(new Dimension(87, 33));
		btnRegistrar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		btnRegistrar.setOpaque(true);
		btnRegistrar.setBackground(turquesa);
		btnRegistrar.addActionListener(this::registrarUsuario);
		panel.add(btnRegistrar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setPreferredSize(new Dimension(87, 33));
		btnCancelar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		btnCancelar.setBackground(boton);
		btnCancelar.setOpaque(true);
		btnCancelar.addActionListener(e -> cancelarRegistro());
		
		
		panel.add(Box.createHorizontalGlue());
		panel.add(btnCancelar);
		JButton btnSeleccionarImagen = new JButton("Seleccionar Imagen");
		btnSeleccionarImagen.setBackground(boton);
		btnSeleccionarImagen.addActionListener(e -> seleccionarImagenDesdePC());
		GridBagConstraints gbc_btnSeleccionarImagen = new GridBagConstraints();
		gbc_btnSeleccionarImagen.insets = new Insets(0, 0, 5, 5);
		gbc_btnSeleccionarImagen.gridx = 4;
		gbc_btnSeleccionarImagen.gridy = 13;
		contentPane.add(btnSeleccionarImagen, gbc_btnSeleccionarImagen);
	}

	



	
	private void registrarUsuario(ActionEvent e) {
	    if (!datosCorrectos()) return;

	    ImageIcon fotoPerfil = (ImageIcon) lblIcon.getIcon();
	    
	    if (fotoPerfil == null || fotoPerfil.getDescription() == null) {
	        try {
	            URL urlRecurso = VentanaRegistro.class.getResource("/recursos/account.png");
	            if (urlRecurso != null) {
	                fotoPerfil = new ImageIcon(urlRecurso);
	                fotoPerfil.setDescription("/recursos/account.png"); // Muy importante
	            } else {
	                System.err.println("❌ No se encontró recurso account.png.");
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }

	    boolean creado = Controlador.getInstancia().registrarUsuario(
	        textFieldNombre.getText(),
	        dateChooserFechaNac.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
	        fotoPerfil,
	        textFieldTelefono.getText(),
	        textFieldSaludo.getText(),
	        new String(passwordFieldContraseña.getPassword())
	    );

	    if (!creado) {
	        JOptionPane.showMessageDialog(this, "El usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
	    } else {
	        JOptionPane.showMessageDialog(this, "Usuario registrado correctamente", "Registro", JOptionPane.INFORMATION_MESSAGE);
	        Toolkit.getDefaultToolkit().beep();
	        dispose();
	        new VentanaLogin().frmLogin.setVisible(true);
	    }
	}



	private void cancelarRegistro() {
		dispose();
		new VentanaLogin().frmLogin.setVisible(true);
	}

	private boolean datosCorrectos() {
		String nombre = textFieldNombre.getText().trim();
		String telefono = textFieldTelefono.getText().trim();
		String contrasena = new String(passwordFieldContraseña.getPassword());
		String repetir = new String(passwordFieldRepContraseña.getPassword());
		if (nombre.isEmpty()) {
			mostrarError("El nombre no puede estar vacío."); return false;
		}
		if (!telefono.matches("\\d{9}")) {
			mostrarError("El teléfono debe contener 9 dígitos."); return false;
		}
		if (contrasena.length() < 6) {
			mostrarError("La contraseña debe tener al menos 6 caracteres."); return false;
		}
		if (!contrasena.equals(repetir)) {
			mostrarError("Las contraseñas no coinciden."); return false;
		}
		if (dateChooserFechaNac.getDate() == null) {
			mostrarError("Debe seleccionar una fecha de nacimiento."); return false;
		}
		return true;
	}
	
	private void seleccionarImagenDesdePC() {
	    JFileChooser seleccion = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg");
	    seleccion.setFileFilter(filter);
	    seleccion.setCurrentDirectory(new File(System.getProperty("user.home")));

	    int resultado = seleccion.showOpenDialog(this);
	    if (resultado == JFileChooser.APPROVE_OPTION) {
	        File archivo = seleccion.getSelectedFile();
	        try {
	            BufferedImage imagen = ImageIO.read(archivo);
	            if (imagen != null) {
	                imagenPerfil = imagen;
	                ImageIcon icono = new ImageIcon(imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
	                icono.setDescription(archivo.getAbsolutePath()); // 🔥 Aquí guardas la ruta
	                lblIcon.setIcon(icono);
	                lblIcon.setDisabledIcon(icono); // por si acaso
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(this, "Error al cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}


	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}
}