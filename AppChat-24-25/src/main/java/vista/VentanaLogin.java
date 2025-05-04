package vista;

import controlador.Controlador;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Ventana de login principal de la aplicación.
 */
public class VentanaLogin {

    // ===================== Constantes de color =====================
	/**
	 * Color naranja claro
     */
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    /**
     * Color naranja oscuro
     */
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
    /**
     * Color turquesa
     */
    private final Color turquesa = Colores.TURQUESA.getColor();
    /**
     * Color naranja boton
     */
    private final Color boton = Colores.NARANJA_BOTON.getColor();

    // ===================== Componentes =====================
    /**
     * Ventana de login.
     */
    public JFrame frmLogin;
	/**
	 * Campo de texto para ingresar el número de teléfono.
	 */
    private JTextField textFieldTelefono;
    /**
     * Campo de texto para ingresar la contraseña.
     */
    private JPasswordField passwordFieldContraseña;
    /**
     * Instancia de la ventana de login.
     */
    private static VentanaLogin instancia;


    // ===================== Constructor =====================
    /**
     * Crea la ventana de login.
     */
    public VentanaLogin() {
        initialize();
    }
    
    /**
     * Devuelve la instancia de la ventana de login.
     * 
     * @return Instancia de la ventana de login.
     */
    public static VentanaLogin getInstancia() {
        if (instancia == null) {
            instancia = new VentanaLogin();
        }
        return instancia;
    }

    // ===================== Inicialización de Componentes =====================

    /**
     * Inicializa el contenido de la ventana.
     */
    private void initialize() {
        frmLogin = new JFrame();
        frmLogin.setTitle("Login");
        frmLogin.setBounds(100, 100, 720, 480);
        frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmLogin.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaLogin.class.getResource("/chat150.png")));
        frmLogin.getContentPane().setLayout(new BorderLayout());

        crearPanelBotones();
        crearPanelSuperior();
        crearPanelCentral();
    }

	/**
	 * Crea el panel de botones de la ventana de login.
	 */
    private void crearPanelBotones() {
        JPanel panelBot = new JPanel(new BorderLayout(0, 0));
        panelBot.setBackground(new Color(236, 215, 176));
        frmLogin.getContentPane().add(panelBot, BorderLayout.SOUTH);

        JPanel panelLogin = new JPanel();
        panelLogin.setBackground(naranjaClaro);
        panelBot.add(panelLogin, BorderLayout.NORTH);

        JButton btnLogin = new JButton("Login");
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btnLogin.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        btnLogin.setOpaque(true);
        btnLogin.setBackground(turquesa);
        btnLogin.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setPreferredSize(new Dimension(250, 40));
        btnLogin.setIcon(new ImageIcon(VentanaLogin.class.getResource("/login.png")));
        btnLogin.addActionListener(this::accionLogin);
        panelLogin.add(btnLogin);

        JPanel panelRegistrar = new JPanel();
        panelRegistrar.setBackground(naranjaClaro);
        panelBot.add(panelRegistrar, BorderLayout.SOUTH);

        JLabel lblNoCuenta = new JLabel("¿No tienes cuenta?");
        lblNoCuenta.setFont(new Font("Sitka Subheading", Font.BOLD, 14));
        lblNoCuenta.setHorizontalAlignment(SwingConstants.LEFT);
        panelRegistrar.add(lblNoCuenta);

        JButton btnRegistrar = new JButton("Regístrate");
        btnRegistrar.setOpaque(true);
        btnRegistrar.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
        btnRegistrar.setBackground(boton);
        btnRegistrar.setIcon(new ImageIcon(VentanaLogin.class.getResource("/register.png")));
        btnRegistrar.addActionListener(e -> {
            frmLogin.dispose();
            VentanaRegistro.getInstancia().setVisible(true);
        });
        panelRegistrar.add(btnRegistrar);
    }

	/**
	 * Crea el panel superior de la ventana de login.
	 */
    private void crearPanelSuperior() {
        JPanel panelTop = new JPanel(new GridBagLayout());
        panelTop.setBackground(naranjaClaro);
        frmLogin.getContentPane().add(panelTop, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblFotoLogin = new JLabel(new ImageIcon(VentanaLogin.class.getResource("/chat150v2.PNG")));
        
        
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 0, 5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelTop.add(lblFotoLogin, gbc);

        JLabel lblTitulo = new JLabel("BlaBlaChat");
        lblTitulo.setFont(new Font("Constantia", Font.BOLD, 35));
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 3;
        gbc.gridy = 0;
        panelTop.add(lblTitulo, gbc);
    }

    /**
     * Crea el panel central de la ventana de login.
     */
    private void crearPanelCentral() {
        JPanel panelMid = new JPanel(new GridBagLayout());
        panelMid.setBackground(naranjaClaro);
        panelMid.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        frmLogin.getContentPane().add(panelMid, BorderLayout.CENTER);

        GridBagConstraints gbc;

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 11));
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelMid.add(lblTelefono, gbc);

        textFieldTelefono = new JTextField();
        textFieldTelefono.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        textFieldTelefono.setBackground(naranjaOscuro);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panelMid.add(textFieldTelefono, gbc);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 11));
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panelMid.add(lblContraseña, gbc);

        passwordFieldContraseña = new JPasswordField();
        passwordFieldContraseña.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        passwordFieldContraseña.setBackground(naranjaOscuro);
        passwordFieldContraseña.setMinimumSize(new Dimension(15, 20));
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        panelMid.add(passwordFieldContraseña, gbc);
    }

    // ===================== Lógica de eventos =====================
	/**
	 * Acción al hacer clic en el botón de login.
	 * 
	 * @param e Evento de acción.
	 */
    private void accionLogin(ActionEvent e) {
        String telefono = textFieldTelefono.getText();
        @SuppressWarnings("deprecation")
        String password = passwordFieldContraseña.getText();

        boolean login = Controlador.getInstancia().hacerLogin(telefono, password);

        if (login) {
            VentanaMain ventanaMain = VentanaMain.getInstancia();
            ventanaMain.limpiarEstadoChats(); 
            ventanaMain.actualizarListaContactos();
            ventanaMain.actualizarDatosUsuario(); 
            ventanaMain.setVisible(true);
            frmLogin.setVisible(false);
        } else {
            mostrarDialogoError();
        }
    }
    /**
     * Muestra un diálogo de error al intentar iniciar sesión.
     */
    private void mostrarDialogoError() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel mensaje = new JLabel("Login incorrecto", SwingConstants.CENTER);
        ImageIcon icono = new ImageIcon(VentanaLogin.class.getResource("/cancel.png"));
        panel.setBackground(naranjaClaro);
        panel.add(mensaje, BorderLayout.CENTER);
        panel.add(new JLabel(icono), BorderLayout.WEST);

        UIManager.put("Button.background", new Color(244, 97, 34));
        UIManager.put("Panel.background", naranjaClaro);
        UIManager.put("OptionPane.background", naranjaClaro);

        JOptionPane.showMessageDialog(frmLogin, panel, "Login failed", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Muestra u oculta la ventana de login.
     * 
     * @param visible true para mostrar la ventana, false para ocultarla.
     */
    public void setVisible(boolean visible) {
        frmLogin.setVisible(visible);
    }
}
