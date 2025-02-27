package vista;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import appChat.Contacto;
import appChat.Mensaje;
import controlador.Controlador;
import tds.BubbleText;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class VentanaMain {

    public JFrame frame;
    
    public Chat chat;
    
	private Map<Contacto, Chat> chatsRecientes;

	private JScrollPane scrollBarChat;

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
     * Cargar los mensajes recientes en la lista de chats
     * 
     * @param contacto cuyo chat se debe cargar
     */
     
	private void cargarMensajesRecientes(Contacto contacto) {
    	 if(contacto == null)
    		 return;
    	 
    	 chat = chatsRecientes.get(contacto);
    	 if (chat == null) {
 			chat = new Chat();
 			chat.setBackground(new Color(241, 192, 133));
 			scrollBarChat.setViewportView(chat);
 			chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
 			chat.setSize(400, 700);
 			scrollBarChat.getViewport().setBackground(new Color(159, 213, 192));
 			// Coloca las burbujas en el panel nuevo
 			Controlador.getInstancia().getMensajesUsuario(contacto).stream().map(m -> {
 				String emisor;
 				int direccionMensaje;
 				Color colorBurbuja;
 				if (m.getEmisor().equals(Controlador.getInstancia().getUsuarioActual())) {
 					colorBurbuja = new Color(159, 213, 192);
 					emisor = "You";
 					direccionMensaje = BubbleText.SENT;
 				} else {
 					colorBurbuja = new Color(16, 154, 137);
 					emisor = Controlador.getInstancia().getContactoDelUsuarioActual(m.getEmisor()).get().getNombre();
 					direccionMensaje = BubbleText.RECEIVED;
 				}

 				if (m.getTexto().isEmpty()) {
 					return new BubbleText(chat, m.getEmoticono(), colorBurbuja, emisor, direccionMensaje, 12);
 				}
 				return new BubbleText(chat, m.getTexto(), colorBurbuja, emisor, direccionMensaje, 12); //12 REFIERE AL TAMAÑO 
 			}).forEach(b -> chat.add(b));

 			// Si no hay espacio en los chats recientes es necesario borrar uno
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
 		// Asegurarse de que el scroll esté al final utilizando invokeLater
 		SwingUtilities.invokeLater(() -> {
 			JScrollBar vertical = scrollBarChat.getVerticalScrollBar();
 			vertical.setValue(vertical.getMaximum());
 		});
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
        JPanel panelRecientes = new JPanel(new BorderLayout());
        panelRecientes.setPreferredSize(new Dimension(250, 0));
        frame.getContentPane().add(panelRecientes, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Chats Recientes", JLabel.CENTER);
        panelRecientes.add(lblTitulo, BorderLayout.NORTH);

        // JList para mensajes recientes con MensajeCellRenderer
        JList<Mensaje> listaChatRecientes = new JList<>();
        listaChatRecientes.setBackground(new Color(236, 163, 96));
        listaChatRecientes.setVisibleRowCount(16);
        
        
        
     // Obtener mensajes recientes desde el controlador
        //TODO: Cambiar null por el usuario actual
        List<Mensaje> mensajes = Controlador.getInstancia().getMensajesUsuarioActual();
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
        panelNorte.setBackground(new Color(242, 190, 142));
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

        JButton btnContactos = new JButton("Contactos");
        btnContactos.setBackground(new Color(234, 158, 66));
        btnContactos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		VentanaContacto ventanaContactos = new VentanaContacto(frame);
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
        frame.getContentPane().add(panelChatActual, BorderLayout.CENTER);
        panelChatActual.setLayout(new BorderLayout(0, 0));

        // Panel para mensajes enviados/recibidos
        JPanel chat = new JPanel();
        chat.setBackground(new Color(241, 192, 133));
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        chat.setSize(400, 700);
        chat.setMinimumSize(new Dimension(400, 700));
        chat.setMaximumSize(new Dimension(400, 700));
        chat.setPreferredSize(new Dimension(400, 700));

        // Mensajes de ejemplo para el chat actual
        BubbleText burbuja1 = new BubbleText(chat, "Hola grupo!!", Color.getColor("turquesa", new Color(159, 213, 192)), "J.Ramón", BubbleText.SENT);
        chat.add(burbuja1);

        BubbleText burbuja2 = new BubbleText(chat,
                "Hola, ¿Está seguro de que la burbuja usa varias líneas si es necesario?", Color.getColor("turquesa oscuro", new Color(16, 154, 137)), "Alumno",
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
