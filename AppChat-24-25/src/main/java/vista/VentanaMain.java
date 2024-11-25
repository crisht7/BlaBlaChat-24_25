package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controlador.Controlador;
import modelo.Mensaje;
import tds.BubbleText;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaMain {

    public JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaMain window = new VentanaMain();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Create the application.
     */
    public VentanaMain() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 746, 654);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        // Panel para mostrar mensajes recientes
        JPanel panelRecientes = new JPanel();
        frame.getContentPane().add(panelRecientes, BorderLayout.WEST);
        panelRecientes.setLayout(new BorderLayout(0, 0));

        JLabel lblTitulo = new JLabel("Mensajes Recientes");
        panelRecientes.add(lblTitulo, BorderLayout.NORTH);

        // JList para mensajes recientes con MensajeCellRenderer
        JList<Mensaje> listaChatRecientes = new JList<>();
        listaChatRecientes.setVisibleRowCount(16);
        listaChatRecientes.setCellRenderer(new MensajeCellRenderer());

     // Obtener mensajes recientes desde el controlador
        List<Mensaje> mensajes = Controlador.getMensajesRecientesPorUsuario();
        DefaultListModel<Mensaje> model = new DefaultListModel<>();
        for (Mensaje mensaje : mensajes) {
            if (mensaje != null && mensaje.getEmisor() != null && mensaje.getReceptor() != null) {
                model.addElement(mensaje);
            } else {
                System.err.println("Mensaje inválido: " + mensaje);
            }
        }
        listaChatRecientes.setModel(model);
        // Agregar la lista a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(listaChatRecientes);
        panelRecientes.add(scrollPane, BorderLayout.CENTER);

        // Panel superior para botones y opciones
        JPanel panelNorte = new JPanel();
        frame.getContentPane().add(panelNorte, BorderLayout.NORTH);
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));

        @SuppressWarnings("rawtypes")
        JComboBox comboPanelRecientes = new JComboBox();
        comboPanelRecientes.setEditable(true);
        panelNorte.add(comboPanelRecientes);

        JButton btnEnviarMensaje = new JButton("Enviar");
        panelNorte.add(btnEnviarMensaje);

        JButton btnBuscarMensaje = new JButton("Buscar");
        panelNorte.add(btnBuscarMensaje);

        JButton btnContactos = new JButton("Contactos");
        btnContactos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		VentanaContacto ventanaContactos = new VentanaContacto(frame);
                ventanaContactos.setVisible(true);
        	}
        });
        panelNorte.add(btnContactos);
        

        Component horizontalGlue = Box.createHorizontalGlue();
        panelNorte.add(horizontalGlue);

        JButton btnPremium = new JButton("Premium");
        panelNorte.add(btnPremium);

        JLabel lblFoto = new JLabel("Usuario");
        panelNorte.add(lblFoto);

        // Panel principal para el chat actual
        JPanel panelChatActual = new JPanel();
        frame.getContentPane().add(panelChatActual, BorderLayout.CENTER);
        panelChatActual.setLayout(new BorderLayout(0, 0));

        // Panel para mensajes enviados/recibidos
        JPanel chat = new JPanel();
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        chat.setSize(400, 700);
        chat.setMinimumSize(new Dimension(400, 700));
        chat.setMaximumSize(new Dimension(400, 700));
        chat.setPreferredSize(new Dimension(400, 700));

        // Mensajes de ejemplo para el chat actual
        BubbleText burbuja1 = new BubbleText(chat, "Hola grupo!!", Color.GREEN, "J.Ramón", BubbleText.SENT);
        chat.add(burbuja1);

        BubbleText burbuja2 = new BubbleText(chat,
                "Hola, ¿Está seguro de que la burbuja usa varias líneas si es necesario?", Color.LIGHT_GRAY, "Alumno",
                BubbleText.RECEIVED);
        chat.add(burbuja2);

        // Añadir el panel del chat al centro
        panelChatActual.add(chat, BorderLayout.CENTER);

        // Panel inferior para enviar mensajes
        JPanel panelMensajesSur = new JPanel();
        panelChatActual.add(panelMensajesSur, BorderLayout.SOUTH);
    }
}
