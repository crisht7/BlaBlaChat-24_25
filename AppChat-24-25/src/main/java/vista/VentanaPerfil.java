package vista;


import javax.swing.*;
import java.awt.*;
import java.io.File;

import controlador.Controlador;
import appChat.Usuario;

public class VentanaPerfil extends JDialog {

    private Usuario usuario;
    private JLabel lblFotoPerfil;
    private JTextField txtSaludo;
    private final Color naranjaClaro = new Color(234, 158, 66);
    private final Color naranjaOscuro = new Color(198, 101, 18);

    public VentanaPerfil(JFrame parent) {
        super(parent, "Mi Perfil", true);
        this.usuario = Controlador.getInstancia().getUsuarioActual();

        getContentPane().setBackground(naranjaOscuro);
        setSize(400, 500);
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
        panelInfo.setBackground(naranjaClaro);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelInfo.add(crearLabel("Nombre: " + usuario.getNombre()));
        panelInfo.add(crearLabel("Teléfono: " + usuario.getTelefono()));
        panelInfo.add(crearLabel("Fecha de nacimiento: " + usuario.getFechaNacimiento()));

        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));

        panelInfo.add(new JLabel("Saludo:"));
        txtSaludo = new JTextField(usuario.getSaludo() != null ? usuario.getSaludo() : "");
        panelInfo.add(txtSaludo);

        getContentPane().add(panelInfo, BorderLayout.CENTER);

        // Panel inferior: Botón guardar
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(naranjaOscuro);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarCambios());
        panelBoton.add(btnGuardar);

        getContentPane().add(panelBoton, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
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

        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        dispose();
    }
}
