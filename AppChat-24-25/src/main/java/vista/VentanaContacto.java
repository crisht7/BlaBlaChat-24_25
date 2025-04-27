package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import appChat.*;
import controlador.Controlador;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * Ventana que permite añadir un nuevo contacto.
 */
public class VentanaContacto extends JFrame {

    private static final long serialVersionUID = 1L;

    // ===================== Constantes de color =====================
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
    //private final Color turquesa = Colores.TURQUESA.getColor();
    private final Color boton = Colores.NARANJA_BOTON.getColor();

    // ===================== Componentes =====================
    private JPanel contentPane;
    private JTextField textFieldName;
    private JTextField textFieldTelf;
    private DefaultListModel<Contacto> modelContacts;

    // ===================== Constructor =====================
    public VentanaContacto(DefaultListModel<Contacto> modelo) {
        this.modelContacts = modelo;

        setTitle("Add Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 558, 334);
        inicializarComponentes();
    }

    // ===================== Inicialización de Componentes =====================

    private void inicializarComponentes() {
        contentPane = new JPanel();
        contentPane.setBackground(naranjaClaro);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc;

        // Título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(naranjaOscuro);
        JLabel label = new JLabel("Agregar Contactos");
        panelTitulo.add(label);
        gbc = crearGbc(1, 0);
        contentPane.add(panelTitulo, gbc);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(naranjaOscuro);
        gbc = crearGbc(1, 1);
        contentPane.add(panelFormulario, gbc);

        JLabel lblName = new JLabel("Nombre");
        lblName.setForeground(Color.BLACK);
        gbc = crearGbc(0, 0);
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(lblName, gbc);

        textFieldName = crearCampoTexto();
        textFieldName.addActionListener(e -> addContacto());
        gbc = crearGbc(1, 0);
        panelFormulario.add(textFieldName, gbc);

        JLabel lblPhoneNumber = new JLabel("Teléfono");
        lblPhoneNumber.setForeground(Color.BLACK);
        gbc = crearGbc(0, 3);
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(lblPhoneNumber, gbc);

        textFieldTelf = crearCampoTexto();
        textFieldTelf.addActionListener(e -> addContacto());
        gbc = crearGbc(1, 3);
        panelFormulario.add(textFieldTelf, gbc);

        // Botón ADD
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(naranjaClaro);
        JButton btnAdd = new JButton("ADD");
        btnAdd.setBackground(boton);
        btnAdd.addActionListener(arg0 -> addContacto());
        panelBoton.add(btnAdd);
        gbc = crearGbc(1, 2);
        contentPane.add(panelBoton, gbc);
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField(10);
        campo.setForeground(Color.BLACK);
        campo.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        campo.setCaretColor(new Color(245, 210, 158));
        campo.setBackground(Color.WHITE);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBackground(new Color(255, 100, 0));
            }
        });

        return campo;
    }

    private GridBagConstraints crearGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    // ===================== Validaciones =====================

    private boolean datosCorrectos() {
        List<String> errores = new LinkedList<>();

        if (textFieldName.getText().isEmpty()) {
            errores.add("Nombre no válido.");
            textFieldName.setBackground(new Color(255, 69, 0));
        }

        if (textFieldTelf.getText().isEmpty() || !isNumeric(textFieldTelf.getText())
                || Integer.parseInt(textFieldTelf.getText()) < 0) {
            errores.add("Teléfono no válido.");
            textFieldTelf.setBackground(new Color(255, 69, 0));
        }

        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errores), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isNumeric(String strNum) {
        try {
            Integer.parseInt(strNum);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    // ===================== Lógica de Añadir Contacto =====================

    /**
     * Comprueba errores y añade el contacto si está todo correcto.
     */
    private void addContacto() {
        if (!datosCorrectos()) return;

        String nombre = textFieldName.getText();
        String telefono = textFieldTelf.getText();

        Controlador controlador = Controlador.getInstancia();
        Usuario usuarioActual = controlador.getUsuarioActual();

        if (telefono.equals(usuarioActual.getTelefono())) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "No te puedes agregar a ti mismo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuarioActual.tieneContactoIndividualPorTelefono(telefono)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Este número ya está agregado como contacto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!controlador.getRepoUsuarios().buscarUsuario(telefono).isPresent()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "El teléfono no existe en la aplicación.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ContactoIndividual nuevoContacto = controlador.crearContacto(nombre, telefono);
        if (nuevoContacto != null) {
            modelContacts.add(modelContacts.size(), nuevoContacto);
            VentanaMain.getInstancia().actualizarListaContactos();
            JOptionPane.showMessageDialog(this, "Contacto añadido correctamente.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "No se ha podido añadir el contacto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
