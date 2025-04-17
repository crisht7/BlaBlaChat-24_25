package vista;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import javax.swing.*;
import java.util.Vector;
import java.util.stream.Collectors;

import appChat.*;
import controlador.Controlador;
import tds.BubbleText;

@SuppressWarnings("serial")
public class VentanaMain extends JFrame {

    public Chat chat;
    private static VentanaMain instancia;
    private Map<Contacto, Chat> chatsRecientes;
    private JScrollPane scrollBarChat;
    private Contacto contactoActual;
    private JList<Contacto> listaChatRecientes;
    private JComboBox<String> comboPanelRecientes;

    private final Color turquesa = Colores.TURQUESA.getColor();
    private final Color turquesaOscuro = Colores.TURQUESA_OSCURO.getColor();
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaMain window = new VentanaMain();
                window.setVisible(true);
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

    /**
     * Método para obtener la instancia de la ventana principal.
     * 
     * @return instancia de VentanaMain
     */
    public static VentanaMain getInstancia() {
        return instancia;
    }
    

	/**
	 * Clase interna para el panel de chat.
	 */
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
    
	/**
	 * Clase interna para el renderizado de la lista de contactos.
	 */
	class CellRenderer extends JPanel implements ListCellRenderer<Contacto> {
        private JLabel nombreLabel;
        private JLabel telefonoLabel;
        private JLabel mensajePreviewLabel;
        private JLabel iconoLabel;

        public CellRenderer() {
            setLayout(new BorderLayout(5, 5));

            iconoLabel = new JLabel();

            nombreLabel = new JLabel();
            nombreLabel.setFont(new Font("Arial", Font.BOLD, 14));

            telefonoLabel = new JLabel();
            telefonoLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            mensajePreviewLabel = new JLabel();
            mensajePreviewLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            mensajePreviewLabel.setForeground(Color.DARK_GRAY);

            JPanel panelTexto = new JPanel(new GridLayout(3, 1));
            panelTexto.add(nombreLabel);
            panelTexto.add(telefonoLabel);
            panelTexto.add(mensajePreviewLabel);

            add(iconoLabel, BorderLayout.WEST);
            add(panelTexto, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Contacto> list,
                                                      Contacto contacto, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            if (contacto != null) {
                nombreLabel.setText(contacto.getNombre());

                if (contacto instanceof ContactoIndividual) {
                    ContactoIndividual cIndividual = (ContactoIndividual) contacto;
                    telefonoLabel.setText(cIndividual.getTelefono());

                    // Cargar directamente la foto de perfil
                    ImageIcon fotoPerfil = cIndividual.getFoto();
                    Image imagenEscalada = fotoPerfil.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    iconoLabel.setIcon(new ImageIcon(imagenEscalada));
                } else {
                    telefonoLabel.setText("");
                }

                // 🟡 Mostrar último mensaje (texto)
                List<Mensaje> mensajes = Controlador.getInstancia().getMensajes(contacto);
                if (!mensajes.isEmpty()) {
                    Mensaje ultimo = mensajes.get(mensajes.size() - 1);
                    String texto = ultimo.getTexto().isEmpty() ? "[Emoticono]" : ultimo.getTexto();
                    mensajePreviewLabel.setText("Último: " + texto);
                } else {
                    mensajePreviewLabel.setText("Sin mensajes");
                }
                
                Color fondo = isSelected ?  naranjaClaro : naranjaOscuro;

                setBackground(fondo);
                nombreLabel.setBackground(fondo);
                telefonoLabel.setBackground(fondo);
                mensajePreviewLabel.setBackground(fondo);
                iconoLabel.setBackground(fondo); // También si quieres el fondo detrás del icono
                
                // los ponemos opacos
                setOpaque(true);
                nombreLabel.setOpaque(true);
                telefonoLabel.setOpaque(true);
                mensajePreviewLabel.setOpaque(true);
                iconoLabel.setOpaque(true);
            }

            return this;
        }
    }
    
