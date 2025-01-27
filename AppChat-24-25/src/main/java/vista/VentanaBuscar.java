package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaBuscar extends JDialog {

    private JTextField textFieldTexto;
    private JTextField textFieldTelefono;
    private JTextField textFieldContacto;

    /**
     * Create the dialog.
     */
    public VentanaBuscar(JFrame parent) {
        super(parent, "Buscar", true); // Modalidad activada
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setResizable(false); // Desactiva la redimensión
        getContentPane().setLayout(null);

        // Etiqueta de buscar
        JLabel lblBuscar = new JLabel("Buscar");
        lblBuscar.setBounds(10, 10, 760, 50);
        lblBuscar.setHorizontalAlignment(SwingConstants.CENTER);
        lblBuscar.setFont(new Font("Tahoma", Font.BOLD, 24));
        getContentPane().add(lblBuscar);

        // Campo de texto para buscar texto
        JLabel lblTexto = new JLabel("Texto:");
        lblTexto.setBounds(10, 70, 80, 25);
        getContentPane().add(lblTexto);

        textFieldTexto = new JTextField();
        textFieldTexto.setBounds(100, 70, 200, 25);
        getContentPane().add(textFieldTexto);
        textFieldTexto.setColumns(10);

        // Campo de texto para buscar por teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(10, 110, 80, 25);
        getContentPane().add(lblTelefono);

        textFieldTelefono = new JTextField();
        textFieldTelefono.setBounds(100, 110, 200, 25);
        getContentPane().add(textFieldTelefono);
        textFieldTelefono.setColumns(10);

        // Campo de texto para buscar por contacto
        JLabel lblContacto = new JLabel("Contacto:");
        lblContacto.setBounds(10, 150, 80, 25);
        getContentPane().add(lblContacto);

        textFieldContacto = new JTextField();
        textFieldContacto.setBounds(100, 150, 200, 25);
        getContentPane().add(textFieldContacto);
        textFieldContacto.setColumns(10);

        // Botón de buscar
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(320, 70, 100, 105);
        getContentPane().add(btnBuscar);

        // Panel para mostrar resultados
        JPanel panelResultados = new JPanel();
        panelResultados.setBounds(10, 200, 760, 350);
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelResultados);
        scrollPane.setBounds(10, 200, 760, 350);
        getContentPane().add(scrollPane);
    }
}
