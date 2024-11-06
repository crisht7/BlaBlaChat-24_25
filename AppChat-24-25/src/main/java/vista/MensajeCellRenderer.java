package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import controlador.ControladorAppChat;
import modelo.Mensaje;

//Sublclase para renderizar los mensajes en la vista main
public class MensajeCellRenderer extends JPanel 
		implements ListCellRenderer<Mensaje> {
	private JLabel usuarioLabel;
	private JLabel imagenLabel;
	private JLabel textoLabel;
	private JPanel panelCentro;

	public MensajeCellRenderer() {
		setLayout(new BorderLayout(5, 5));

		usuarioLabel = new JLabel();
		imagenLabel = new JLabel();
		textoLabel = new JLabel();
		panelCentro = new JPanel();
		panelCentro.setLayout(new BorderLayout(2,2));	
		
		panelCentro.add(usuarioLabel, BorderLayout.NORTH);
		panelCentro.add(textoLabel, BorderLayout.CENTER);
		add(imagenLabel, BorderLayout.WEST);
		add(panelCentro, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje mensaje, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Set the text
		textoLabel.setText(mensaje.getTexto());
		String usuario = "";
		if (mensaje.getEmisor().getNombre().equals(ControladorAppChat.getUsuarioActual())) {
			usuario = mensaje.getReceptor().getNombre();
		} else {
			usuario = mensaje.getEmisor().getNombre();
		}

		// Load the image from a random URL (for example, using "https://robohash.org")
		try {
			URL imageUrl = new URL("https://robohash.org/" + mensaje.getNombre() + "?size=50x50");
			Image image = ImageIO.read(imageUrl);
			ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			imageLabel.setIcon(imageIcon);
		} catch (IOException e) {
			e.printStackTrace();
			imageLabel.setIcon(null); // Default to no image if there was an issue
		}

		// Set background and foreground based on selection
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}
}