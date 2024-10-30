package tds.swing.componentes;
import javax.swing.*;

import java.awt.*;

public class Programa {
	
    public static void main(String[] args) {
        // Crear un JFrame
        JFrame frame = new JFrame("Ejemplo de JList con DefaultListModel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setSize(300, 200);

        // Crear un modelo de lista y agregar elementos
        DefaultListModel<Persona> modelo = new DefaultListModel<>();
        modelo.addElement(new Persona("Jose", "Lopez"));
        modelo.addElement(new Persona("Ana", "Jover"));
        modelo.addElement(new Persona("Maria", "Sanchez"));
        modelo.addElement(new Persona("Pedro", "Garcia"));
        
        // Crear el JList basado en el modelo
        JList<Persona> lista = new JList<>(modelo);
        lista.setCellRenderer(new PersonaCellRenderer());//Renderizador

        // Agregar la lista al frame
        frame.add(new JScrollPane(lista), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}