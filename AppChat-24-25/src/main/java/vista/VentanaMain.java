package vista;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import javax.swing.*;

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

    private final Color turquesa = Colores.TURQUESA.getColor();
    private final Color turquesaOscuro = Colores.TURQUESA_OSCURO.getColor();
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
    
    private JLabel lblFotoUsuario;


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaMain.getInstancia().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private VentanaMain() {
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
        if (instancia == null) {
            instancia = new VentanaMain();
        }
        return instancia;
    }

    
    public void refrescarFotoUsuario() {
        ImageIcon nuevaFoto = Controlador.getInstancia().getUsuarioActual().getFotoPerfil();
        if (nuevaFoto != null) {
            Image imagenEscalada = nuevaFoto.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            lblFotoUsuario.setIcon(new ImageIcon(imagenEscalada));
        } else {
            lblFotoUsuario.setIcon(new ImageIcon(getClass().getResource("/anonimo.png")));
        }
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

            // Icono del contacto
            iconoLabel = new JLabel();

            // Panel de texto
            nombreLabel = new JLabel();
            nombreLabel.setFont(new Font("Arial", Font.BOLD, 14));

            telefonoLabel = new JLabel();
            telefonoLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            mensajePreviewLabel = new JLabel();
            mensajePreviewLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            mensajePreviewLabel.setForeground(Color.DARK_GRAY);

            JPanel panelTexto = new JPanel(new GridLayout(3, 1));
            panelTexto.setOpaque(false);
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

                    ImageIcon fotoPerfil = cIndividual.getFoto();
                    if (fotoPerfil != null && fotoPerfil.getImage() != null) {
                        Image imagenEscalada = fotoPerfil.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        iconoLabel.setIcon(new ImageIcon(imagenEscalada));
                    } else {
                        iconoLabel.setIcon(null);
                    }
                } else if (contacto instanceof Grupo) {
                    Grupo grupo = (Grupo) contacto;
                    telefonoLabel.setText("");

                    ImageIcon fotoGrupo = grupo.getFoto();
                    if (fotoGrupo != null && fotoGrupo.getImage() != null) {
                        Image imagenEscalada = fotoGrupo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        iconoLabel.setIcon(new ImageIcon(imagenEscalada));
                    } else {
                        iconoLabel.setIcon(null);
                    }
                } else {
                    telefonoLabel.setText("");
                    iconoLabel.setIcon(null);
                }

                // Último mensaje
                List<Mensaje> mensajes = Controlador.getInstancia().getMensajes(contacto);
                if (!mensajes.isEmpty()) {
                    Mensaje ultimo = mensajes.get(mensajes.size() - 1);
                    String texto = ultimo.getTexto().isEmpty() ? "[Emoticono]" : ultimo.getTexto();
                    mensajePreviewLabel.setText("Último: " + texto);
                } else {
                    mensajePreviewLabel.setText("Sin mensajes");
                }

                // Colores y opacidad
                Color fondo = isSelected ? naranjaClaro : naranjaOscuro;
                setBackground(fondo);
                nombreLabel.setBackground(fondo);
                telefonoLabel.setBackground(fondo);
                mensajePreviewLabel.setBackground(fondo);
                iconoLabel.setBackground(fondo);

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
        this.setBounds(100, 100, 900, 700);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/chat150.png")));
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
        
        listaChatRecientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int index = listaChatRecientes.locationToIndex(e.getPoint());
                if (index == -1) return;

                Contacto posible = listaChatRecientes.getModel().getElementAt(index);
                if (!(posible instanceof ContactoIndividual)) return;

                ContactoIndividual ci = (ContactoIndividual) posible;

                boolean noAgregado = Controlador.getInstancia()
                    .getUsuarioActual()
                    .getContactosIndividuales()
                    .stream()
                    .noneMatch(c -> c.getTelefono().equals(ci.getTelefono()));

                Rectangle cellBounds = listaChatRecientes.getCellBounds(index, index);
                Point clickPoint = e.getPoint();

                // Si se hace clic en el área derecha (donde está el "+"), activamos
                if (noAgregado && clickPoint.getX() > (cellBounds.getMaxX() - 35)) {
                    int confirm = JOptionPane.showConfirmDialog(
                        VentanaMain.this,
                        "¿Deseas agregar a " + ci.getTelefono() + " como contacto?",
                        "Agregar contacto",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        String nombre = JOptionPane.showInputDialog(VentanaMain.this, "Nombre para el contacto:");
                        if (nombre != null && !nombre.trim().isEmpty()) {
                            Controlador.getInstancia().crearContacto(nombre.trim(), ci.getTelefono());
                            actualizarListaContactos();
                        }
                    }
                }
            }
        });


        
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
    
        configurarMenuContextual();
    }
    
    
	/**
	 * Método para mostrar la información de un contacto.
	 */
    public void mostrarInfoContacto(ContactoIndividual contacto) {
        JDialog dialogo = new JDialog(this, "Información de contacto", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(300, 320);
        dialogo.setResizable(false);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(naranjaOscuro); 
        
        // Imagen
        JLabel lblImagen = new JLabel();
        ImageIcon foto = contacto.getFoto();
        if (foto != null) {
            Image img = foto.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(img));
        }
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre: " + contacto.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono: " + contacto.getTelefono());
        lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTelefono.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Obtener saludo desde el Usuario real
        Usuario usuarioContacto = Controlador.getInstancia().getUsuarioPorTelefono(contacto.getTelefono());
        String saludo = (usuarioContacto != null && usuarioContacto.getSaludo() != null)
            ? usuarioContacto.getSaludo()
            : "Sin saludo";

        // Saludo separado en dos etiquetas centradas
        JLabel lblTituloSaludo = new JLabel("Saludo:");
        lblTituloSaludo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTituloSaludo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTextoSaludo = new JLabel("<html><i>" + saludo + "</i></html>");
        lblTextoSaludo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTextoSaludo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Añadir componentes
        panel.add(lblImagen);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblTelefono);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblTituloSaludo);
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        panel.add(lblTextoSaludo);

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.setVisible(true);
    }



	/**
	 * Método para configurar el panel norte de la ventana.
	 */
    private void configurarPanelNorte() {
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(naranjaClaro);
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
        this.getContentPane().add(panelNorte, BorderLayout.NORTH);

        JTextField campoTelefono = new JTextField();
        Dimension tamañoFijo = new Dimension(100, 30); // Ajusta el tamaño a tu gusto

        campoTelefono.setPreferredSize(tamañoFijo);
        campoTelefono.setMaximumSize(tamañoFijo);
        campoTelefono.setMinimumSize(tamañoFijo);
        campoTelefono.setBackground(new Color(234, 158, 66));
        panelNorte.add(campoTelefono);


        
        panelNorte.add(crearBoton("Ir al chat", e -> {
            String telefono = campoTelefono.getText().trim();
            if (!telefono.isEmpty()) {
                Contacto contacto = Controlador.getInstancia().abrirChatConTelefono(telefono);
                if (contacto != null) {
                    actualizarListaContactos();
                    cargarChat(contacto);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con ese número.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        }));

        // Asegurate de agregar todos los botones:
        panelNorte.add(crearBoton("Buscar", e -> new VentanaBuscar(this).setVisible(true)));
        panelNorte.add(crearBoton("Contactos", e -> new VentanaContacto(new DefaultListModel<>()).setVisible(true)));
        panelNorte.add(crearBoton("Crear Grupo", e -> new VentanaGrupo(this).setVisible(true)));


        panelNorte.add(Box.createHorizontalGlue());

        JButton btnExportarPDF = new JButton("Exportar PDF");
        btnExportarPDF.setBackground(new Color(159, 213, 192));

        btnExportarPDF.addActionListener(e -> {
            Usuario usuarioActual = Controlador.getInstancia().getUsuarioActual();

            if (usuarioActual == null || !usuarioActual.isPremium()) {
                JOptionPane.showMessageDialog(this, "Función disponible solo para usuarios premium.", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (contactoActual == null) {
                JOptionPane.showMessageDialog(this, "No hay ningún chat seleccionado para exportar.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar como PDF");
            fileChooser.setSelectedFile(new java.io.File("chat_exportado.pdf"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                exportarChatAPDF(usuarioActual, contactoActual, fileToSave.getAbsolutePath());
            }
        });

        panelNorte.add(btnExportarPDF);

        
        JButton btnPremium = new JButton("Premium");
        btnPremium.setBackground(new Color(159, 213, 192));

        btnPremium.addActionListener(e -> {
            Usuario usuarioActual = Controlador.getInstancia().getUsuarioActual();

            if (usuarioActual != null && !usuarioActual.isPremium()) {
            	double precioBase = 50.0;

            	double maxDescuento = Controlador.getInstancia().calcularMejorDescuento();
            	double precioFinal = precioBase * (1 - maxDescuento / 100);

                String mensaje = "¿Deseas adquirir la versión Premium?\n\n" +
                        "Precio base: 50 €\n" +
                        (maxDescuento > 0 ?
                            String.format("Descuento aplicado: %.0f%%\n", maxDescuento) :
                            "Sin descuentos aplicables.\n") +
                        String.format("Precio final: %.2f €\n", precioFinal);

                int opcion = JOptionPane.showConfirmDialog(
                        this,
                        mensaje,
                        "Confirmar suscripción Premium",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    usuarioActual.setPremium(true);
                    Controlador.getInstancia().getAdaptadorUsuario().modificarUsuario(usuarioActual);

                    JOptionPane.showMessageDialog(
                            this,
                            "Ahora eres usuario premium ",
                            "Suscripción exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } else if (usuarioActual != null && usuarioActual.isPremium()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ya eres usuario premium 😊",
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
            lblFotoUsuario = new JLabel();
            
            lblFotoUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lblFotoUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    VentanaPerfil ventanaUsuario = new VentanaPerfil(VentanaMain.getInstancia());
                    ventanaUsuario.setVisible(true);
                }
            });


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
        JScrollPane scroll = new JScrollPane(panelEmojis);
        scroll.setPreferredSize(new Dimension(400, 200)); // Ajusta el tamaño máximo visible
        menuEmojis.add(scroll);




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
            emisor = "Tú";
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
    	List<Contacto> contactos = Controlador.getInstancia().obtenerContactosOrdenadosPorUltimoMensaje();

        DefaultListModel<Contacto> nuevoModelo = new DefaultListModel<>();
        contactos.forEach(nuevoModelo::addElement);

        listaChatRecientes.setModel(nuevoModelo);
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


    
    private void exportarChatAPDF(Usuario usuario, Contacto contacto, String rutaArchivo) {
        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(rutaArchivo));
            document.open();

            document.add(new com.itextpdf.text.Paragraph("Historial de Chat entre " + usuario.getNombre() + " y " + contacto.getNombre()));
            document.add(new com.itextpdf.text.Paragraph(" ")); // espacio

            List<Mensaje> mensajes = Controlador.getInstancia().getMensajes(contacto);
            for (Mensaje m : mensajes) {
                String emisor = m.getEmisor().equals(usuario) ? "Tú" : contacto.getNombre();
                String contenido;

                if (m.getTexto().isEmpty()) {
                    contenido = emisor + ": [Emoticono]";
                } else {
                    contenido = emisor + ": " + m.getTexto();
                }

                document.add(new com.itextpdf.text.Paragraph(contenido));
            }

            document.close();

            JOptionPane.showMessageDialog(this, "PDF generado exitosamente en:\n" + rutaArchivo, "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el PDF:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    
   
    private void configurarMenuContextual() {
        JPopupMenu menuContextual = new JPopupMenu();

        JMenuItem itemCambiarNombre = new JMenuItem("Cambiar nombre");
        itemCambiarNombre.addActionListener(e -> {
            Contacto seleccionado = listaChatRecientes.getSelectedValue();
            if (seleccionado instanceof Grupo) return;
            String nuevoNombre = JOptionPane.showInputDialog(this, "Introduce el nuevo nombre:", seleccionado.getNombre());
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                Controlador.getInstancia().renombrarContacto(seleccionado, nuevoNombre.trim());
                actualizarListaContactos();
            }
        });
        menuContextual.add(itemCambiarNombre);

        JMenuItem itemAñadirContactoGrupo = new JMenuItem("Añadir contacto al grupo");
        itemAñadirContactoGrupo.addActionListener(ev -> {
            Contacto seleccionado = listaChatRecientes.getSelectedValue();
            if (!(seleccionado instanceof Grupo)) return;
            Grupo grupo = (Grupo) seleccionado;

            List<ContactoIndividual> disponibles = Controlador.getInstancia().getUsuarioActual()
                .getContactosIndividuales().stream()
                .filter(c -> !grupo.getIntegrantes().contains(c))
                .toList();

            if (disponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay contactos disponibles para añadir.");
                return;
            }

            ContactoIndividual elegido = (ContactoIndividual) JOptionPane.showInputDialog(
                this,
                "Selecciona un contacto para añadir:",
                "Añadir al grupo",
                JOptionPane.PLAIN_MESSAGE,
                null,
                disponibles.toArray(),
                disponibles.get(0)
            );

            if (elegido != null) {
                boolean añadido = Controlador.getInstancia().añadirContactoAGrupo(grupo.getNombre(), elegido);
                if (añadido) {
                    JOptionPane.showMessageDialog(this, "Contacto añadido al grupo.");
                } else {
                    JOptionPane.showMessageDialog(this, "El contacto ya está en el grupo.");
                }
            }
        });
        menuContextual.add(itemAñadirContactoGrupo);
        
        JMenuItem itemAñadirContacto = new JMenuItem("Añadir contacto");
        itemAñadirContacto.addActionListener(ev -> {
            Contacto seleccionado = listaChatRecientes.getSelectedValue();
            if (!(seleccionado instanceof ContactoIndividual)) return;

            ContactoIndividual ci = (ContactoIndividual) seleccionado;

            String nombre = JOptionPane.showInputDialog(
                VentanaMain.this, "Introduce el nombre del nuevo contacto:", ci.getTelefono());
            if (nombre != null && !nombre.trim().isEmpty()) {
                Controlador.getInstancia().crearContacto(nombre.trim(), ci.getTelefono());
                actualizarListaContactos();
            }
        });
        menuContextual.add(itemAñadirContacto);


        listaChatRecientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = listaChatRecientes.locationToIndex(e.getPoint());
                    if (index != -1) {
                        listaChatRecientes.setSelectedIndex(index);
                        Contacto seleccionado = listaChatRecientes.getModel().getElementAt(index);

                        boolean esGrupo = seleccionado instanceof Grupo;
                        boolean esContactoIndividual = seleccionado instanceof ContactoIndividual;
                        boolean noAgregado = false;

                        if (esContactoIndividual) {
                            ContactoIndividual ci = (ContactoIndividual) seleccionado;
                            noAgregado = Controlador.getInstancia()
                                .getUsuarioActual()
                                .getContactosIndividuales()
                                .stream()
                                .noneMatch(c -> c.getTelefono().equals(ci.getTelefono()));
                        }

                        itemCambiarNombre.setVisible(esContactoIndividual && !noAgregado);
                        itemAñadirContacto.setVisible(esContactoIndividual && noAgregado);
                        itemAñadirContactoGrupo.setVisible(esGrupo);
                        	

                        menuContextual.show(listaChatRecientes, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    
    public void actualizarDatosUsuario() {
        Usuario usuarioActual = Controlador.getInstancia().getUsuarioActual();
        if (lblFotoUsuario != null && usuarioActual != null) {
            // Actualizar texto del nombre
            lblFotoUsuario.setText(" " + usuarioActual.getNombre());

            // Actualizar foto de perfil
            ImageIcon fotoPerfil = usuarioActual.getFotoPerfil();
            if (fotoPerfil != null) {
                Image imagenEscalada = fotoPerfil.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblFotoUsuario.setIcon(new ImageIcon(imagenEscalada));
            } else {
                lblFotoUsuario.setIcon(new ImageIcon(getClass().getResource("/anonimo.png")));
            }
        }
    }

    
    public void limpiarEstadoChats() {
        chatsRecientes.clear();
        contactoActual = null;
        chat.removeAll(); // Limpia el panel visual del chat
        chat.revalidate();
        chat.repaint();
        scrollBarChat.setViewportView(chat); // Aplica el cambio visualmente
    }






}
