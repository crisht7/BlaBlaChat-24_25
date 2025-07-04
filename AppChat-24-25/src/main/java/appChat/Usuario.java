package appChat;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

/**
 * Representa un usuario de la aplicación de chat.
 */
public class Usuario {
	/**
	 * Nombre del usuario.
	 */
    private String nombre;
    /**
     * Foto de perfil del usuario.
     */
    private ImageIcon fotoPerfil;
	/**
	 * Contraseña del usuario.
	 */
    private String contraseña;
    /**
     * Número de teléfono del usuario.
     */
    private String telefono;
	/**
	 * Saludo del usuario.
	 */
    private String saludo;
	/**
	 * Indica si el usuario es premium.
	 */
    private boolean premium;
    /**
     * Fecha de registro del usuario.
     */
    private LocalDate fechaRegistro;
    /**
     * Código único del usuario.
     */
    private int codigo;
    /**
     * Lista de contactos del usuario.
     */
    private List<Contacto> contactos;

    // ===================== Constructores =====================

    /**
     * Constructor por defecto, crea un usuario sin premium.
     */
    public Usuario() {
        this.premium = false;
        this.contactos = new LinkedList<>();
    }

    /**
     * Constructor que inicializa los atributos básicos.
     * 
     * @param nombre nombre del usuario
     * @param imagen foto de perfil
     * @param contraseña contraseña del usuario
     * @param telefono número de teléfono
     * @param saludo saludo del usuario
     * @param fechaRegistro fecha de registro
     */
    public Usuario(String nombre, ImageIcon imagen, String contraseña, 
                   String telefono, String saludo, LocalDate fechaRegistro) {
        this();
        this.nombre = nombre;
        this.fotoPerfil = imagen;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.saludo = saludo;
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Constructor que inicializa un usuario con premium especificado.
     * 
     * @param nombre nombre del usuario
     * @param imagen foto de perfil
     * @param contraseña contraseña del usuario
     * @param telefono número de teléfono
     * @param saludo saludo del usuario
     * @param fechaRegistro fecha de registro
     * @param isPremium estado premium del usuario
     */
    public Usuario(String nombre, ImageIcon imagen, String contraseña, 
                   String telefono, String saludo, LocalDate fechaRegistro, Boolean isPremium) {
        this(nombre, imagen, contraseña, telefono, saludo, fechaRegistro);
        this.premium = isPremium;
    }

    /**
     * Constructor que inicializa un usuario con premium y código especificados.
     * 
     * @param nombre nombre del usuario
     * @param fecha fecha de registro
     * @param imagen foto de perfil
     * @param contraseña contraseña del usuario
     * @param telefono número de teléfono
     * @param saludo saludo del usuario
     * @param fechaRegistro fecha de registro
     * @param isPremium estado premium del usuario
     * @param codigo código único del usuario
     */
    public Usuario(String nombre, LocalDate fecha, ImageIcon imagen, String contraseña, 
                   String telefono, String saludo, LocalDate fechaRegistro, Boolean isPremium, int codigo) {
        this(nombre, imagen, contraseña, telefono, saludo, fechaRegistro, isPremium);
        this.codigo = codigo;
    }

    // ===================== Métodos de Verificación =====================

    /**
     * Verifica si el usuario tiene un contacto individual por nombre.
     * 
     * @param nombreContacto nombre del contacto
     * @return true si el contacto existe, false en caso contrario
     */
    public boolean tieneContactoIndividual(String nombreContacto) {
        return contactos.stream()
                .anyMatch(c -> c instanceof ContactoIndividual && c.getNombre().equals(nombreContacto));
    }

    /**
     * Verifica si el usuario ya tiene un contacto individual con el teléfono especificado.
     * 
     * @param telefono número de teléfono del contacto
     * @return true si el contacto existe, false en caso contrario
     */
    public boolean tieneContactoIndividualPorTelefono(String telefono) {
        return contactos.stream()
                .filter(c -> c instanceof ContactoIndividual)
                .map(c -> (ContactoIndividual) c)
                .anyMatch(c -> c.getTelefono().equals(telefono));
    }

    /**
     * Verifica si el usuario tiene un grupo por nombre.
     * 
     * @param nombre nombre del grupo
     * @return true si el grupo existe, false en caso contrario
     */
    public boolean tieneGrupo(String nombre) {
        return contactos.stream()
                .anyMatch(c -> c instanceof Grupo && c.getNombre().equals(nombre));
    }

    // ===================== Métodos de Contactos =====================

    /**
     * Añade un contacto a la lista de contactos del usuario.
     * 
     * @param contacto contacto a añadir
     */
    public void añadirContacto(Contacto contacto) {
        this.contactos.add(contacto);
    }

    /**
     * Devuelve la lista de contactos del usuario.
     * 
     * @return lista de contactos
     */
    public List<Contacto> getContactos() {
        if (this.contactos == null) {
            return new LinkedList<>();
        }
        return this.contactos;
    }

    /**
     * Devuelve la lista de grupos del usuario.
     * 
     * @return lista de grupos
     */
    public List<Grupo> getGrupos() {
        return contactos.stream()
                .filter(c -> c instanceof Grupo)
                .map(c -> (Grupo) c)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve la lista de contactos individuales del usuario.
     * 
     * @return lista de contactos individuales
     */
    public List<ContactoIndividual> getContactosIndividuales() {
        return contactos.stream()
                .filter(c -> c instanceof ContactoIndividual)
                .map(c -> (ContactoIndividual) c)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve una lista de contactos ordenados por el número total de mensajes enviados.
     * 
     * @return lista de contactos ordenados
     */
    public List<Contacto> getContactosOrdenadosPorMensaje() {
        if (contactos == null || contactos.isEmpty()) {
            return new LinkedList<>();
        }

        return contactos.stream()
                .filter(c -> c != null)
                .sorted(Comparator.comparing((Contacto c) -> c.getMensajesEnviados().size()).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Establece la lista de contactos del usuario.
     * 
     * @param contactos nueva lista de contactos
     */
    public void setContactos(List<Contacto> contactos) {
        if (contactos == null) {
            this.contactos = new LinkedList<>();
        } else {
            this.contactos = contactos;
        }
    }

    // ===================== Getters =====================

	/**
	 * Devuelve el nombre del usuario.
	 * 
	 * @return nombre del usuario
	 */
    public String getNombre() {
        return this.nombre;
    }
    /**
     * Devuelve el número de teléfono del usuario.
     * 
     * @return número de teléfono
     */
    public String getTelefono() {
        return this.telefono;
    }

	/**
	 * Devuelve el saludo del usuario.
	 * 
	 * @return saludo del usuario
	 */
    public String getSaludo() {
        return this.saludo;
    }
    /**
     * Devuelve la contraseña del usuario.
     * 
     * @return contraseña del usuario
     */
    public String getContraseña() {
        return this.contraseña;
    }
    /**
     * Devuelve el estado premium del usuario.
     * 
     * @return true si es premium, false en caso contrario
     */
    public boolean isPremium() {
        return this.premium;
    }
	/**
	 * Devuelve la fecha de registro del usuario.
	 * 
	 * @return fecha de registro
	 */
    public LocalDate getFechaRegistro() {
        return this.fechaRegistro;
    }
	/**
	 * Devuelve la foto de perfil del usuario.
	 * 
	 * @return foto de perfil
	 */
    public ImageIcon getFotoPerfil() {
        return this.fotoPerfil;
    }
	/**
	 * Devuelve el código único del usuario.
	 * 
	 * @return código único
	 */
    public int getCodigo() {
        return this.codigo;
    }

    // ===================== Setters =====================

    /**
     * Establece el nombre del usuario.
     * 
     * @param nombre nuevo nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el número de teléfono del usuario.
     * @param telefono nuevo número de teléfono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

	/**
	 * Establece el saludo del usuario.
	 * 
	 * @param saludo nuevo saludo del usuario
	 */
    public void setSaludo(String saludo) {
        this.saludo = saludo;
    }

	/**
	 * Establece la contraseña del usuario.
	 * 
	 * @param contraseña nueva contraseña del usuario
	 */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Establece el estado premium del usuario.
     * 
     * @param premium nuevo estado premium
     */
    public void setPremium(boolean premium) {
        this.premium = premium;
    }

	/**
	 * Establece la foto de perfil del usuario.
	 * 
	 * @param imagen nueva foto de perfil
	 */
    public void setFotoPerfil(ImageIcon imagen) {
        this.fotoPerfil = imagen;
    }

    /**
     * Establece un nuevo codigo único para el usuario.
     * 
     * @param codigo nuevo código único
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    // ===================== Otros Métodos =====================

    /**
     * Cuenta el total de mensajes enviados por el usuario a todos sus contactos.
     * 
     * @return total de mensajes enviados
     */
    public int totalMensajesEnviados() {
        int total = 0;
        for (Contacto contacto : this.getContactos()) {
            for (Mensaje mensaje : contacto.getMensajesEnviados()) {
                if (mensaje.getEmisor().equals(this)) {
                    total++;
                }
            }
        }
        return total;
    }

    /**
     * Compara dos usuarios por su número de teléfono.
     * 
     * @param obj objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario other = (Usuario) obj;
        return this.telefono.equals(other.telefono);
    }

    /**
     * Representa el usuario como una cadena de texto.
     * 
     * @return representación en cadena del usuario
     */
    @Override
    public String toString() {
        return "Usuario [nombre=" + nombre + ", fotoPerfil=" + fotoPerfil + ", contraseña=" + contraseña
                + ", telefono=" + telefono + ", saludo=" + saludo + ", premium=" + premium
                + ", fechaRegistro=" + fechaRegistro + ", codigo=" + codigo + ", contactos=" + contactos + "]";
    }
}
