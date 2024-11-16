package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaContacto extends JDialog {

    private JList<String> listaContactos;
    private JList<String> listaGrupo;
    private DefaultListModel<String> modeloContactos;
    private DefaultListModel<String> modeloGrupo;

    public VentanaContacto(JFrame parent) {
        super(parent, "Gestión de Contactos y Grupos", true); // Modalidad activada
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2, 10, 10));
        add(panelPrincipal, BorderLayout.CENTER);

        // Panel de contactos
        JPanel panelContactos = new JPanel(new BorderLayout());
        panelPrincipal.add(panelContactos);

        JLabel lblContactos = new JLabel("Lista Contactos");
        panelContactos.add(lblContactos, BorderLayout.NORTH);

        modeloContactos = new DefaultListModel<>();
        listaContactos = new JList<>(modeloContactos);
        JScrollPane scrollContactos = new JScrollPane(listaContactos);
        panelContactos.add(scrollContactos, BorderLayout.CENTER);

        JButton btnAnadirContacto = new JButton("Añadir Contacto");
        panelContactos.add(btnAnadirContacto, BorderLayout.SOUTH);

        // Panel de grupo
        JPanel panelGrupo = new JPanel(new BorderLayout());
        panelPrincipal.add(panelGrupo);

        JLabel lblGrupo = new JLabel("Grupo");
        panelGrupo.add(lblGrupo, BorderLayout.NORTH);

        modeloGrupo = new DefaultListModel<>();
        listaGrupo = new JList<>(modeloGrupo);
        JScrollPane scrollGrupo = new JScrollPane(listaGrupo);
        panelGrupo.add(scrollGrupo, BorderLayout.CENTER);

        JButton btnAnadirGrupo = new JButton("Añadir Grupo");
        panelGrupo.add(btnAnadirGrupo, BorderLayout.SOUTH);

        // Panel central para botones entre listas
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 10, 10));
        add(panelBotones, BorderLayout.EAST);

        JButton btnMoverADerecha = new JButton(">>");
        panelBotones.add(btnMoverADerecha);

        JButton btnMoverAIzquierda = new JButton("<<");
        panelBotones.add(btnMoverAIzquierda);

        // Eventos de botones
        btnAnadirContacto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoContacto = JOptionPane.showInputDialog("Ingrese el nombre del nuevo contacto:");
                if (nuevoContacto != null && !nuevoContacto.trim().isEmpty()) {
                    modeloContactos.addElement(nuevoContacto);
                }
            }
        });

        btnAnadirGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoGrupo = JOptionPane.showInputDialog("Ingrese el nombre del nuevo grupo:");
                if (nuevoGrupo != null && !nuevoGrupo.trim().isEmpty()) {
                    modeloGrupo.addElement(nuevoGrupo);
                }
            }
        });

        btnMoverADerecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccionado = listaContactos.getSelectedValue();
                if (seleccionado != null) {
                    modeloContactos.removeElement(seleccionado);
                    modeloGrupo.addElement(seleccionado);
                }
            }
        });

        btnMoverAIzquierda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccionado = listaGrupo.getSelectedValue();
                if (seleccionado != null) {
                    modeloGrupo.removeElement(seleccionado);
                    modeloContactos.addElement(seleccionado);
                }
            }
        });

        // Centrar la ventana en relación al padre
        setLocationRelativeTo(parent);
    }
}
