package appChat;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

public class Usuario {
    private String nombre;
    private ImageIcon fotoPerfil;
    private String contraseña;
    private String telefono;
    private String saludo;
    private boolean premium;
    private LocalDate fechaNacimiento;
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
     * @param fotoPerfil
     * @param contraseña
     * @param telefono
     * @param saludo
     * @param fechaRegistro
     */
    public Usuario(String nombre, ImageIcon imagen, String contraseña, 
    			String telefono, String saludo, LocalDate fechaNacimiento) {
    	this.codigo = 0;
    	this.nombre = nombre;
    	this.fotoPerfil = imagen;
    	this.contraseña = contraseña;
    	this.telefono = telefono;
    	this.saludo = saludo;
    	this.fechaNacimiento = fechaNacimiento;
    	this.premium = false;
    	this.contactos = new LinkedList<>();
    	
    }
    
    /**
     * Constructor que inicializa un usuario con premium especificado
     * 
     * @param nombre
     * @param fecha
     * @param fotoPerfil
     * @param contraseña
     * @param telefono
     * @param saludo
     * @param fechaRegistro
     * @param isPremium
     */
    public Usuario(String nombre, ImageIcon imagen, String contraseña, 
			String telefono, String saludo, LocalDate fechaNacimiento, Boolean isPremium) {
    	this(nombre, imagen, contraseña, telefono, saludo, fechaNacimiento);
    	this.premium = isPremium;
    	this.contactos = new LinkedList<>();

    
    }
    
    /**
     * Constructor que inicializa un usuario con premium y codigos especificados
     * 
     * @param nombre
     * @param fecha
     * @param fotoPerfil
     * @param contraseña
     * @param telefono
     * @param saludo
     * @param fechaRegistro
     * @param isPremium
     */
    public Usuario(String nombre, LocalDate fecha, ImageIcon imagen, String contraseña, 
			String telefono, String saludo, LocalDate fechaRegistro, Boolean isPremium, int codigo) {
    	this(nombre, imagen, contraseña, telefono, saludo, fechaRegistro,isPremium);
    	this.codigo = codigo;	
    	this.contactos = new LinkedList<>();

    }
    
    /**
     * Verifica si el usuario tiene un contacto individual por nombre
     * 
     * @param nombreContacto
     * @return true si el usuario tiene un contacto individual con ese nombre
     */
	public boolean tieneContactoIndividual(String nombreContacto) {
		return contactos.stream().
				anyMatch(c -> c instanceof ContactoIndividual && c.getNombre().equals(nombreContacto));
	}
	
	/**
	 * Verifica si el usuario ya tiene un contacto individual con el teléfono especificado
	 * 
	 * @param telefono del posible contacto
	 * @return true si ya existe un contacto con ese número
	 */
	public boolean tieneContactoIndividualPorTelefono(String telefono) {
	    return contactos.stream()
	        .filter(c -> c instanceof ContactoIndividual)
	        .map(c -> (ContactoIndividual) c)
	        .anyMatch(c -> c.getTelefono().equals(telefono));
	}
	
	/**
	 * Verifica si el usuario tiene un grupo por nombre
	 * 
	 * @param nombre
	 * @return true si el usuario tiene un grupo con ese nombre
	 */
	public boolean tieneGrupo(String nombre) {
		return contactos.stream().
				anyMatch(c -> c instanceof Grupo && c.getNombre().equals(nombre));
	}
	
	/**
	 * Añade un contacto a la lista de contactos del usuario
	 * 
	 * @param contacto
	 */
	public void añadirContacto(Contacto contacto) {
		this.contactos.add(contacto);
	}
	
	/**
	 * Cuenta el total de mensajes enviados por el usuario actual 
	 * a todos sus contactos
	 * 
	 * @return numero de mensajes enviados por el usuario
	 */
	public int totalMensajesEnviados() {
		int totalMensajes=0;
		for (Contacto contacto : this.getContactos()) {
			for (Mensaje mensaje : contacto.getMensajesEnviados()) {
				if (mensaje.getEmisor().equals(this)) {
					totalMensajes++;
				}
			}
		}
		
		return totalMensajes;
	}
    
    /**
     * Añade un contacto a la lista de contactos del usuario
     * 
     * @return nombre
     */
    public String getNombre() {
    	return this.nombre;
    }
	

	
	/**
	 * Devuelve el telefono del usuario
	 * 
	 * @return telefono
	 */
	public String getTelefono() {
		return this.telefono;
	}
	
