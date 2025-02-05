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

import appChat.Mensaje;
import controlador.Controlador;

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
	public Component getListCellRendererComponent(
	    JList<? extends Mensaje> list, Mensaje mensaje, int index,
	    boolean isSelected, boolean cellHasFocus) {

	    // Comprobar si el mensaje es nulo
	    if (mensaje == null) {
	        usuarioLabel.setText("Desconocido");
	        textoLabel.setText("");
	        imagenLabel.setIcon(null);
	        return this;
	    }

	    // Establecer el texto del mensaje
	    textoLabel.setText(mensaje.getTexto());

	    String usuario = "Desconocido"; // Valor predeterminado si no hay datos
	    if (mensaje.getEmisor() != null && mensaje.getReceptor() != null) {
	        // Determinar si el usuario actual es emisor o receptor
	        if (mensaje.getEmisor().getNombre().equals(Controlador.getUsuarioActual())) {
	            usuario = mensaje.getReceptor().getNombre();
	        } else {
	            usuario = mensaje.getEmisor().getNombre();
	        }
	    }
	    usuarioLabel.setText(usuario);

	    // Cargar la imagen del usuario
	    try {
	        URL imageUrl = new URL("https://robohash.org/" + usuario + "?size=50x50");
	        Image image = ImageIO.read(imageUrl);
	        ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	        imagenLabel.setIcon(imageIcon);
	    } catch (IOException e) {
	        e.printStackTrace();
	        imagenLabel.setIcon(null); // Icono por defecto en caso de error
	    }

	    // Cambiar colores según la selección
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