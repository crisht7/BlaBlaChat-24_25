package vista;



import javax.swing.*;
import java.awt.*;
import java.util.List;
import controlador.Controlador;
import appChat.ContactoIndividual;

@SuppressWarnings("serial")
public class VentanaGrupo extends JDialog {

    private JTextField txtNombreGrupo;
    private JList<ContactoIndividual> listaContactos;
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
    private final Color boton = Colores.NARANJA_BOTON.getColor();

 

    public VentanaGrupo(JFrame parent) {
        super(parent, "Crear Nuevo Grupo", true);
        getContentPane().setBackground(naranjaOscuro);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout(10, 10));

        // Panel superior: Nombre del grupo
        JPanel panelNombre = new JPanel(new BorderLayout(5, 5));
        panelNombre.setBackground(naranjaOscuro); 
        panelNombre.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        panelNombre.add(new JLabel("Nombre del grupo:"), BorderLayout.NORTH);

        txtNombreGrupo = new JTextField();
        txtNombreGrupo.setBackground(naranjaClaro);
        txtNombreGrupo.setPreferredSize(new Dimension(200, 25));
        panelNombre.add(txtNombreGrupo, BorderLayout.CENTER);

        getContentPane().add(panelNombre, BorderLayout.NORTH);

        // Panel centro: Lista de contactos
        listaContactos = new JList<>(new DefaultListModel<>());
        listaContactos.setBackground(naranjaClaro);
        cargarContactos();
        listaContactos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaContactos);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Selecciona los contactos"));
        scrollLista.setBackground(naranjaOscuro);
        getContentPane().add(scrollLista, BorderLayout.CENTER);

        // Panel inferior: Botón crear grupo
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(naranjaOscuro);
        JButton btnCrearGrupo = new JButton("Crear Grupo");
        btnCrearGrupo.setBackground(boton);
        btnCrearGrupo.addActionListener(e -> crearGrupo());
        panelBoton.add(btnCrearGrupo);
        getContentPane().add(panelBoton, BorderLayout.SOUTH);
    }

    private void cargarContactos() {
        DefaultListModel<ContactoIndividual> model = (DefaultListModel<ContactoIndividual>) listaContactos.getModel();
        List<ContactoIndividual> contactos = Controlador.getInstancia()
                .getContactosUsuarioActual().stream()
                .filter(c -> c instanceof ContactoIndividual)
                .map(c -> (ContactoIndividual) c)
                .toList();
        for (ContactoIndividual c : contactos) {
            model.addElement(c);
        }
    }

    private void crearGrupo() {
        String nombreGrupo = txtNombreGrupo.getText().trim();
        if (nombreGrupo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del grupo no puede estar vacío.");
            return;
        }

        List<ContactoIndividual> contactosSeleccionados = listaContactos.getSelectedValuesList();
        if (contactosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un contacto.");
            return;
        }

        Controlador.getInstancia().crearGrupo(nombreGrupo, contactosSeleccionados);

        VentanaMain.getInstancia().actualizarListaContactos();

        JOptionPane.showMessageDialog(this, "Grupo creado exitosamente.");
        dispose();
    }
}
