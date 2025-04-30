package vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import filtros.*;
import appChat.Mensaje;
import appChat.Usuario;
import controlador.Controlador;

/**
 * Ventana para realizar búsquedas de mensajes aplicando diferentes filtros.
 */
@SuppressWarnings("serial")
public class VentanaBuscar extends JDialog {

    // ===================== Constantes de color y texto =====================
    private static final String PLACEHOLDER_TEXTO = "Texto del mensaje";
    private static final String PLACEHOLDER_TELEFONO = "Teléfono del contacto";
    private static final String PLACEHOLDER_CONTACTO = "Nombre del contacto";
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
    private final Color boton = Colores.NARANJA_BOTON.getColor();
    private final Color turquesa = Colores.TURQUESA.getColor();
    private final Color turquesaOscuro = Colores.TURQUESA_OSCURO.getColor();

    // ===================== Componentes de la vista =====================
    private JTextField textFieldTexto;
    private JTextField textFieldTelefono;
    private JTextField textFieldContacto;
    private JPanel panelResultados;

    // ===================== Constructor =====================
    public VentanaBuscar(JFrame parent) {
        super(parent, "Buscar", true);

        configurarVentana();
        inicializarComponentes();
        configurarAcciones();
    }

    // ===================== Métodos de configuración =====================

    private void configurarVentana() {
        getContentPane().setBackground(naranjaOscuro);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setResizable(false);
        getContentPane().setLayout(null);
    }

    private void inicializarComponentes() {
        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBackground(naranjaClaro);
        panelSuperior.setBounds(10, 10, 760, 60);
        getContentPane().add(panelSuperior);

        // Campo de texto para buscar por contenido
        textFieldTexto = crearCampoTexto(PLACEHOLDER_TEXTO);
        panelSuperior.add(textFieldTexto);

        // Campo de texto para buscar por teléfono
        textFieldTelefono = crearCampoTexto(PLACEHOLDER_TELEFONO);
        panelSuperior.add(textFieldTelefono);

        // Campo de texto para buscar por contacto
        textFieldContacto = crearCampoTexto(PLACEHOLDER_CONTACTO);
        panelSuperior.add(textFieldContacto);

        // Botón buscar
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(boton);
        panelSuperior.add(btnBuscar);

        // Panel de resultados
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBackground(naranjaClaro);

        JScrollPane scrollPane = new JScrollPane(panelResultados);
        scrollPane.setBounds(10, 80, 760, 470);
        getContentPane().add(scrollPane);

        // Acción buscar
        btnBuscar.addActionListener(e -> realizarBusqueda());

        // Evitar que el campo de texto tome el foco automáticamente
        SwingUtilities.invokeLater(btnBuscar::requestFocusInWindow);
    }

    private JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField();
        campo.setPreferredSize(new Dimension(150, 25));
        campo.setBackground(naranjaOscuro);
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
        return campo;
    }

    private void configurarAcciones() {
        // Separé esta función si quieres en el futuro expandir acciones en init
    }

    // ===================== Lógica de búsqueda =====================

    private void realizarBusqueda() {
        panelResultados.removeAll();

        FiltroCombinado filtroCombinado = new FiltroCombinado();

        String texto = textFieldTexto.getText().trim();
        if (!texto.isEmpty() && !texto.equals(PLACEHOLDER_TEXTO)) {
            filtroCombinado.añadirFiltro(new FiltroPorTexto(texto));
        }

        String telefono = textFieldTelefono.getText().trim();
        if (!telefono.isEmpty() && !telefono.equals(PLACEHOLDER_TELEFONO)) {
            filtroCombinado.añadirFiltro(new FiltroPorTelefono(telefono));
        }

        String contacto = textFieldContacto.getText().trim();
        if (!contacto.isEmpty() && !contacto.equals(PLACEHOLDER_CONTACTO)) {
            filtroCombinado.añadirFiltro(new FiltroPorNombre(contacto));
        }
        
        if (filtroCombinado.estaVacio()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduzca al menos un criterio de búsqueda.", "Búsqueda vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }


        Controlador ctrl = Controlador.getInstancia();

        List<Mensaje> mensajes = ctrl.getContactosUsuarioActual().stream()
                .flatMap(c -> ctrl.getMensajes(c).stream())
                .distinct()
                .collect(Collectors.toList());

        List<Mensaje> resultados = filtroCombinado.filtrar(mensajes);

        if (resultados.isEmpty()) {
            panelResultados.add(new JLabel("No se encontraron mensajes."));
        } else {
            for (Mensaje m : resultados) {
                panelResultados.add(crearPanelMensaje(m));
            }
        }

        panelResultados.revalidate();
        panelResultados.repaint();
    }

    // ===================== Métodos auxiliares =====================

    private JPanel crearPanelMensaje(Mensaje m) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(naranjaClaro);

        JLabel lblEmisor = new JLabel(m.getEmisor().getNombre());
        JLabel lblReceptor = new JLabel(m.getReceptor().getNombre(), SwingConstants.RIGHT);

        JTextArea textArea = new JTextArea(m.getTexto());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        Usuario usuarioActual = Controlador.getInstancia().getUsuarioActual();
        if (m.getEmisor().equals(usuarioActual)) {
            textArea.setBackground(turquesa); // Mensajes enviados por mí
        } else {
            textArea.setBackground(turquesaOscuro); // Mensajes recibidos
        }

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(turquesaOscuro);
        top.add(lblEmisor, BorderLayout.WEST);
        top.add(lblReceptor, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);
        panel.add(textArea, BorderLayout.CENTER);

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }
}
