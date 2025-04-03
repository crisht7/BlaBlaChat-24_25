package vista;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import appChat.Contacto;
import appChat.ContactoIndividual;
import appChat.Mensaje;
import controlador.Controlador;

public class RenderizadorListaContactos extends JPanel implements ListCellRenderer<Contacto> {
    private JLabel nombreLabel;
    private JLabel telefonoLabel;
    private JLabel mensajePreviewLabel;
    private JLabel iconoLabel;

    public RenderizadorListaContactos() {
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
                telefonoLabel.setText(((ContactoIndividual) contacto).getTelefono());
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

            try {
                URL iconURL = getClass().getResource("/iconos/contacto.png");
                if (iconURL != null) {
                    ImageIcon icono = new ImageIcon(iconURL);
                    Image imagenEscalada = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    iconoLabel.setIcon(new ImageIcon(imagenEscalada));
                } else {
                    iconoLabel.setIcon(null);
                    System.err.println("⚠️ Icono '/iconos/contacto.png' no encontrado.");
                }
            } catch (Exception ex) {
                System.err.println("❌ Error cargando icono: " + ex.getMessage());
                iconoLabel.setIcon(null);
            }

            setBackground(isSelected ? new Color(173, 216, 230) : Color.WHITE);
        }

        return this;
    }
}
