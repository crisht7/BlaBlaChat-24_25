package vista;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import controlador.Controlador;
import appChat.Usuario;

/**
 * Ventana de perfil del usuario, que permite ver y modificar la información del
 * usuario.
 */
public class VentanaPerfil extends JDialog {

	/**
	 * SerialVersionUID para la serialización de la clase.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Usuario actual.
	 */
    private Usuario usuario;
    /**
     * Etiqueta para mostrar la foto de perfil del usuario.
     */
    private JLabel lblFotoPerfil;
	/**
	 * Área de texto para mostrar y editar el saludo del usuario.
	 */
    private JTextArea txtSaludo;
	/**
	 * Color de fondo claro.
	 */
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor(); 
    /**
     * Color de fondo oscuro.
     */
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
	/**
	 * Color de fondo naranja.
	 */
    private final Color boton = Colores.NARANJA_BOTON.getColor();

    // ===================== Constructor =====================
	/**
	 * Crea la ventana de perfil del usuario.
	 * 
	 * @param parent Ventana padre.
	 */
    public VentanaPerfil(JFrame parent) {
        super(parent, "Mi Perfil", true);
        this.usuario = Controlador.getInstancia().getUsuarioActual();

        getContentPane().setBackground(naranjaOscuro);
        setSize(330, 350);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout(10, 10));

        JPanel panelFoto = new JPanel(new FlowLayout());
        panelFoto.setBackground(naranjaOscuro);

        lblFotoPerfil = new JLabel();
        actualizarFotoPerfil();
        
        JPopupMenu menuFoto = new JPopupMenu();
        JMenuItem itemBorrar = new JMenuItem("Borrar foto de perfil");

        itemBorrar.addActionListener(e -> {
            usuario.setFotoPerfil(new ImageIcon(getClass().getResource("/account.png")));
            actualizarFotoPerfil();

        });


        menuFoto.add(itemBorrar);
        
        lblFotoPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblFotoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    menuFoto.show(lblFotoPerfil, e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    cambiarFotoPerfil();
                }
            }
        });
        
        panelFoto.add(lblFotoPerfil);
        getContentPane().add(panelFoto, BorderLayout.NORTH);

        JPanel panelInfo = new JPanel();
        panelInfo.setBackground(naranjaOscuro);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelInfo.add(crearLabel("Nombre: " + usuario.getNombre()));
        panelInfo.add(crearLabel("Teléfono: " + usuario.getTelefono()));
        panelInfo.add(crearLabel("Fecha en la que te uniste: " + usuario.getFechaRegistro()));

        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInfo.add(crearLabel("Saludo:"));

        txtSaludo = new JTextArea(usuario.getSaludo() != null ? usuario.getSaludo() : "");
        txtSaludo.setLineWrap(true);
        txtSaludo.setWrapStyleWord(true);
        txtSaludo.setBackground(naranjaClaro);
        txtSaludo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtSaludo.setRows(2);
        txtSaludo.setColumns(25);
        txtSaludo.setMaximumSize(new Dimension(300, 50));
        txtSaludo.setPreferredSize(new Dimension(300, 50));
        panelInfo.add(txtSaludo);

        getContentPane().add(panelInfo, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(naranjaOscuro);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(boton);
        btnGuardar.addActionListener(e -> guardarCambios());
        panelBoton.add(btnGuardar);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(200, 23, 28));
        btnCerrarSesion.setForeground(new Color(0, 0, 0));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panelBoton.add(btnCerrarSesion);

        getContentPane().add(panelBoton, BorderLayout.SOUTH);
    }
    /**
     * Crea un JLabel con el texto especificado.
     * 
     * @param texto Texto a mostrar en el JLabel.
     * @return Un JLabel configurado.
     */
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setMaximumSize(new Dimension(300, 20));
        return label;
    }


	/**
	 * Actualiza la foto de perfil del usuario en la interfaz.
	 */
    private void actualizarFotoPerfil() {
        ImageIcon fotoPerfil = usuario.getFotoPerfil();
        Image img = fotoPerfil.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        lblFotoPerfil.setIcon(new ImageIcon(img));
    }

	/**
	 * Cambia la foto de perfil del usuario.
	 */
    private void cambiarFotoPerfil() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar nueva foto de perfil");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                ImageIcon nuevaFoto = new ImageIcon(archivo.getAbsolutePath());
                // Validar si la imagen es válida
                if (nuevaFoto.getIconWidth() <= 0 || nuevaFoto.getIconHeight() <= 0) {
                    throw new Exception("Imagen inválida");
                }
                usuario.setFotoPerfil(nuevaFoto);
                actualizarFotoPerfil();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "La imagen seleccionada no es válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

	/**
	 * Guarda los cambios realizados en el perfil del usuario.
	 */
    private void guardarCambios() {
        usuario.setSaludo(txtSaludo.getText().trim());
        Controlador.getInstancia().getAdaptadorUsuario().modificarUsuario(usuario);
        VentanaMain.getInstancia().refrescarFotoUsuario();
        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        dispose();
    }
    /**
     * Cierra la sesión del usuario actual y vuelve a la ventana de inicio de sesión.
     */
    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            Controlador.getInstancia().setUsuarioActual(null);
            VentanaMain.getInstancia().dispose();
            dispose();
            SwingUtilities.invokeLater(() -> {
                VentanaLogin ventanaLogin = VentanaLogin.getInstancia();
                ventanaLogin.limpiarCampos();
                ventanaLogin.setVisible(true);
                
            });
        }
    }
}
