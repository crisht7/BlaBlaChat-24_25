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

    public VentanaBuscar(JFrame parent) {
        super(parent, "Buscar", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setResizable(false);
        getContentPane().setLayout(null);

        // Panel superior (Filtros y botón)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBounds(10, 10, 760, 60);
        getContentPane().add(panelSuperior);

        // Campo de texto
        textFieldTexto = new JTextField("");
        textFieldTexto.setPreferredSize(new Dimension(150, 25));
        panelSuperior.add(textFieldTexto);

        // Teléfono
        textFieldTelefono = new JTextField("");
        textFieldTelefono.setPreferredSize(new Dimension(150, 25));
        panelSuperior.add(textFieldTelefono);

        // Contacto
        textFieldContacto = new JTextField("");
        textFieldContacto.setPreferredSize(new Dimension(150, 25));
        panelSuperior.add(textFieldContacto);


        // Botón buscar
        JButton btnBuscar = new JButton("Buscar");
        panelSuperior.add(btnBuscar);

        // Panel resultados
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelResultados);
        scrollPane.setBounds(10, 80, 760, 470);
        getContentPane().add(scrollPane);

        // Acción buscar
        btnBuscar.addActionListener(e -> {
            panelResultados.removeAll();

            FiltroCombinado filtroCombinado = new FiltroCombinado();

            String texto = textFieldTexto.getText().trim();
            if (!texto.isEmpty()) filtroCombinado.añadirFiltro(new FiltroPorTexto(texto));

            String telefono = textFieldTelefono.getText().trim();
            if (!telefono.isEmpty()) filtroCombinado.añadirFiltro(new FiltroPorTelefono(telefono));

            String contacto = textFieldContacto.getText().trim();
            if (!contacto.isEmpty()) filtroCombinado.añadirFiltro(new FiltroPorNombre(contacto));

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
    }

    private JPanel crearPanelMensaje(Mensaje m) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JLabel lblEmisor = new JLabel(m.getEmisor().getNombre());
        JLabel lblReceptor = new JLabel(m.getReceptor().getNombre(), SwingConstants.RIGHT);
        JTextArea textArea = new JTextArea(m.getTexto());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(Color.LIGHT_GRAY);

        JPanel top = new JPanel(new BorderLayout());
        top.add(lblEmisor, BorderLayout.WEST);
        top.add(lblReceptor, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);
        panel.add(textArea, BorderLayout.CENTER);

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }
    
}
