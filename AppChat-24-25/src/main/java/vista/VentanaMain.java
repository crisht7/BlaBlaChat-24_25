package vista;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import javax.swing.*;
import appChat.*;
import controlador.Controlador;
import tds.BubbleText;

public class VentanaMain {

    public JFrame frame;
    public Chat chat;
    private static VentanaMain instancia;
    private PanelListaContactos panelListaContactos;
    private Map<Contacto, Chat> chatsRecientes;
    private JScrollPane scrollBarChat;
    private Contacto contactoActual;

    private final Color turquesa = Colores.TURQUESA.getColor();
    private final Color turquesaOscuro = Colores.TURQUESA_OSCURO.getColor();
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaMain window = new VentanaMain();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public VentanaMain() {
        chatsRecientes = new HashMap<>();
        instancia = this;
        initialize();
    }

    private void cargarChat(Contacto contacto) {
        if (contacto == null) return;

        this.contactoActual = contacto;
        chat = chatsRecientes.get(contacto);

        if (chat == null) {
            chat = crearChat();
            scrollBarChat.setViewportView(chat);

            List<Mensaje> mensajes = Optional.ofNullable(Controlador.getInstancia().getMensajes(contacto))
                    .orElse(new LinkedList<>());

            for (Mensaje m : mensajes) {
                chat.add(crearBubble(m));
            }

            if (chatsRecientes.size() >= 4) {
                chatsRecientes.remove(chatsRecientes.keySet().stream().findFirst().orElse(null));
            }

            chatsRecientes.put(contacto, chat);
        } else {
            scrollBarChat.setViewportView(chat);
        }

        chat.revalidate();
        chat.repaint();

        SwingUtilities.invokeLater(() ->
                scrollBarChat.getVerticalScrollBar().setValue(scrollBarChat.getVerticalScrollBar().getMaximum())
        );
    }

