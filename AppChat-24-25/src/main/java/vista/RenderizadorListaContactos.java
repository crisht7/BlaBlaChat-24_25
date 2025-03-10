package vista;

import javax.swing.*;
import java.awt.*;
import appChat.Contacto;
import appChat.ContactoIndividual;

public class RenderizadorListaContactos extends JPanel implements ListCellRenderer<Contacto> {
    private JLabel nombreLabel;
    private JLabel telefonoLabel;
    private JLabel iconoLabel;

    public RenderizadorListaContactos() {
        setLayout(new BorderLayout(5, 5));
        iconoLabel = new JLabel();
        nombreLabel = new JLabel();
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        telefonoLabel = new JLabel();
        telefonoLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.add(nombreLabel);
        panelTexto.add(telefonoLabel);

        add(iconoLabel, BorderLayout.WEST);
        add(panelTexto, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> list,
            Contacto contacto, int index, boolean isSelected, boolean cellHasFocus) {
    	if (contacto != null) {
    		nombreLabel.setText(contacto.getNombre());
    		// Si es un ContactoIndividual, muestra su teléfono
    		if (contacto instanceof ContactoIndividual) {
    			telefonoLabel.setText(((ContactoIndividual) contacto).getTelefono());
    		} else {
    			telefonoLabel.setText(""); // Si es un grupo u otro tipo de contacto
    		}

    		ImageIcon icono = new ImageIcon(getClass().getResource("/iconos/contacto.png")); 
    		Image imagenEscalada = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
    		iconoLabel.setIcon(new ImageIcon(imagenEscalada));
    		
    		setBackground(isSelected ? new Color(173, 216, 230) : Color.WHITE);
    	}
	return this;
}

}
