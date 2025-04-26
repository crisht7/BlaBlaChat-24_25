package vista;


import javax.swing.*;
import java.awt.*;
import java.io.File;

import controlador.Controlador;
import appChat.Usuario;

public class VentanaPerfil extends JDialog {

    private Usuario usuario;
    private JLabel lblFotoPerfil;
    private JTextArea txtSaludo;
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor(); 
	private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
	private final Color boton = Colores.NARANJA_BOTON.getColor();



    public VentanaPerfil(JFrame parent) {
        super(parent, "Mi Perfil", true);
        this.usuario = Controlador.getInstancia().getUsuarioActual();

        getContentPane().setBackground(naranjaOscuro);
        setSize(330, 350);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout(10, 10));

        // Panel superior: Foto de perfil
        JPanel panelFoto = new JPanel(new FlowLayout());
        panelFoto.setBackground(naranjaOscuro);

        lblFotoPerfil = new JLabel();
        actualizarFotoPerfil();
        lblFotoPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblFotoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cambiarFotoPerfil();
            }
        });
        panelFoto.add(lblFotoPerfil);
        getContentPane().add(panelFoto, BorderLayout.NORTH);

     // Panel centro: Información usuario
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

        // Controlar tamaño de verdad
        txtSaludo.setRows(2);
        txtSaludo.setColumns(25);
        txtSaludo.setMaximumSize(new Dimension(300, 50)); // <-- tamaño máximo REAL
        txtSaludo.setPreferredSize(new Dimension(300, 50)); // <-- preferido también


        panelInfo.add(txtSaludo);

        getContentPane().add(panelInfo, BorderLayout.CENTER);


        // Panel inferior: Botón guardar
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(naranjaOscuro);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarCambios());
        btnGuardar.setBackground(boton);
        panelBoton.add(btnGuardar);

        getContentPane().add(panelBoton, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setMaximumSize(new Dimension(300, 20)); // máximo espacio permitido
        return label;
    }


 
    private void actualizarFotoPerfil() {
        ImageIcon fotoPerfil = usuario.getFotoPerfil();
        if (fotoPerfil != null) {
            Image img = fotoPerfil.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblFotoPerfil.setIcon(new ImageIcon(img));
        } else {
            lblFotoPerfil.setIcon(new ImageIcon(getClass().getResource("/anonimo.png")));
        }
    }

    private void cambiarFotoPerfil() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar nueva foto de perfil");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            ImageIcon nuevaFoto = new ImageIcon(archivo.getAbsolutePath());
            usuario.setFotoPerfil(nuevaFoto);
            actualizarFotoPerfil();
        }
    }

    private void guardarCambios() {
        usuario.setSaludo(txtSaludo.getText().trim());
        Controlador.getInstancia().getAdaptadorUsuario().modificarUsuario(usuario);
        VentanaMain.getInstancia().refrescarFotoUsuario();


        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        dispose();
    }
}
