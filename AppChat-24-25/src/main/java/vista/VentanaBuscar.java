package vista;

import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import filtros.*;
import appChat.Mensaje;
import appChat.Usuario;
import appChat.Contacto;
import controlador.Controlador;


@SuppressWarnings("serial")
public class VentanaBuscar extends JDialog { 

    private JTextField textFieldTexto;
    private JTextField textFieldTelefono;
    private JTextField textFieldContacto;
    private JPanel panelResultados;
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
    private final Color boton = Colores.NARANJA_BOTON.getColor();
    private final Color turquesa = Colores.TURQUESA.getColor();
    private final Color turquesaOscuro = Colores.TURQUESA_OSCURO.getColor();
    private static final String PLACEHOLDER_TEXTO = "Texto del mensaje";
    private static final String PLACEHOLDER_TELEFONO = "Teléfono del contacto";
    private static final String PLACEHOLDER_CONTACTO = "Nombre del contacto";




    public VentanaBuscar(JFrame parent) {
        super(parent, "Buscar", true);
        getContentPane().setBackground(naranjaOscuro);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setResizable(false);
        getContentPane().setLayout(null);
        

        // Panel superior (Filtros y botón)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(naranjaClaro);
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBounds(10, 10, 760, 60);
        getContentPane().add(panelSuperior);

        // Campo de texto
        textFieldTexto = new JTextField("");
        textFieldTexto.setBackground(naranjaOscuro);
        textFieldTexto.setPreferredSize(new Dimension(150, 25));
        panelSuperior.add(textFieldTexto);
        // Placeholder para "Texto del mensaje"
        textFieldTexto.setText(PLACEHOLDER_TEXTO); 
        textFieldTexto.setForeground(Color.GRAY);
        textFieldTexto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textFieldTexto.getText().equals(PLACEHOLDER_TEXTO)) {
                    textFieldTexto.setText("");
                    textFieldTexto.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textFieldTexto.getText().isEmpty()) {
                    textFieldTexto.setText(PLACEHOLDER_TEXTO);
                    textFieldTexto.setForeground(Color.GRAY);
                }
            }
        });

        // Teléfono
        textFieldTelefono = new JTextField("");
        textFieldTelefono.setPreferredSize(new Dimension(150, 25));
        textFieldTelefono.setBackground(naranjaOscuro);
     // Placeholder para "Teléfono del contacto"
        textFieldTelefono.setText(PLACEHOLDER_TELEFONO);
        textFieldTelefono.setForeground(Color.GRAY);
        textFieldTelefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textFieldTelefono.getText().equals(PLACEHOLDER_TELEFONO)) {
                    textFieldTelefono.setText("");
                    textFieldTelefono.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textFieldTelefono.getText().isEmpty()) {
                    textFieldTelefono.setText(PLACEHOLDER_TELEFONO);
                    textFieldTelefono.setForeground(Color.GRAY);
                }
            }
        });

        panelSuperior.add(textFieldTelefono);

        // Contacto
        textFieldContacto = new JTextField("");
        textFieldContacto.setPreferredSize(new Dimension(150, 25));
        textFieldContacto.setBackground(naranjaOscuro);
     // Placeholder para "Nombre del contacto"
        textFieldContacto.setText(PLACEHOLDER_CONTACTO);
        textFieldContacto.setForeground(Color.GRAY);
        textFieldContacto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textFieldContacto.getText().equals(PLACEHOLDER_CONTACTO)) {
                    textFieldContacto.setText("");
                    textFieldContacto.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textFieldContacto.getText().isEmpty()) {
                    textFieldContacto.setText(PLACEHOLDER_CONTACTO);
                    textFieldContacto.setForeground(Color.GRAY);
                }
            }
        });
        panelSuperior.add(textFieldContacto);


        // Botón buscar
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(boton);
        panelSuperior.add(btnBuscar);

        // Panel resultados
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBackground(naranjaClaro);

        JScrollPane scrollPane = new JScrollPane(panelResultados);
        scrollPane.setBounds(10, 80, 760, 470);
        getContentPane().add(scrollPane);

        // Acción buscar
        btnBuscar.addActionListener(e -> { 
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
        });
        
     // Evitar que el campo de texto tome el foco automáticamente
        SwingUtilities.invokeLater(() -> {
            btnBuscar.requestFocusInWindow();
        }); 
    }

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
