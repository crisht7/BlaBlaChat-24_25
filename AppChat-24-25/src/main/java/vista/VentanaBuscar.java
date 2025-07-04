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
	/**
	 * Constante para el texto del placeholder del campo de texto.
     */
    private static final String PLACEHOLDER_TEXTO = "Texto del mensaje";
	/**
	 * Constante para el texto del placeholder del campo de teléfono.
	 */
    private static final String PLACEHOLDER_TELEFONO = "Teléfono del contacto";
    /**
     * Constante para el texto del placeholder del campo de contacto.
     */
    private static final String PLACEHOLDER_CONTACTO = "Nombre del contacto";
	/**
	 * Color naranja claro
	 */
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    /**
     * Color naranja oscuro
     */
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
	/**
	 * Color naranja boton
	 */
    private final Color boton = Colores.NARANJA_BOTON.getColor();
	/**
	 * Color turquesa
	 */
    private final Color turquesa = Colores.TURQUESA.getColor();
    /**
     * Color turquesa oscuro
     */
    private final Color turquesaOscuro = Colores.TURQUESA_OSCURO.getColor();

    // ===================== Componentes de la vista =====================
    /**
     * Campo de texto para ingresar el texto del mensaje.
     */
    private JTextField textFieldTexto;
    /**
     * Campo de texto para ingresar el teléfono del contacto.
     */
    private JTextField textFieldTelefono;
	/**
	 * Campo de texto para ingresar el nombre del contacto.
	 */
    private JTextField textFieldContacto;
    /**
     * Panel para mostrar los resultados de la búsqueda.
     */
    private JPanel panelResultados;

    // ===================== Constructor =====================
	/**
	 * Crea la ventana de búsqueda.
	 *
	 * @param parent Ventana padre.
	 */
    public VentanaBuscar(JFrame parent) {
        super(parent, "Buscar", true);

        configurarVentana();
        inicializarComponentes();
    }

    // ===================== Métodos de configuración =====================
    /**
     * Configura la ventana de búsqueda.
     */
    private void configurarVentana() {
        getContentPane().setBackground(naranjaOscuro);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setResizable(false);
        getContentPane().setLayout(null);
    }

	/**
	 * Inicializa los componentes de la ventana.
	 */
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

	/**
	 * Crea un campo de texto con un placeholder.
	 * 
	 * @param placeholder El texto que se mostrará como placeholder.
	 * @return El campo de texto creado.
	 */
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


    // ===================== Lógica de búsqueda =====================

	/**
	 * Realiza la búsqueda de mensajes aplicando los filtros seleccionados.
	 */
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

    /**
     * Crea un panel para mostrar un mensaje.
     * @param m mensaje a mostrar
     * @return el panel creado
     */
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