    private void enviarMensaje(JTextArea textField) {
        if (contactoActual == null || textField == null) return;

        String texto = textField.getText().trim();
        if (texto.isEmpty()) return;

        Controlador.getInstancia().enviarMensaje(texto, contactoActual);

        BubbleText burbuja = new BubbleText(chat, texto, new Color(159, 213, 192), "Tú", BubbleText.SENT, 12);
        chat.add(burbuja);

        textField.setText(null);

        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollBarChat.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });

        actualizarListaContactos();
    }

    private void enviarIcono(int idEmoticono) {
        if (contactoActual == null) return;

        Controlador.getInstancia().enviarMensaje(idEmoticono, contactoActual);

        BubbleText burbuja = new BubbleText(chat, idEmoticono, new Color(159, 213, 192), "Tú", BubbleText.SENT, 12);
        chat.add(burbuja);

        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollBarChat.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });

        actualizarListaContactos();
    }

    private Chat crearChat() {
        Chat nuevoChat = new Chat();
        nuevoChat.setBackground(new Color(241, 192, 133));
        nuevoChat.setLayout(new BoxLayout(nuevoChat, BoxLayout.Y_AXIS));
        nuevoChat.setSize(400, 700);
        scrollBarChat.getViewport().setBackground(new Color(159, 213, 192));
        return nuevoChat;
    }

    private BubbleText crearBubble(Mensaje m) {
        String emisor;
        int direccionMensaje;
        Color colorBurbuja = new Color(159, 213, 192);

        if (m.getEmisor().equals(Controlador.getInstancia().getUsuarioActual())) {
            emisor = "You";
            direccionMensaje = BubbleText.SENT;
        } else {
            Optional<ContactoIndividual> contactoOpcional = Controlador.getInstancia().getContactoDelUsuarioActual(m.getEmisor());
            emisor = contactoOpcional.map(ContactoIndividual::getNombre).orElse("Desconocido");
            direccionMensaje = BubbleText.RECEIVED;
        }

        if (m.getTexto().isEmpty()) {
            return new BubbleText(chat, m.getEmoticono(), colorBurbuja, emisor, direccionMensaje, 12);
        }
        return new BubbleText(chat, m.getTexto(), colorBurbuja, emisor, direccionMensaje, 12);
    }

    public void actualizarListaContactos() {
        List<Contacto> contactos = Optional.ofNullable(Controlador.getInstancia().getContactosUsuarioActual())
                .orElse(new LinkedList<>());
        panelListaContactos.actualizarLista(contactos);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 746, 654);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        scrollBarChat = new JScrollPane();
        frame.getContentPane().add(scrollBarChat, BorderLayout.CENTER);

        configurarPanelRecientes();
        configurarPanelNorte();
        configurarPanelChat();
    }

    private void configurarPanelRecientes() {
        JPanel panelRecientes = new JPanel(new BorderLayout());
        panelRecientes.setPreferredSize(new Dimension(250, 0));
        frame.getContentPane().add(panelRecientes, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Chats Recientes", JLabel.CENTER);
        panelRecientes.add(lblTitulo, BorderLayout.NORTH);

        panelListaContactos = new PanelListaContactos();
        panelRecientes.add(panelListaContactos, BorderLayout.CENTER);

        actualizarListaContactos();

        JList<Contacto> listaChatRecientes = new JList<>();
        listaChatRecientes.setCellRenderer(new RenderizadorListaContactos());
        listaChatRecientes.setBackground(naranjaOscuro);
        listaChatRecientes.setVisibleRowCount(16);

        DefaultListModel<Contacto> modelContactos = new DefaultListModel<>();
        Controlador.getInstancia().getContactosUsuarioActual().forEach(modelContactos::addElement);

        listaChatRecientes.setModel(modelContactos);
        listaChatRecientes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Contacto contactoSeleccionado = listaChatRecientes.getSelectedValue();
                if (contactoSeleccionado != null) {
                    cargarChat(contactoSeleccionado);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaChatRecientes);
        panelRecientes.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelNorte() {
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(naranjaClaro);
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
        frame.getContentPane().add(panelNorte, BorderLayout.NORTH);

        JComboBox<String> comboPanelRecientes = new JComboBox<>();
        comboPanelRecientes.setBackground(new Color(234, 158, 66));
        comboPanelRecientes.setEditable(true);
        panelNorte.add(comboPanelRecientes);

        JButton btnBuscarMensaje = crearBoton("Buscar", e -> new VentanaBuscar(frame).setVisible(true));
        JButton btnContactos = crearBoton("Contactos", e -> new VentanaContacto(new DefaultListModel<>()).setVisible(true));
        JButton btnTema = crearBoton("Tema", e -> {});

        JButton btnPremium = new JButton("Premium");
        btnPremium.setBackground(new Color(159, 213, 192));

        JLabel lblFoto = new JLabel("Usuario");

        panelNorte.add(btnBuscarMensaje);
        panelNorte.add(btnContactos);
        panelNorte.add(btnTema);
        panelNorte.add(Box.createHorizontalGlue());
        panelNorte.add(btnPremium);
        panelNorte.add(lblFoto);
    }

    private JButton crearBoton(String texto, ActionListener al) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(234, 158, 66));
        btn.addActionListener(al);
        return btn;
    }

    private void configurarPanelChat() {
        JPanel panelChatActual = new JPanel(new BorderLayout());
        frame.getContentPane().add(panelChatActual, BorderLayout.CENTER);

        chat = new Chat();
        chat.setBackground(naranjaClaro);
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        chat.setPreferredSize(new Dimension(400, 700));

        panelChatActual.add(chat, BorderLayout.CENTER);
        panelChatActual.add(crearPanelMensajesSur(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelMensajesSur() {
        JPanel panelMensajesSur = new JPanel();
        panelMensajesSur.setBackground(new Color(241, 192, 133));
        panelMensajesSur.setLayout(new BorderLayout());

        JTextArea txtMensaje = new JTextArea(3, 30);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        JScrollPane scrollMensaje = new JScrollPane(txtMensaje);
        scrollMensaje.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelMensajesSur.add(scrollMensaje, BorderLayout.CENTER);

        JButton btnEmoji = new JButton("");
        btnEmoji.setBackground(new Color(234, 158, 66));
        btnEmoji.setIcon(new ImageIcon(VentanaMain.class.getResource("/recursos/emoticono.png")));
        JPopupMenu menuEmojis = new JPopupMenu();
        List<String> emojis = Arrays.asList("😀", "😂", "😍", "😎", "😢", "😡", "👍", "🔥", "💯");
        for (int i = 0; i < emojis.size(); i++) {
            final int index = i;
            JMenuItem item = new JMenuItem(emojis.get(i));
            item.addActionListener(e -> enviarIcono(index));
            menuEmojis.add(item);
        }
        btnEmoji.addActionListener(e -> menuEmojis.show(btnEmoji, 0, btnEmoji.getHeight()));
        panelMensajesSur.add(btnEmoji, BorderLayout.WEST);

        JButton btnEnviar = new JButton("");
        btnEnviar.setBackground(new Color(234, 158, 66));
        btnEnviar.setIcon(new ImageIcon(VentanaMain.class.getResource("/recursos/enviar.png")));
        btnEnviar.addActionListener(e -> enviarMensaje(txtMensaje));

        panelMensajesSur.add(btnEnviar, BorderLayout.EAST);

        return panelMensajesSur;
    }

    class Chat extends JPanel implements Scrollable {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return null;
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 0;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 0;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}