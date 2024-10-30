package tds.swing.componentes;

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

public class PersonaCellRenderer extends JPanel implements ListCellRenderer<Persona> {
	private JLabel nameLabel;
	private JLabel imageLabel;

	public PersonaCellRenderer() {
		setLayout(new BorderLayout(5, 5));

		nameLabel = new JLabel();
		imageLabel = new JLabel();

		add(imageLabel, BorderLayout.WEST);
		add(nameLabel, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Persona> list, Persona persona, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Set the text
		nameLabel.setText(persona.toString());

		// Load the image from a random URL (for example, using "https://robohash.org")
		try {
			URL imageUrl = new URL("https://robohash.org/" + persona.getNombre() + "?size=100x100");
			Image image = ImageIO.read(imageUrl);
			ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
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