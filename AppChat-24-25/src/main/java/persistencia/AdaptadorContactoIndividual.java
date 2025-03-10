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
import controlador.Controlador;
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
	
	/*
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
        
        System.out.println("➕ Registrando nuevo contacto en persistencia: " + contacto.getNombre());
		
		
		registrarSiNoExisteMensaje(contacto);
		
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
	    
	    Usuario usuarioActual = Controlador.getInstancia().getUsuarioActual();
	    usuarioActual.añadirContacto(contacto);
	    
	    Controlador.getInstancia().getAdaptadorUsuario().registrarUsuario(usuarioActual);

	    
		PoolDAO.getUnicaInstancia().añadirObjeto(contacto.getCodigo(), contacto);
    }*/
	
	public void registrarContacto(ContactoIndividual contacto) {
	    System.out.println("➕ Registrando nuevo contacto en persistencia:");
	    System.out.println("   🔹 Nombre: " + contacto.getNombre());
	    System.out.println("   🔹 Teléfono: " + contacto.getTelefono());
	    System.out.println("   🔹 Usuario dueño: " + contacto.getUsuario().getCodigo());

	    Entidad eContacto = new Entidad();
	    eContacto.setNombre("contacto");
	    eContacto.setPropiedades(new ArrayList<>(Arrays.asList(
	        new Propiedad("nombre", contacto.getNombre()),
	        new Propiedad("usuario", String.valueOf(contacto.getUsuario().getCodigo())),
	        new Propiedad("mensajesRecibidos", obtenerCodigosMensajesRecibidos(contacto.getMensajesEnviados())),
	        new Propiedad("telefono", contacto.getTelefono())
	    )));

	    eContacto = servPersistencia.registrarEntidad(eContacto);
	    contacto.setCodigo(eContacto.getId());

	    System.out.println("✅ Contacto registrado con ID: " + contacto.getCodigo());

	    Usuario usuarioActual = Controlador.getInstancia().getUsuarioActual();
	    usuarioActual.añadirContacto(contacto);
	    Controlador.getInstancia().getAdaptadorUsuario().modificarUsuario(usuarioActual);

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
	    System.out.println("🔍 Intentando recuperar contacto con código: " + codigo);

	    if (PoolDAO.getUnicaInstancia().contieneID(codigo)) {
	        System.out.println("✅ Contacto obtenido desde PoolDAO.");
	        return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(codigo);
	    }

	    Entidad eContacto = servPersistencia.recuperarEntidad(codigo);
	    if (eContacto == null) {
	        System.err.println("❌ No se encontró la entidad para el contacto con código: " + codigo);
	        return null;
	    }

	    System.out.println("🟢 Propiedades de contacto en BD: ");
	    for (Propiedad p : eContacto.getPropiedades()) {
	        System.out.println(p.getNombre() + " = " + p.getValor());
	    }

	    String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");
	    String telefono = servPersistencia.recuperarPropiedadEntidad(eContacto, "telefono");
	    String codigoUsuarioStr = servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario");

	    if (codigoUsuarioStr == null || codigoUsuarioStr.isEmpty()) {
	        System.err.println("⚠️ Contacto sin usuario asignado.");
	        return null;
	    }

	    int codigoUsuario = Integer.parseInt(codigoUsuarioStr);
	    Usuario usuario = AdaptadorUsuario.getUnicaInstancia().recuperarUsuario(codigoUsuario);

	    if (usuario == null) {
	        System.err.println("❌ No se pudo recuperar el usuario con código " + codigoUsuario);
	        return null;
	    }

	    ContactoIndividual contacto = new ContactoIndividual(nombre, new LinkedList<>(), telefono, usuario);
	    contacto.setCodigo(codigo);
	    usuario.añadirContacto(contacto);

	    PoolDAO.getUnicaInstancia().añadirObjeto(codigo, contacto);
	    System.out.println("✅ Contacto recuperado y asignado al usuario: " + usuario.getNombre());

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
	public void registrarSiNoExisteMensaje(ContactoIndividual contacto) {
	    if (contacto == null) return;

	    // 🔹 Verificar si la lista de mensajes es null y corregirlo antes de recorrerla
	    if (contacto.getMensajesEnviados() == null) {
	        System.err.println("⚠️ La lista de mensajes del contacto era null. Inicializando lista vacía.");
	        contacto.setMensajes(new LinkedList<>());
	    }

	    // Registrar solo si el mensaje no existe
	    contacto.getMensajesEnviados().forEach(mensaje -> {
	        AdaptadorMensaje.getUnicaInstancia().registrarSiNoExiste(mensaje);
	    });
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