    /**
	 * Método para inicializar la ventana principal.
	 */
    private void initialize() {
        this.setBounds(100, 100, 746, 654);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout(0, 0));

        scrollBarChat = new JScrollPane();
        this.getContentPane().add(scrollBarChat, BorderLayout.CENTER);

        configurarPanelRecientes();
        configurarPanelNorte();
        configurarPanelChat();
    }

	/**
	 * Método para configurar el panel de chats recientes.
	 */
    private void configurarPanelRecientes() {
        JPanel panelRecientes = new JPanel(new BorderLayout());
        panelRecientes.setPreferredSize(new Dimension(250, 0));
        this.getContentPane().add(panelRecientes, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Chats Recientes", JLabel.CENTER);
        panelRecientes.add(lblTitulo, BorderLayout.NORTH);

        listaChatRecientes = new JList<>();
        listaChatRecientes.setCellRenderer(new CellRenderer());
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

        // Menú contextual para contactos
        JPopupMenu menuContextual = new JPopupMenu();
        JMenuItem itemCambiarNombre = new JMenuItem("Cambiar nombre");

        itemCambiarNombre.addActionListener(e -> {
            Contacto seleccionado = listaChatRecientes.getSelectedValue();
            if (seleccionado != null) {
                String nuevoNombre = JOptionPane.showInputDialog(this, "Introduce el nuevo nombre:", seleccionado.getNombre());
                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    Controlador.getInstancia().renombrarContacto(seleccionado, nuevoNombre.trim());
                    actualizarListaContactos();
                }
            }
        });

        menuContextual.add(itemCambiarNombre);

        listaChatRecientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = listaChatRecientes.locationToIndex(e.getPoint());
                    if (index != -1) {
                        listaChatRecientes.setSelectedIndex(index); // Selecciona el elemento sobre el que se hace click
                        menuContextual.show(listaChatRecientes, e.getX(), e.getY());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaChatRecientes);
        panelRecientes.add(scrollPane, BorderLayout.CENTER);
    }


