package appChat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import com.toedter.calendar.JDateChooser;

public class Usuario {
    private String nombre;
    private LocalDate fecha;
    private ImageIcon imagen;
    private String contraseña;
    private String telefono;
    private String saludo;
    private boolean premium;
    private LocalDate fechaRegistro;
    private int codigo;
    private List<Contacto> contactos;
    
    /**
     * Contructor por defecto, crea un usuario sin premium
     */
    public Usuario() {
    	this.premium = false;
    }
    /** 
     * Constructor que inicializa los atributos básicos
     * @param nombre
     * @param fecha
     * @param imagen
     * @param contraseña
     * @param telefono
     * @param saludo
     * @param fechaRegistro
     */
    public Usuario(String nombre, LocalDate fecha, ImageIcon imagen, String contraseña, 
    			String telefono, String saludo, LocalDate fechaRegistro) {
    	this.codigo = 0;
    	this.nombre = nombre;
    	this.fecha = fecha;
    	this.imagen = imagen;
    	this.contraseña = contraseña;
    	this.telefono = telefono;
    	this.saludo = saludo;
    	this.fechaRegistro = fechaRegistro;
    	this.premium = false;
    	this.contactos = new LinkedList<>();
    	
    }
    
    /**
     * Constructor que inicializa un usuario con premium especificado
     * 
     * @param nombre
     * @param fecha
     * @param imagen
     * @param contraseña
     * @param telefono
     * @param saludo
     * @param fechaRegistro
     * @param isPremium
     */
    public Usuario(String nombre, LocalDate fecha, ImageIcon imagen, String contraseña, 
			String telefono, String saludo, LocalDate fechaRegistro, Boolean isPremium) {
    	this(nombre, fecha, imagen, contraseña, telefono, saludo, fechaRegistro);
    	this.premium = isPremium;
    
    }
    
    /**
     * Constructor que inicializa un usuario con premium y codigos especificados
     * 
     * @param nombre
     * @param fecha
     * @param imagen
     * @param contraseña
     * @param telefono
     * @param saludo
     * @param fechaRegistro
     * @param isPremium
     */
    public Usuario(String nombre, LocalDate fecha, ImageIcon imagen, String contraseña, 
			String telefono, String saludo, LocalDate fechaRegistro, Boolean isPremium, int codigo) {
    	this(nombre, fecha, imagen, contraseña, telefono, saludo, fechaRegistro,isPremium);
    	this.codigo = codigo;	
    
    }
    
    //TODO: Getters y setters bien hechos
    //TODO: Añadir contacto
    //TODO: Verificar si el usaurio tiene un contacto individual por nombre 
    //TODO: Verificar si el usuario riene un grupo por nombre
    //TODO: MEtodo que cuente el total de mensajes enviados para poder calcular el descuento
    //TODO: A traves del numero de telefono y el @override del equal comprobar su dos usuarios son iguales
    
    //TODO: ¿Devuelve Contacto o devuelve ContactoIndividual?
    public Contacto getContactoIndividual(Usuario otroUsuario) {
        // Implementar la lógica para obtener un contacto individual
        return null;
    }

    public List<Mensaje> getChatMensajes(Usuario otroUsuario) {
        // Implementar la lógica para obtener mensajes
        return new ArrayList<>();
    }

	public String getNombre() {
		
		return this.nombre;
	}
	
	public String getNumero() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getTelefono() {
		return telefono;
	}
	public String getContraseña() {
		return this.contraseña;
	}
	
	

}
