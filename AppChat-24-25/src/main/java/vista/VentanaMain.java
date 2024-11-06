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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
		
		JPanel panelRecientes = new JPanel();
		frame.getContentPane().add(panelRecientes, BorderLayout.WEST);
		panelRecientes.setLayout(new BorderLayout(0, 0));
		
		
		//List list = new List();
		//panelRecientes.add(list, BorderLayout.NORTH);
		
		JList<Mensaje> listaChatRecientes = new JList<Mensaje>();
		listaChatRecientes.setCellRenderer(new MensajeCellRenderer());
		
		List<Mensaje> mensajes = ControladorAppChat.getMensajesRecientesPorUsuario();
		DefaultListModel<Mensaje> model = new DefaultListModel<Mensaje>();
		for (Mensaje mensaje : mensajes) {
            model.addElement(mensaje);
        }
		listaChatRecientes.setModel(model);
		
		JPanel panelNorte = new JPanel();
		frame.getContentPane().add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
		
		@SuppressWarnings("rawtypes")
		JComboBox comboPanelRecientes = new JComboBox();
		comboPanelRecientes.setEditable(true);
		panelNorte.add(comboPanelRecientes);
		
		JButton btnEnviarMensaje = new JButton("");
		panelNorte.add(btnEnviarMensaje);
		
		JButton btnBuscarMensaje = new JButton("");
		panelNorte.add(btnBuscarMensaje);
		
		JButton btnContactos = new JButton("Contactos");
		btnContactos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panelNorte.add(btnContactos);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panelNorte.add(horizontalGlue);
		
		JButton btnPremium = new JButton("Premium");
		panelNorte.add(btnPremium);
		
		JLabel lblFoto = new JLabel("Usuario");
		panelNorte.add(lblFoto);
		
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