	/**
	 * Método para configurar el panel norte de la ventana.
	 */
    private void configurarPanelNorte() {
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(naranjaClaro);
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
        this.getContentPane().add(panelNorte, BorderLayout.NORTH);

        comboPanelRecientes = new JComboBox<>();
        comboPanelRecientes.setMaximumSize(new Dimension(150, 30));
        comboPanelRecientes.setBackground(new Color(234, 158, 66));
        comboPanelRecientes.setEditable(true);
        panelNorte.add(comboPanelRecientes);

        panelNorte.add(crearBoton("Ir al chat", e -> abrirChatConNumeroDesdeCombo()));

        // Asegurate de agregar todos los botones:
        panelNorte.add(crearBoton("Buscar", e -> new VentanaBuscar(this).setVisible(true)));
        panelNorte.add(crearBoton("Contactos", e -> new VentanaContacto(new DefaultListModel<>()).setVisible(true)));
        panelNorte.add(crearBoton("Tema", e -> {}));
        panelNorte.add(crearBoton("Añadir Grupo", e -> new VentanaGrupo(this).setVisible(true)));


        panelNorte.add(Box.createHorizontalGlue());

        JButton btnPremium = new JButton("Premium");
        btnPremium.setBackground(new Color(159, 213, 192));

        btnPremium.addActionListener(e -> {
            Usuario usuarioActual = Controlador.getInstancia().getUsuarioActual();

            if (usuarioActual != null && !usuarioActual.isPremium()) {
                int opcion = JOptionPane.showConfirmDialog(
                        this,
                        "¿Quieres suscribirte a la versión Premium?\n(La suscripción es anual)",
                        "Activar Premium",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    usuarioActual.setPremium(true);
                    Controlador.getInstancia().getAdaptadorUsuario().modificarUsuario(usuarioActual);

                    JOptionPane.showMessageDialog(
                            this,
                            "¡Felicidades! Ahora eres usuario premium.",
                            "Suscripción activada",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } else if (usuarioActual != null && usuarioActual.isPremium()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ya eres usuario premium",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        panelNorte.add(btnPremium);

        
        panelNorte.add(Box.createRigidArea(new Dimension(10, 0))); // 10 píxeles de separación

        Usuario usuarioActual = Controlador.getInstancia().getUsuarioActual();

        if (usuarioActual != null) {
            ImageIcon fotoPerfil = usuarioActual.getFotoPerfil();  // Ya sabemos que no es null
            JLabel lblFotoUsuario = new JLabel();

            if (fotoPerfil != null && fotoPerfil.getImage() != null) {
            	Image imagenEscalada = fotoPerfil.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblFotoUsuario.setIcon(new ImageIcon(imagenEscalada));
            }

            lblFotoUsuario.setText(" " + usuarioActual.getNombre());
            lblFotoUsuario.setHorizontalTextPosition(SwingConstants.RIGHT); // Icono a la izquierda, texto a la derecha
            lblFotoUsuario.setIconTextGap(10); // Espacio entre icono y texto
            lblFotoUsuario.setForeground(Color.BLACK); // Opcional: color del texto
            lblFotoUsuario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));


            panelNorte.add(lblFotoUsuario);
        }
       
    }


	/**
	 * Método para crear un botón con texto y ActionListener.
	 * 
	 * @param texto el texto del botón
	 * @param al    el ActionListener a asociar al botón
	 * @return el botón creado
	 */
    private JButton crearBoton(String texto, ActionListener al) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(234, 158, 66));
        btn.addActionListener(al);
        return btn;
    }

    /**
     * Método para configurar el panel de chat.
     */
    private void configurarPanelChat() {
        JPanel panelChatActual = new JPanel(new BorderLayout());
        this.getContentPane().add(panelChatActual, BorderLayout.CENTER);

        chat = new Chat();
        chat.setBackground(naranjaClaro);
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        chat.setPreferredSize(new Dimension(400, 700));

        scrollBarChat = new JScrollPane(chat);
        scrollBarChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollBarChat.getViewport().setBackground(naranjaClaro);

        panelChatActual.add(scrollBarChat, BorderLayout.CENTER);
        panelChatActual.add(crearPanelMensajesSur(), BorderLayout.SOUTH);
    }

	/**
	 * Método para crear el panel de mensajes en la parte sur.
	 * 
	 * @return el panel de mensajes creado
	 */
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

     // Botón de emojis
        JButton btnEmoji = new JButton("");
        btnEmoji.setBackground(new Color(234, 158, 66));
        btnEmoji.setIcon(new ImageIcon(VentanaMain.class.getResource("/emoji.png"))); // tu icono de emoji

        // Crear menú emergente
        JPopupMenu menuEmojis = new JPopupMenu();
        JPanel panelEmojis = new JPanel(new GridLayout(4, 9, 5, 5)); // 4 filas x 9 columnas, con separación de 5px

        // Añadir los botones de los emoticonos
        for (int i = 0; i <= BubbleText.MAXICONO; i++) {
            JButton botonEmoji = new JButton();
            botonEmoji.setIcon(BubbleText.getEmoji(i));
            botonEmoji.setBorderPainted(false); // Sin borde de botón
            botonEmoji.setContentAreaFilled(false); // Sin fondo de botón
            botonEmoji.setFocusPainted(false); // Que no se pinte al hacer click
            final int idEmoticono = i; // Necesario para el listener

            botonEmoji.addActionListener(e -> {
                enviarIcono(idEmoticono); // Envía el emoticono
                menuEmojis.setVisible(false); // Cierra el menú después de seleccionar
            });

            panelEmojis.add(botonEmoji);
        }

        // Añadir el panel cuadrado al menú emergente
        menuEmojis.add(panelEmojis);

        // Acción al pulsar el botón de emoji
        btnEmoji.addActionListener(e -> menuEmojis.show(btnEmoji, 0, btnEmoji.getHeight()));

        // Añadir el botón al panel sur
        panelMensajesSur.add(btnEmoji, BorderLayout.WEST);


        JButton btnEnviar = new JButton("");
        btnEnviar.setBackground(new Color(234, 158, 66));
        btnEnviar.setIcon(new ImageIcon(VentanaMain.class.getResource("/enviar.png")));
        btnEnviar.addActionListener(e -> enviarMensaje(txtMensaje));

        panelMensajesSur.add(btnEnviar, BorderLayout.EAST);

        return panelMensajesSur;
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //							METODOS										  //
    ////////////////////////////////////////////////////////////////////////////
    
	/**
	 * Método para cargar el chat de un contacto específico.
	 * 
	 * @param contacto a cargar
	 */
    public void cargarChat(Contacto contacto) {
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

	/**
	 * Método para enviar un mensaje al contacto actual.
	 * 
	 * @param textField campo de texto donde se escribe el mensaje
	 */
    private void enviarMensaje(JTextArea textField) {
        if (contactoActual == null || textField == null) return;

        String texto = textField.getText().trim();
        if (texto.isEmpty()) return;

        
        Controlador.getInstancia().enviarMensaje(texto, contactoActual);

        // Obtener el chat real asociado al contacto
        Chat chatActual = chatsRecientes.get(contactoActual);
        if (chatActual == null) {
            chatActual = crearChat();
            chatsRecientes.put(contactoActual, chatActual);
        }

        // Crear y agregar la burbuja al panel de chat correspondiente
        BubbleText burbuja = new BubbleText(chatActual, texto, new Color(159, 213, 192), "Tú", BubbleText.SENT, 12);
        chatActual.add(burbuja);

        // Mostrar ese chat en pantalla
        scrollBarChat.setViewportView(chatActual);

        // Limpiar campo de texto
        textField.setText(null);

        // Refrescar la vista
        chatActual.revalidate();
        chatActual.repaint();

        // Scroll automático al final
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollBarChat.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });

        // Actualizar lista de contactos
        actualizarListaContactos();
    }
    
