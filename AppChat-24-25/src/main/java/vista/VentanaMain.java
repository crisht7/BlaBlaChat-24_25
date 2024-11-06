package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controlador.ControladorAppChat;
import modelo.Mensaje;
import tds.BubbleText;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;

import java.util.List;
public class VentanaMain {

	JFrame frame;

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
		frame.setBounds(100, 100, 773, 433);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		List list = new List();
		panel.add(list, BorderLayout.NORTH);
		
		JList<Mensaje> listaChatRecientes = new JList<Mensaje>();
		listaChatRecientes.setCellRenderer(new MensajeCellRenderer());
		
		List<Mensaje> mensajes = ControladorAppChat.getMensajesRecientesPorUsuario();
		DefaultListModel<Mensaje> model = new DefaultListModel<Mensaje>();
		for (Mensaje mensaje : mensajes) {
            model.addElement(mensaje);
        }
		listaChatRecientes.setModel(model);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JComboBox comboPanelRecientes = new JComboBox();
		comboPanelRecientes.setEditable(true);
		panel_1.add(comboPanelRecientes);
		
		JButton btnEnviarMensaje = new JButton("");
		panel_1.add(btnEnviarMensaje);
		
		JButton btnBuscarMensaje = new JButton("");
		panel_1.add(btnBuscarMensaje);
		
		JButton btnContactos = new JButton("Contactos");
		panel_1.add(btnContactos);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue);
		
		JButton btnPremium = new JButton("Premium");
		panel_1.add(btnPremium);
		
		JLabel lblFoto = new JLabel("Usuario");
		panel_1.add(lblFoto);
		
		JPanel panelChatActual = new JPanel();
		frame.getContentPane().add(panelChatActual, BorderLayout.CENTER);
		panelChatActual.setLayout(new BorderLayout(0, 0));
		
		JPanel MensajesSur = new JPanel();
		panelChatActual.add(MensajesSur, BorderLayout.SOUTH);
		
		JPanel chat = new JPanel();
		chat.setLayout(new BoxLayout(chat,BoxLayout.Y_AXIS)); 
		chat.setSize(400,700); 
		chat.setMinimumSize(new Dimension(400,700)); 
		chat.setMaximumSize(new Dimension(400,700)); 
		chat.setPreferredSize(new Dimension(400,700)); 
		
		BubbleText burbuja; 
		burbuja=new BubbleText(chat,"Hola grupo!!", Color.GREEN, "J.Ramón", BubbleText.SENT); 
		chat.add(burbuja); 
		
		BubbleText burbuja2; 
		burbuja2=new BubbleText(chat, 
		"Hola, ¿Está seguro de que la burbuja usa varias lineas si es necesario?", 
		Color.LIGHT_GRAY, "Alumno", BubbleText.RECEIVED); 
		chat.add(burbuja2); 
		
		panelChatActual.add(chat, BorderLayout.CENTER);
	}

	public void setVisible(boolean b) {
		
		
	}

}
