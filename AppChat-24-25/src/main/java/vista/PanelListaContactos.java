package vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import appChat.Contacto;
import controlador.Controlador;

public class PanelListaContactos extends JPanel {
    private JList<Contacto> listaContactos;
    private ModeloListaContactos modeloContactos;

    public PanelListaContactos() {
        setLayout(new BorderLayout());

        // Inicializa el modelo correctamente
        modeloContactos = new ModeloListaContactos(Controlador.getInstancia().getContactosUsuarioActual());
        listaContactos = new JList<>(modeloContactos);
        listaContactos.setCellRenderer(new RenderizadorListaContactos());

        JScrollPane scrollPane = new JScrollPane(listaContactos);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void actualizarLista(List<Contacto> contactos) {
        modeloContactos.actualizarDatos(contactos);
    }
}
