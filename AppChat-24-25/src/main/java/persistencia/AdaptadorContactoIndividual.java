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

	private static AdaptadorContactoIndividual unicaInstancia = null;
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
	

	public void registrarContacto(ContactoIndividual contacto) {
		Entidad eContacto = null;
		
		try {
			eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		} catch (NullPointerException e) {
			
		}
		
		if (eContacto != null) return;
		
		 // 🔹 Verificar si el contacto ya existe antes de registrarlo
        if (existeContacto(contacto)) {
            System.out.println("ℹ️ Contacto ya existe: " + contacto.getNombre());
            return;
        }
		
		registrarSiNoExiste(contacto.getUsuario());
		
		registrarSiNoExisteMensaje(contacto.getMensajesEnviados());
		
		eContacto = new Entidad();
		eContacto.setNombre("contacto");
		eContacto.setPropiedades(new ArrayList<>(Arrays.asList(
				new Propiedad("nombre", contacto.getNombre()),
				new Propiedad("usuario", String.valueOf(contacto.getUsuario().getCodigo())),
				new Propiedad("mensajesRecibidos", obtenerCodigosMensajesRecibidos(contacto.getMensajesEnviados())),
				new Propiedad("telefono", String.valueOf(contacto.getTelefono())))));
		eContacto = servPersistencia.registrarEntidad(eContacto);
		contacto.setCodigo(eContacto.getId());
		
		// 🔹 Verificar si la lista de mensajes es null antes de registrar mensajes
	    if (contacto.getMensajesEnviados() == null) {
	        contacto.setMensajes(new LinkedList<>());
	    }

	    contacto.getMensajesEnviados().forEach(mensaje -> {
	        AdaptadorMensaje.getUnicaInstancia().registrarSiNoExiste(mensaje);
	    });
	    
		PoolDAO.getUnicaInstancia().añadirObjeto(contacto.getCodigo(), contacto);
    }


	

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

	@Override
	public ContactoIndividual recuperarContacto(int codigo) {
		if (PoolDAO.getUnicaInstancia().contieneID(codigo)) {
			return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		
		Entidad eContacto = servPersistencia.recuperarEntidad(codigo);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");
		String telefono = servPersistencia.recuperarPropiedadEntidad(eContacto, "telefono");

		ContactoIndividual contacto = new ContactoIndividual(nombre, new LinkedList<Mensaje>(), telefono, null);
		contacto.setCodigo(codigo);
		
		PoolDAO.getUnicaInstancia().añadirObjeto(codigo, contacto);
		
		List<Mensaje> mensajes = obtenerMensajesConCodigo(
				servPersistencia.recuperarPropiedadEntidad(eContacto, "mensajesRecibidos"));
		
		String codigoUsuario = servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario");
		
		Usuario usuario = AdaptadorUsuario.getUnicaInstancia().recuperarUsuario(Integer.valueOf(codigoUsuario));
		contacto.setUsuario(usuario);
		
		for (Mensaje mensaje : mensajes) {
			contacto.enviarMensaje(mensaje);
		}
		
		return contacto;

	}



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
	 * Registrar mensajes en persistencia si no existen
	 * 
	 * @param mensajes registrar
	 */
	private void registrarSiNoExisteMensaje(List<Mensaje> mensajes) {

		AdaptadorMensaje adaptadorMensaje = AdaptadorMensaje.getUnicaInstancia();
		mensajes.forEach(adaptadorMensaje::registrarMensaje);
	}

	
	private void registrarSiNoExiste(Usuario usuario) {
		AdaptadorUsuario adaptadorUsuario = AdaptadorUsuario.getUnicaInstancia();
        adaptadorUsuario.registrarUsuario(usuario);
		
	}
	
	private String obtenerCodigosMensajesRecibidos(List<Mensaje> mensajesRecibidos) {
		return mensajesRecibidos.stream().map(m -> String.valueOf(m.getCodigo()))
										.reduce("", (l, m) -> l + m + " ").trim();
	}

	
	private List<Mensaje> obtenerMensajesConCodigo(String codigos) {
		List<Mensaje> mensajes = new LinkedList<>();
		StringTokenizer strTok = new StringTokenizer(codigos, " ");
		AdaptadorMensaje adaptadorMensajes = AdaptadorMensaje.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			mensajes.add(adaptadorMensajes.recuperarMensaje(Integer.valueOf(strTok.nextToken())));
		}
		return mensajes;
	}
	
	private boolean existeContacto(ContactoIndividual contacto) {
	    return servPersistencia.recuperarEntidad(contacto.getCodigo()) != null;
	}

	
}