	/**
	 * Devuelve el saludo del usuario
	 * 
	 * @return saludo
	 */
	public String getSaludo() {
		return this.saludo;
	}
	
	/**
	 * Devuelve la contraseña del usuario
	 * 
	 * @return contraseña
	 */
	public String getContraseña() {
		return this.contraseña;
	}
	
	/**
	 * Devuelve si el usuario es premium
	 * 
	 * @return premium
	 */
	public boolean isPremium() {
		return this.premium;
	}
	
	/**
     * Devuelve la lista de contactos del usuario
     * 
     * @return contactos
     */
	public List<Contacto> getContactos() {
	    if (this.contactos == null) {
	        System.err.println("⚠️ Advertencia: La lista de contactos es null para el usuario " + this.nombre);
	        return new LinkedList<>();
	    }

	    // Detectar contactos nulos antes de devolver la lista
	    for (Contacto c : this.contactos) {
	        if (c == null) {
	            System.err.println("❌ Error: Se encontró un contacto NULL en la lista de " + this.nombre);
	        }
	    }
	    return this.contactos;
	}

	
	/**
	 * Devuelve la lista de grupos del usuario
	 * 
	 * @return grupos
	 */
	public List<Grupo> getGrupos(){
		return contactos.stream().
				filter(c -> c instanceof Grupo).
				map(c -> (Grupo) c).
				collect(Collectors.toList());
	}
	
	/**
	 * Devuelve la lista de contactos individuales del usuario
	 * 
	 * @return contactos individuales
	 */
	public List<ContactoIndividual> getContactosIndividuales() {
		return contactos.stream().
				filter(c -> c instanceof ContactoIndividual).
				map(c -> (ContactoIndividual) c)
				.collect(Collectors.toList());
	}

	
	/**
	 * Devuelve la fecha de registro del usuario
	 * 
	 * @return fechaRegistro
	 */
	public LocalDate getFechaRegistro() {
		return this.fechaNacimiento;
	}
	
	/**
     * Devuelve la fotoPerfil del usuario
     * 
     * @return fotoPerfil
     */
	public ImageIcon getFotoPerfil() {
        return this.fotoPerfil;
    }
	
	/**
	 * Devuelve el codigo del usuario
	 * 
	 * @return codigo
	 */
	public int getCodigo() {
		return this.codigo;
	}
	
	/**
	 * Establece un nombre al usuario
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	/**
	 * Establece el codigo al usuario
	 * 
	 * @param codigo
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	/**
	 * Establece un saludo al usuario
	 * 
	 * @param saludo
	 */
	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}
	
	/**
	 * Establece el numero de telefono del usuario
	 * 
	 * @param telefono
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	
	/**
	 * Establece si el usuario es premium
	 * 
	 * @param premium
	 */
	public void setPremium(boolean premium) {
        this.premium = premium;
    }
	
	/**
	 * Establece una nueva fotoPerfil de perfil al usuario
	 * 
	 * @param fotoPerfil
	 */
	public void setFotoPerfil(ImageIcon imagen) {
		this.fotoPerfil = imagen;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Usuario other = (Usuario) obj;
		return this.telefono.equals(other.telefono);
	}
	
	public List<Contacto> getContactosOrdenadosPorMensaje() {
	    if (contactos == null || contactos.isEmpty()) {
	        System.err.println("❌ No hay contactos en usuario");
	    	return new LinkedList<>(); // 🔹 Si no hay contactos, devuelve una lista vacía
	    }

	    // Ordenar contactos por el número total de mensajes enviados
	    return contactos.stream()
	            .filter(c -> c != null) // 🔹 Evita contactos nulos
	            .sorted(Comparator.comparing(c -> c.getMensajesEnviados().size(), Comparator.reverseOrder()))
	            .collect(Collectors.toList());

	}

	
	public void setContactos(List<Contacto> contactos) {
	    if (contactos == null) {
	        this.contactos = new LinkedList<>(); // 🔹 Asegurar que nunca sea null
	    } else {
	        this.contactos = contactos;
	    }
	}

	
	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + "," + ", fotoPerfil=" + fotoPerfil + ", contraseña=" + contraseña
                + ", telefono=" + telefono + ", saludo=" + saludo + ", premium=" + premium + ", fechaRegistro="
                + fechaNacimiento + ", codigo=" + codigo + ", contactos=" + contactos + "]";
	}
}