    private void abrirVentanaAgregarContacto(Contacto contacto) {
        DefaultListModel<Contacto> modelo = new DefaultListModel<>();
        VentanaContacto ventana = new VentanaContacto(modelo);
        ventana.setVisible(true);
    }


    
	/**
	 * Método para enviar un emoticono al contacto actual.
	 * 
	 * @param idEmoticono identificador del emoticono a enviar
	 */
    private void enviarIcono(int idEmoticono) {
        if (contactoActual == null) return;

        Controlador.getInstancia().enviarMensaje(idEmoticono, contactoActual);

        BubbleText burbuja = new BubbleText(chat, idEmoticono, new Color(159, 213, 192), "Tú", BubbleText.SENT, 18);
        chat.add(burbuja);

        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollBarChat.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });

        actualizarListaContactos();
    }


	/**
	 * Método para crear un nuevo chat.
	 * 
	 * @return el nuevo chat creado
	 */
    private Chat crearChat() {
        Chat nuevoChat = new Chat();
        nuevoChat.setBackground(naranjaClaro);
        nuevoChat.setLayout(new BoxLayout(nuevoChat, BoxLayout.Y_AXIS));
        nuevoChat.setSize(400, 700);
        scrollBarChat.getViewport().setBackground(naranjaClaro);
        return nuevoChat;
    }

	/**
	 * Método para crear una burbuja de texto.
	 * 
	 * @param m Mensaje a mostrar en la burbuja
	 * @return la burbuja de texto creada
	 */
    private BubbleText crearBubble(Mensaje m) {
        String emisor;
        int direccionMensaje;
        Color colorBurbuja;

        if (m.getEmisor().equals(Controlador.getInstancia().getUsuarioActual())) {
            emisor = "You";
            direccionMensaje = BubbleText.SENT;
            colorBurbuja = turquesa;
            
        } else {
            Optional<ContactoIndividual> contactoOpcional = Controlador.getInstancia().getContactoDelUsuarioActual(m.getEmisor());
            emisor = contactoOpcional.map(ContactoIndividual::getNombre).orElse("Desconocido");
            direccionMensaje = BubbleText.RECEIVED;
            colorBurbuja = turquesaOscuro;
        }

        if (m.getTexto().isEmpty()) {
            return new BubbleText(chat, m.getEmoticono(), colorBurbuja, emisor, direccionMensaje, 12);
        }
        return new BubbleText(chat, m.getTexto(), colorBurbuja, emisor, direccionMensaje, 12);
    }

	/**
	 * Método para actualizar la lista de contactos en la interfaz.
	 */
    public void actualizarListaContactos() {
        List<Contacto> contactos = Optional.ofNullable(Controlador.getInstancia().getContactosUsuarioActual())
                .orElse(new LinkedList<>());

        DefaultListModel<Contacto> nuevoModelo = new DefaultListModel<>();
        contactos.forEach(nuevoModelo::addElement);
        listaChatRecientes.setModel(nuevoModelo);
    }
    
	/**
	 * Método para abrir un chat con un número de teléfono desde el combo box.
	 */
    private void abrirChatConNumeroDesdeCombo() {
        String telefono = ((String) comboPanelRecientes.getEditor().getItem()).trim();

        if (telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce un número de teléfono.");
            return;
        }

        Usuario actual = Controlador.getInstancia().getUsuarioActual();

        // Verificar si ya es contacto
        Optional<ContactoIndividual> existente = actual.getContactosIndividuales().stream()
            .filter(c -> c.getTelefono().equals(telefono))
            .findFirst();

        ContactoIndividual contacto;
        if (existente.isPresent()) {
            contacto = existente.get();
        } else {
            // Buscar el usuario al que corresponde el teléfono
            Optional<Usuario> usuarioDestino = Controlador.getInstancia().getRepoUsuarios().buscarUsuario(telefono);
            if (usuarioDestino.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No existe ningún usuario con ese número.");
                return;
            }

            // Crear contacto real y persistente
            contacto = Controlador.getInstancia().crearContacto(telefono, telefono); // nombre = telefono

            if (contacto == null) {
                JOptionPane.showMessageDialog(this, "No se pudo crear el contacto.");
                return;
            }

            // Actualizar lista de contactos en pantalla
            actualizarListaContactos();
        }

        // Cargar chat en pantalla
        cargarChat(contacto);
    }
    
    public void cargarChatDesdeExterno(Contacto contacto, Mensaje mensajeBuscado) {
        // Cargar el chat normalmente
        cargarChat(contacto);

        // Hacer scroll hasta el final del chat (donde probablemente esté el mensaje)
        SwingUtilities.invokeLater(() -> {
            JScrollBar barra = getScrollChat();
            barra.setValue(barra.getMaximum());
        });

        // Mensaje informativo
        JOptionPane.showMessageDialog(this,
                "Mostrando mensajes del contacto: " + contacto.getNombre() +
                "\n(No se puede resaltar visualmente el mensaje por limitaciones de la vista)",
                "Mensaje encontrado",
                JOptionPane.INFORMATION_MESSAGE);
    }


    public JScrollBar getScrollChat() {
        return scrollBarChat.getVerticalScrollBar();
    }


    
    public List<Mensaje> getMensajesFiltradosPorTexto(Contacto contacto, String fragmento) {
        List<Mensaje> mensajes = Controlador.getInstancia().getMensajes(contacto);
        if (fragmento == null || fragmento.trim().isEmpty()) return mensajes;

        String f = fragmento.toLowerCase();
        return mensajes.stream()
                       .filter(m -> m.getTexto() != null && m.getTexto().toLowerCase().contains(f))
                       .collect(Collectors.toList());
    }

    
    




}
