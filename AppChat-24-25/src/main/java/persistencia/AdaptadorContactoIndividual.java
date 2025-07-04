package persistencia;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import appChat.ContactoIndividual;
import appChat.Mensaje;
import appChat.Usuario;
import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Clase que implementa el Adaptador de ContactoIndividual para base de datos 
 * con DAO
 */
public class AdaptadorContactoIndividual implements ContactoIndividualDAO {

	/**
	 * Instancia única de la clase AdaptadorContactoIndividual (Singleton)
	 */
	private static AdaptadorContactoIndividual unicaInstancia = null;
	/**
	 * Servicio de persistencia
	 */
	private static ServicioPersistencia servPersistencia;
	
	
	/**
	 * Constructor privado Singleton
	 */
	private AdaptadorContactoIndividual() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	/**
	 * Devuelve la única instancia unica de la clase 
	 * Garantiza Singleton
	 * 
	 * @return unicaInstancia
	 */
	public static AdaptadorContactoIndividual getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorContactoIndividual();
		return unicaInstancia;
	}
	
	/**
	 * Registra un contacto individual en la base de datos
	 * 
	 * @param contacto a registrar
	 */
	@Override
	public void registrarContacto(ContactoIndividual contacto) {

		Entidad eContacto = new Entidad();

		// Si la entidad está registrada no la registra de nuevo
		try {
			eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		} catch (NullPointerException e) {
			System.err.println("No se ha encontrado la entidad de contacto");
		}
	
		// Registrar los mensajes del contacto
		registrarSiNoExistenMensajes(contacto.getMensajesEnviados());

		// Registramos al usuario correspondiente al contacto si no existe.
		registrarSiNoExiste(contacto.getUsuario());
		
		eContacto = new Entidad();
		eContacto.setNombre("contacto");
		eContacto.setPropiedades(new ArrayList<>(Arrays.asList(
				new Propiedad("nombre", contacto.getNombre()),
				new Propiedad("usuario", String.valueOf(contacto.getUsuario().getCodigo())),
				new Propiedad("mensajesRecibidos", obtenerCodigosMensajesRecibidos(contacto.getMensajesEnviados())),
				new Propiedad("telefono", String.valueOf(contacto.getTelefono())))));
		
		eContacto = servPersistencia.registrarEntidad(eContacto);
		
		contacto.setCodigo(eContacto.getId());
	    
		PoolDAO.getUnicaInstancia().añadirObjeto(contacto.getCodigo(), contacto);
    }	
	
	/**
	 * Borra un contacto individual de la base de datos
	 * 
	 * @param contacto a borrar
     */
	@Override
	public void borrarContacto(ContactoIndividual contacto) {

		Entidad eContact;

		// Borrar los mensajes asociados al contacto
		AdaptadorMensaje adaptadorMensaje = AdaptadorMensaje.getUnicaInstancia();
		for (Mensaje mensaje : contacto.getMensajesEnviados()) {
			adaptadorMensaje.borrarMensaje(mensaje);
		}

		eContact = servPersistencia.recuperarEntidad(contacto.getCodigo());
		servPersistencia.borrarEntidad(eContact);

		// Eliminar del pool si está presente
		if (PoolDAO.getUnicaInstancia().contieneID(contacto.getCodigo()))
			PoolDAO.getUnicaInstancia().eliminarObjeto(contacto.getCodigo());
	}
	

	/**
	 * Modifica un contacto individual en la base de datos
	 * 
	 * @param contacto a modificar
	 */
	@Override
	public void modificarContacto(ContactoIndividual contacto) {
		Entidad eContact = servPersistencia.recuperarEntidad(contacto.getCodigo());

		for (Propiedad p : eContact.getPropiedades()) {
			switch (p.getNombre()) {
			case "nombre":
				p.setValor(contacto.getNombre());
				break;
			case "telefono":
				p.setValor(String.valueOf(contacto.getTelefono()));
				break;
			case "mensajesRecibidos":
				p.setValor(obtenerCodigosMensajesRecibidos(contacto.getMensajesEnviados()));
				break;
			case "usuario":
				p.setValor(String.valueOf(contacto.getUsuario().getCodigo()));
				break;
			}
			servPersistencia.modificarPropiedad(p);
		}
	}

	/**
	 * Recupera un contacto individual de la base de datos
	 * 
	 * @param codigo del contacto a recuperar
	 * @return contacto recuperado
	 */
	@Override
	public ContactoIndividual recuperarContacto(int codigo) {
	    if (codigo == 0) return null;

	    if (PoolDAO.getUnicaInstancia().contieneID(codigo)) {
	        return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(codigo);
	    }

	    Entidad eContacto = servPersistencia.recuperarEntidad(codigo);

	    String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");
	    String telefono = servPersistencia.recuperarPropiedadEntidad(eContacto, "telefono");

	    // Crear el objeto base con campos simples
	    ContactoIndividual contacto = new ContactoIndividual(nombre, new LinkedList<Mensaje>(), telefono, null);
	    contacto.setCodigo(codigo);

	    // Añadir al Pool antes de seguir para evitar recursión infinita
	    PoolDAO.getUnicaInstancia().añadirObjeto(codigo, contacto);

	    // Recuperar mensajes asociados al contacto
	    List<Mensaje> mensajes = obtenerMensajesDesdeCodigos(
	        servPersistencia.recuperarPropiedadEntidad(eContacto, "mensajesRecibidos"));

	    // Recuperar el usuario asociado al contacto
	    String codigoUsuario = servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario");
	    Usuario user = AdaptadorUsuario.getUnicaInstancia().recuperarUsuario(Integer.valueOf(codigoUsuario));

	    contacto.setUsuario(user);

	    // Añadir mensajes al contacto
	    for (Mensaje m : mensajes) {
	        contacto.enviarMensaje(m);
	    }

	    return contacto;
	}

	/**
	 * Recupera todos los contactos individuales de la base de datos
	 * 
	 * @return lista de contactos individuales
	 */
	@Override
	public List<ContactoIndividual> recuperarTodosContactosIndividuales() {
		List<ContactoIndividual> contactos = new LinkedList<>();
		List<Entidad> eContacts = servPersistencia.recuperarEntidades("contacto");

		for (Entidad eContact : eContacts) {
			contactos.add(recuperarContacto(eContact.getId()));
		}

		return contactos;
	}

	/**
	 * Registra los mensajes del contacto individual si no existen
	 * 
	 * @param messages lista de mensajes a registrar
	 */
	private void registrarSiNoExistenMensajes(List<Mensaje> messages) {
		AdaptadorMensaje adaptadorMensajes = AdaptadorMensaje.getUnicaInstancia();
		messages.stream().forEach(m -> adaptadorMensajes.registrarMensaje(m));
	}
	
	/**
	 * Registra el usuario si no existe
	 * 
	 * @param usuario a registrar
	 */
	private void registrarSiNoExiste(Usuario usuario) {
		AdaptadorUsuario adaptadorUsuario = AdaptadorUsuario.getUnicaInstancia();
        adaptadorUsuario.registrarUsuario(usuario);
	}
	
	/**
	 * Convierte una lista de mensajes a una cadena de códigos
	 * 
	 * @param mensajesRecibidos lista de mensajes
	 * @return cadena de códigos
	 */
	private String obtenerCodigosMensajesRecibidos(List<Mensaje> mensajesRecibidos) {
		return mensajesRecibidos.stream().map(m -> String.valueOf(m.getCodigo()))
				.reduce("", (l, m) -> l + m + " ").trim();
	}
	
	/**
	 * Convierte una cadena de códigos a una lista de mensajes
	 * 
	 * @param codigos cadena de códigos
	 * @return lista de mensajes
	 */
	private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
		List<Mensaje> mensajes = new LinkedList<>();
		StringTokenizer strTok = new StringTokenizer(codigos, " ");
		AdaptadorMensaje adaptadorMensajes = AdaptadorMensaje.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			mensajes.add(adaptadorMensajes.recuperarMensaje(Integer.valueOf(strTok.nextToken())));
		}
		return mensajes;
	}

}



