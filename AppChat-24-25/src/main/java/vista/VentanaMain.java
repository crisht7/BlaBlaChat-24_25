package vista;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.*;

import appChat.Contacto;
import appChat.ContactoIndividual;
import appChat.Mensaje;
import controlador.Controlador;
import tds.BubbleText;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaMain {

    public JFrame frame;
    
    public Chat chat;
    
    private static VentanaMain instancia; // 🔹 Permite actualizar la lista de contactos dinámicamente

    private PanelListaContactos panelListaContactos;
    
	private Map<Contacto, Chat> chatsRecientes;

	private JScrollPane scrollBarChat;

	// variables para los colores 
	private final Color turquesa = Colores.TURQUESA.getColor();
	private final Color turquesaOscuro = Colores.TURQUESA_OSCURO.getColor();
	private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
	private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();

	
	
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
        chatsRecientes = new HashMap<>(); 
        instancia = this; 
        initialize();
    }


    
    /**
     * Cargar los mensajes recientes en la lista de chats
     * 
     * @param contacto cuyo chat se debe cargar
     */
     
    void cargarMensajesRecientes(Contacto contacto) {

        chat = chatsRecientes.get(contacto);

        if (chat == null) {
            chat = new Chat();
            chat.setBackground(new Color(241, 192, 133));
            scrollBarChat.setViewportView(chat);
            chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
            chat.setSize(400, 700);
            scrollBarChat.getViewport().setBackground(new Color(159, 213, 192));

            // Obtener mensajes del contacto si no es null
            List<Mensaje> mensajes = Controlador.getInstancia().getMensajesUsuario(contacto);
            if (mensajes == null) {
                System.err.println("⚠️ La lista de mensajes del contacto es null.");
                mensajes = new LinkedList<>(); // Evitar fallos al iterar
            }

            mensajes.stream().map(m -> {
                String emisor;
                int direccionMensaje;
                Color colorBurbuja = new Color(159, 213, 192);

                if (m.getEmisor().equals(Controlador.getInstancia().getUsuarioActual())) {
                    colorBurbuja = new Color(159, 213, 192);
                    emisor = "You";
                    direccionMensaje = BubbleText.SENT;
                } else {
                    // 🔹 Obtener el contacto de forma segura con Optional
                    Optional<ContactoIndividual> contactoOpcional = Controlador.getInstancia().getContactoDelUsuarioActual(m.getEmisor());
                    emisor = contactoOpcional.map(ContactoIndividual::getNombre).orElse("Desconocido");
                    direccionMensaje = BubbleText.RECEIVED;
                }

                if (m.getTexto().isEmpty()) {
                    return new BubbleText(chat, m.getEmoticono(), colorBurbuja, emisor, direccionMensaje, 12);
                }
                return new BubbleText(chat, m.getTexto(), colorBurbuja, emisor, direccionMensaje, 12);
            }).forEach(b -> chat.add(b));

            if (chatsRecientes.size() >= 4) {
                chatsRecientes.remove(chatsRecientes.keySet().stream().findFirst().orElse(null));
            }

            chatsRecientes.put(contacto, chat);
        } else {
            chat.setBackground(new Color(241, 192, 133));
            scrollBarChat.setViewportView(chat);
            chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
            chat.setSize(400, 700);
            scrollBarChat.getViewport().setBackground(new Color(241, 192, 133));
        }

        scrollBarChat.setViewportView(chat);

        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollBarChat.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }



    public void actualizarListaContactos() {
        List<Contacto> contactos = Controlador.getInstancia().getContactosUsuarioActual();

        if (contactos == null || contactos.isEmpty()) {
            System.err.println("⚠️ No se encontraron contactos para mostrar en la ventana principal.");
            contactos = new LinkedList<>();
        } else {
            System.out.println("✅ Contactos cargados en la ventana principal: " + contactos.size());
        }

        panelListaContactos.actualizarLista(contactos);
    }


	@SuppressWarnings("serial")
	private static ListCellRenderer<? super Mensaje> crearRendererMensajes() {
		return new DefaultListCellRenderer() {


			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel cellPanel = new JPanel(new BorderLayout(5, 5));
                cellPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                Mensaje mensaje = (Mensaje) value;

                JLabel imageLabel = new JLabel();
                imageLabel.setIcon(new ImageIcon(getClass().getResource("/recursos/usuario.png")));    // foto predeterminada pero podríamos añadir la foto del usuario

                JPanel textPanel = new JPanel(new BorderLayout());
                textPanel.setOpaque(false);

                JLabel nameLabel = new JLabel(mensaje.getEmisor().getNombre());
                JLabel textoLabel = new JLabel(mensaje.getTexto());
                JLabel fechaLabel = new JLabel(mensaje.getHora() + " " );

                nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
                textoLabel.setFont(new Font("Arial", Font.PLAIN, 11));
                fechaLabel.setFont(new Font("Arial", Font.ITALIC, 9));
                fechaLabel.setForeground(Color.DARK_GRAY);

                textPanel.add(nameLabel, BorderLayout.NORTH);
                textPanel.add(textoLabel, BorderLayout.CENTER);
                textPanel.add(fechaLabel, BorderLayout.SOUTH);

                cellPanel.add(imageLabel, BorderLayout.WEST);
                cellPanel.add(textPanel, BorderLayout.CENTER);

                Color turquesaClaro = new Color(175, 238, 238);
                Color turquesaOscuro = new Color(64, 224, 208);

                if (isSelected) {
                    cellPanel.setBackground(turquesaOscuro);
                } else {
                    cellPanel.setBackground(turquesaClaro);
                }

                return cellPanel;
            }
                
                
		};
		
	}
    
    

  
    //############################################################################
    //############################################################################
    //############################################################################
    //############################################################################
    //############################################################################
    //############################################################################
    //############################################################################
    //############################################################################
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 746, 654);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        // Panel para mostrar mensajes recientes
        JPanel panelRecientes = new JPanel(new BorderLayout());
        panelRecientes.setPreferredSize(new Dimension(250, 0));
        frame.getContentPane().add(panelRecientes, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Chats Recientes", JLabel.CENTER);
        panelRecientes.add(lblTitulo, BorderLayout.NORTH);

        panelListaContactos = new PanelListaContactos();
        panelRecientes.add(panelListaContactos, BorderLayout.CENTER);
       
        actualizarListaContactos();
        
        // JList para mensajes recientes con MensajeCellRenderer
        JList<Contacto> listaChatRecientes = new JList<>();
        listaChatRecientes.setCellRenderer(new RenderizadorListaContactos()); // Usa tu renderizador
        listaChatRecientes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Contacto contactoSeleccionado = listaChatRecientes.getSelectedValue();
                if (contactoSeleccionado != null) {
                    cargarMensajesRecientes(contactoSeleccionado);
                }
            }
        });

        	
        
        // Obtener contactos recientes desde el controlador
        List<Contacto> contactos = Controlador.getInstancia().getContactosUsuarioActual();
        DefaultListModel<Contacto> modelContactos = new DefaultListModel<>();

        for (Contacto contacto : contactos) {
            modelContactos.addElement(contacto);
        }

        listaChatRecientes.setBackground(naranjaOscuro);
        listaChatRecientes.setVisibleRowCount(16);
        
        List<Mensaje> mensajes = Controlador.getInstancia().getMensajesUsuarioActual();
        DefaultListModel<Mensaje> model = new DefaultListModel<>();
        for (Mensaje mensaje : mensajes) {
            if (mensaje != null && mensaje.getEmisor() != null && mensaje.getReceptor() != null) {
                model.addElement(mensaje);
            } else {
                System.err.println("Mensaje inválido: " + mensaje);
            }
        }
        listaChatRecientes.setModel(modelContactos);
        listaChatRecientes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Contacto contactoSeleccionado = listaChatRecientes.getSelectedValue();
                if (contactoSeleccionado != null) {
                    cargarMensajesRecientes(contactoSeleccionado);
                }
            }
        });

        
        
        // Agregar la lista a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(listaChatRecientes);
        panelRecientes.add(scrollPane, BorderLayout.CENTER);

        
        // Panel superior para botones y opciones
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(naranjaClaro);
        frame.getContentPane().add(panelNorte, BorderLayout.NORTH);
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));

        @SuppressWarnings("rawtypes")
        JComboBox comboPanelRecientes = new JComboBox();
        comboPanelRecientes.setBackground(new Color(234, 158, 66));
        comboPanelRecientes.setEditable(true);
        panelNorte.add(comboPanelRecientes);

        JButton btnBuscarMensaje = new JButton("Buscar");
        btnBuscarMensaje.setBackground(new Color(234, 158, 66));
        btnBuscarMensaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaBuscar ventanaBuscar = new VentanaBuscar(frame); // Crear una instancia de VentanaBuscar
                ventanaBuscar.setVisible(true); 
            }
        });
        
        panelNorte.add(btnBuscarMensaje);
        
		final DefaultListModel<Contacto> modelContacts = new DefaultListModel<>();

        JButton btnContactos = new JButton("Contactos");
        btnContactos.setBackground(new Color(234, 158, 66));
        btnContactos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		VentanaContacto ventanaContactos = new VentanaContacto(modelContacts);
                ventanaContactos.setVisible(true);
        	}
        });
        panelNorte.add(btnContactos);
        
        JButton btnNewButton = new JButton("Tema");
        btnNewButton.setBackground(new Color(234, 158, 66));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// aquí quiero que se inviertan los colores, rollo que el naranja se vuelva azul y eso, para cambiar el tema 
        		// pero ya es mas capricho que otra cosa 
        		
        	}
        });
        
        panelNorte.add(btnNewButton);
        

        Component horizontalGlue = Box.createHorizontalGlue();
        panelNorte.add(horizontalGlue);

        JButton btnPremium = new JButton("Premium");
        btnPremium.setBackground(new Color(159, 213, 192));
        panelNorte.add(btnPremium);

        JLabel lblFoto = new JLabel("Usuario");
        panelNorte.add(lblFoto);

        // Panel principal para el chat actual
        JPanel panelChatActual = new JPanel();
        scrollBarChat = new JScrollPane();
        frame.getContentPane().add(scrollBarChat, BorderLayout.CENTER);
        frame.getContentPane().add(panelChatActual, BorderLayout.CENTER);
        panelChatActual.setLayout(new BorderLayout(0, 0));

        // Panel para mensajes enviados/recibidos
        JPanel chat = new JPanel();
        chat.setBackground(naranjaClaro);
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        chat.setSize(400, 700);
        chat.setMinimumSize(new Dimension(400, 700));
        chat.setMaximumSize(new Dimension(400, 700));
        chat.setPreferredSize(new Dimension(400, 700));

        // Mensajes de ejemplo para el chat actual
        BubbleText burbuja1 = new BubbleText(chat, "Hola grupo!!", Color.getColor("turquesa", turquesa), "J.Ramón", BubbleText.SENT);
        chat.add(burbuja1);

        BubbleText burbuja2 = new BubbleText(chat,
                "Hola, ¿Está seguro de que la burbuja usa varias líneas si es necesario?", Color.getColor("turquesa oscuro", turquesaOscuro), "Alumno",
                BubbleText.RECEIVED);
        chat.add(burbuja2);

        // Añadir el panel del chat al centro
        panelChatActual.add(chat, BorderLayout.CENTER);

        // Panel inferior para enviar mensajes
        JPanel panelMensajesSur = new JPanel();
        panelMensajesSur.setBackground(new Color(241, 192, 133));
        panelChatActual.add(panelMensajesSur, BorderLayout.SOUTH);
       


        // Crear JTextArea en lugar de JTextField
        JTextArea txtMensaje = new JTextArea(3, 30); // 3 líneas de alto, 30 caracteres de ancho
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);

        // Agregar un JScrollPane para permitir desplazamiento si el mensaje es largo
        JScrollPane scrollMensaje = new JScrollPane(txtMensaje);
        scrollMensaje.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelMensajesSur.add(scrollMensaje, BorderLayout.CENTER);

        
     // Botón de emoji
        JButton btnEmoji = new JButton("");
        btnEmoji.setBackground(new Color(234, 158, 66));
        btnEmoji.setIcon(new ImageIcon(VentanaMain.class.getResource("/recursos/emoticono.png")));
        panelMensajesSur.add(btnEmoji, BorderLayout.WEST);

        // Menú emergente con emojis
        JPopupMenu menuEmojis = new JPopupMenu();
        String[] emojis = {"😀", "😂", "😍", "😎", "😢", "😡", "👍", "🔥", "💯"};
        for (String emoji : emojis) {
            JMenuItem item = new JMenuItem(emoji);
            item.addActionListener(e -> txtMensaje.append(emoji)); // Agregar emoji al mensaje
            menuEmojis.add(item);
        }

        // Mostrar menú cuando se haga clic en el botón de emoji
        btnEmoji.addActionListener(e -> menuEmojis.show(btnEmoji, 0, btnEmoji.getHeight()));

        
        // Botón para enviar mensaje
        JButton btnEnviar = new JButton("");
        btnEnviar.setBackground(new Color(234, 158, 66));
        btnEnviar.setIcon(new ImageIcon(VentanaMain.class.getResource("/recursos/enviar.png")));
        panelMensajesSur.add(btnEnviar, BorderLayout.EAST);

        // Acción para enviar mensaje
        btnEnviar.addActionListener(e -> {
            String texto = txtMensaje.getText().trim();
            if (!texto.isEmpty()) {
            	BubbleText mensajeEnviado = new BubbleText(chat, texto, Color.getColor("turquesa", new Color(159, 213, 192)), "Tú", BubbleText.SENT); // en tú imagino que pondremos usuario.getNombre 
                chat.add(mensajeEnviado);
                txtMensaje.setText(""); // Vaciar el campo después de enviar
            }
        });
    
    }

 
}
