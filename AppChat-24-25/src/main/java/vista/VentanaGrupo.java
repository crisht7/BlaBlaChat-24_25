package vista;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import controlador.Controlador;
import appChat.ContactoIndividual;
import appChat.Grupo;

/**
 * Ventana para crear un nuevo grupo seleccionando contactos.
 */
@SuppressWarnings("serial")
public class VentanaGrupo extends JDialog {

    // ===================== Constantes de color =====================
    private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
    private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
    private final Color boton = Colores.NARANJA_BOTON.getColor();

    // ===================== Componentes =====================
    private JTextField txtNombreGrupo;
    private JList<ContactoIndividual> listaContactos;
    private ImageIcon imagenSeleccionadaGrupo = null;

    // ===================== Constructor =====================
    public VentanaGrupo(JFrame parent) {
        super(parent, "Crear Nuevo Grupo", true);

        configurarVentana();
        inicializarComponentes();
    }

    // ===================== Métodos de configuración =====================

    private void configurarVentana() {
        getContentPane().setBackground(naranjaOscuro);
        setSize(400, 400);
        setLocationRelativeTo(getParent());
        getContentPane().setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // Panel superior: nombre del grupo
        JPanel panelNombre = new JPanel(new BorderLayout(5, 5));
        panelNombre.setBackground(naranjaOscuro);
        panelNombre.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        panelNombre.add(new JLabel("Nombre del grupo:"), BorderLayout.NORTH);

        txtNombreGrupo = new JTextField();
        txtNombreGrupo.setBackground(naranjaClaro);
        txtNombreGrupo.setPreferredSize(new Dimension(200, 25));
        panelNombre.add(txtNombreGrupo, BorderLayout.CENTER);

        getContentPane().add(panelNombre, BorderLayout.NORTH);

        // Panel centro: lista de contactos
        listaContactos = new JList<>(new DefaultListModel<>());
        listaContactos.setBackground(naranjaClaro);
        cargarContactos();
        listaContactos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollLista = new JScrollPane(listaContactos);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Selecciona los contactos"));
        scrollLista.setBackground(naranjaOscuro);

        getContentPane().add(scrollLista, BorderLayout.CENTER);

        // Panel inferior: botones
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(naranjaOscuro);

        JButton btnCrearGrupo = new JButton("Crear Grupo");
        btnCrearGrupo.setBackground(boton);
        btnCrearGrupo.addActionListener(e -> crearGrupo());
        panelBoton.add(btnCrearGrupo);

        JButton btnSeleccionarImagen = new JButton("Seleccionar imagen");
        btnSeleccionarImagen.setBackground(boton);
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        panelBoton.add(btnSeleccionarImagen);

        getContentPane().add(panelBoton, BorderLayout.SOUTH);
    }

    // ===================== Métodos auxiliares =====================

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

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen del grupo");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            imagenSeleccionadaGrupo = new ImageIcon(archivo.getAbsolutePath());
        }
    }

    private void crearGrupo() {
        String nombreGrupo = txtNombreGrupo.getText().trim();
        if (nombreGrupo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del grupo no puede estar vacío.");
            return;
        }
        
        // Validar que no exista un grupo con el mismo nombre para el usuario actual
        boolean nombreDuplicado = Controlador.getInstancia()
            .getGruposUsuarioActual()
            .stream()
            .anyMatch(g -> g.getNombre().equalsIgnoreCase(nombreGrupo));

        if (nombreDuplicado) {
            JOptionPane.showMessageDialog(this, "Ya existe un grupo con ese nombre. Por favor, elige otro nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<ContactoIndividual> contactosSeleccionados = listaContactos.getSelectedValuesList();
        if (contactosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un contacto.");
            return;
        }

        // Crear y registrar el grupo en la BD
        Grupo nuevoGrupo = Controlador.getInstancia().crearGrupo(nombreGrupo, contactosSeleccionados);

        // Si se eligió imagen personalizada, actualizarla
        if (imagenSeleccionadaGrupo != null) {
            nuevoGrupo.setFoto(imagenSeleccionadaGrupo);
            Controlador.getInstancia().getAdaptadorGrupo().modificarGrupo(nuevoGrupo);
        }

        VentanaMain.getInstancia().actualizarListaContactos();
        JOptionPane.showMessageDialog(this, "Grupo creado exitosamente.");
        dispose();
    }
}
