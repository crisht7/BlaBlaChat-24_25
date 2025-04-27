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

    private String nombre;
    private ImageIcon fotoPerfil;
    private String contraseña;
    private String telefono;
    private String saludo;
    private boolean premium;
    private LocalDate fechaRegistro;
    private int codigo;
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
     */
    public Usuario(String nombre, ImageIcon imagen, String contraseña, 
                   String telefono, String saludo, LocalDate fechaRegistro, Boolean isPremium) {
        this(nombre, imagen, contraseña, telefono, saludo, fechaRegistro);
        this.premium = isPremium;
    }

    /**
     * Constructor que inicializa un usuario con premium y código especificados.
     */
    public Usuario(String nombre, LocalDate fecha, ImageIcon imagen, String contraseña, 
                   String telefono, String saludo, LocalDate fechaRegistro, Boolean isPremium, int codigo) {
        this(nombre, imagen, contraseña, telefono, saludo, fechaRegistro, isPremium);
        this.codigo = codigo;
    }

    // ===================== Métodos de Verificación =====================

    /**
     * Verifica si el usuario tiene un contacto individual por nombre.
     */
    public boolean tieneContactoIndividual(String nombreContacto) {
        return contactos.stream()
                .anyMatch(c -> c instanceof ContactoIndividual && c.getNombre().equals(nombreContacto));
    }

    /**
     * Verifica si el usuario ya tiene un contacto individual con el teléfono especificado.
     */
    public boolean tieneContactoIndividualPorTelefono(String telefono) {
        return contactos.stream()
                .filter(c -> c instanceof ContactoIndividual)
                .map(c -> (ContactoIndividual) c)
                .anyMatch(c -> c.getTelefono().equals(telefono));
    }

    /**
     * Verifica si el usuario tiene un grupo por nombre.
     */
    public boolean tieneGrupo(String nombre) {
        return contactos.stream()
                .anyMatch(c -> c instanceof Grupo && c.getNombre().equals(nombre));
    }

    // ===================== Métodos de Contactos =====================

    /**
     * Añade un contacto a la lista de contactos del usuario.
     */
    public void añadirContacto(Contacto contacto) {
        this.contactos.add(contacto);
    }

    /**
     * Devuelve la lista de contactos del usuario.
     */
    public List<Contacto> getContactos() {
        if (this.contactos == null) {
            return new LinkedList<>();
        }
        return this.contactos;
    }

    /**
     * Devuelve la lista de grupos del usuario.
     */
    public List<Grupo> getGrupos() {
        return contactos.stream()
                .filter(c -> c instanceof Grupo)
                .map(c -> (Grupo) c)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve la lista de contactos individuales del usuario.
     */
    public List<ContactoIndividual> getContactosIndividuales() {
        return contactos.stream()
                .filter(c -> c instanceof ContactoIndividual)
                .map(c -> (ContactoIndividual) c)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve una lista de contactos ordenados por el número total de mensajes enviados.
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
     */
    public void setContactos(List<Contacto> contactos) {
        if (contactos == null) {
            this.contactos = new LinkedList<>();
        } else {
            this.contactos = contactos;
        }
    }

    // ===================== Getters =====================

    public String getNombre() {
        return this.nombre;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public String getSaludo() {
        return this.saludo;
    }

    public String getContraseña() {
        return this.contraseña;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public LocalDate getFechaRegistro() {
        return this.fechaRegistro;
    }

    public ImageIcon getFotoPerfil() {
        return this.fotoPerfil;
    }

    public int getCodigo() {
        return this.codigo;
    }

    // ===================== Setters =====================

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setSaludo(String saludo) {
        this.saludo = saludo;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setFotoPerfil(ImageIcon imagen) {
        this.fotoPerfil = imagen;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    // ===================== Otros Métodos =====================

    /**
     * Cuenta el total de mensajes enviados por el usuario a todos sus contactos.
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
     */
    @Override
    public String toString() {
        return "Usuario [nombre=" + nombre + ", fotoPerfil=" + fotoPerfil + ", contraseña=" + contraseña
                + ", telefono=" + telefono + ", saludo=" + saludo + ", premium=" + premium
                + ", fechaRegistro=" + fechaRegistro + ", codigo=" + codigo + ", contactos=" + contactos + "]";
    }
}
