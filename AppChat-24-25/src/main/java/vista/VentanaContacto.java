package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import appChat.*;
import controlador.Controlador;

import java.awt.Toolkit;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * Ventana que permite añadir un nuevo contacto
 */
public class VentanaContacto extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldName;
    private JTextField textFieldTelf;
    private DefaultListModel<Contacto> modelContacts;

	private final Color naranjaClaro = Colores.NARANJA_CLARO.getColor();
	private final Color naranjaOscuro = Colores.NARANJA_OSCURO.getColor();
	private final Color turquesa = Colores.TURQUESA.getColor();
	private final Color boton = Colores.NARANJA_BOTON.getColor();
    
    /**
     * Crea la ventana
     */
    public VentanaContacto(DefaultListModel<Contacto> modelo) {
        this.modelContacts = modelo;
        setTitle("Add contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 558, 334);
        contentPane = new JPanel();
        contentPane.setBackground(naranjaClaro); 
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{150, 0, 150, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(naranjaOscuro); 
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_1.insets = new Insets(0, 0, 5, 5);
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 0;
        contentPane.add(panel_1, gbc_panel_1);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel label = new JLabel("Agregar Contactos");
        panel_1.add(label);

        JPanel panel = new JPanel();
        panel.setBackground(naranjaOscuro);
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 1;
        contentPane.add(panel, gbc_panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0, 0};
        gbl_panel.rowHeights = new int[]{0, 13, 0, 0, 0};
        gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);

        JLabel lblName = new JLabel("Nombre");
        lblName.setBackground(new Color(255, 255, 255));
        lblName.setForeground(new Color(0, 0, 0)); // Texto en blanco
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.anchor = GridBagConstraints.EAST;
        gbc_lblName.insets = new Insets(0, 0, 5, 5);
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 0;
        panel.add(lblName, gbc_lblName);

        textFieldName = new JTextField();
        textFieldName.addActionListener(e -> addContacto());
        textFieldName.setForeground(new Color(255, 255, 255));
        textFieldName.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        textFieldName.setCaretColor(new Color(245, 210, 158));
        textFieldName.setBackground(new Color(255, 255, 255)); // Naranja oscuro
        textFieldName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textFieldName.setBackground(new Color(255, 100, 0));
            }
        });
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        panel.add(textFieldName, gbc_textField);
        textFieldName.setColumns(10);

        JLabel lblPhoneNumber = new JLabel("Telefono");
        lblPhoneNumber.setForeground(new Color(0, 0, 0));
        GridBagConstraints gbc_lblPhoneNumber = new GridBagConstraints();
        gbc_lblPhoneNumber.anchor = GridBagConstraints.EAST;
        gbc_lblPhoneNumber.insets = new Insets(0, 0, 0, 5);
        gbc_lblPhoneNumber.gridx = 0;
        gbc_lblPhoneNumber.gridy = 3;
        panel.add(lblPhoneNumber, gbc_lblPhoneNumber);

        textFieldTelf = new JTextField();
        textFieldTelf.addActionListener(e -> addContacto());
        textFieldTelf.setForeground(new Color(255, 255, 255));
        textFieldTelf.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        textFieldTelf.setCaretColor(new Color(245, 210, 158));
        textFieldTelf.setBackground(new Color(255, 255, 255));
        textFieldTelf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textFieldTelf.setBackground(new Color(255, 100, 0));
            }
        });
        GridBagConstraints gbc_textField_1 = new GridBagConstraints();
        gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_1.gridx = 1;
        gbc_textField_1.gridy = 3;
        panel.add(textFieldTelf, gbc_textField_1);
        textFieldTelf.setColumns(10);

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(naranjaClaro); 
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.insets = new Insets(0, 0, 0, 5);
        gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_2.gridx = 1;
        gbc_panel_2.gridy = 2;
        contentPane.add(panel_2, gbc_panel_2);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton btnAdd = new JButton("ADD");
        btnAdd.addActionListener(arg0 -> addContacto());
        btnAdd.setBackground(boton); // Botón naranja más oscuro
        panel_2.add(btnAdd);
    }

    private boolean datosCorrectos() {
        List<String> errores = new LinkedList<>();

        if (textFieldName.getText().equals("")) {
            errores.add("Nombre no valido");
            textFieldName.setBackground(new Color(255, 69, 0)); // Rojo-naranja
        }

        if (textFieldTelf.getText().equals("") || !isNumeric(textFieldTelf.getText())
                || Integer.parseInt(textFieldTelf.getText()) < 0) {
            textFieldTelf.setBackground(new Color(255, 69, 0)); 
            errores.add("Telefono no valido");
        }

        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errores), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isNumeric(String strNum) {
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

	/**
	 * Comprueba errores y añade el contacto si está todo correcto
	 */
	private void addContacto() {
		// Comprobamos que los datos son correctos
		if (!datosCorrectos())
			return;

		// Creamos el contacto
		ContactoIndividual nuevoContacto = Controlador.getInstancia().crearContacto(textFieldName.getText(),
				String.valueOf(textFieldTelf.getText()));
		if (nuevoContacto == null) {
			// No se ha podido crear el usuario
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(VentanaContacto.this, "El contacto ya existe o no es un usuario real",
					"Error", JOptionPane.ERROR_MESSAGE);
		} else {
			// Usuario creado
			modelContacts.add(modelContacts.size(), nuevoContacto);
			JOptionPane.showMessageDialog(VentanaContacto.this, "Contacto añadido correctamente", "Info",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

