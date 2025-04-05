package persistencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import appChat.*;
import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorMensaje implements MensajeDAO {

	public static AdaptadorMensaje unicainstancia = null;
	public static ServicioPersistencia servPersistencia;
	
	/**
	 * Constructor privado Singleton
	 */
	private AdaptadorMensaje() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	/**
	 * Devuelve la única instancia unica de la clase 
	 * Garantiza Singleton
	 * 
	 * @return unicaInstancia
	 */
	public static AdaptadorMensaje getUnicaInstancia() {
		if (unicainstancia == null)
			unicainstancia = new AdaptadorMensaje();
		return unicainstancia;
	}

	/**
	 * Registra un mensaje en la base de datos
	 * 
	 * @param mensaje a registrar
	 */
	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = null;

		try {
			eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		} catch (NullPointerException e) {
		}

		if (eMensaje != null)
			return;
		
		eMensaje = new Entidad();
		eMensaje.setNombre("mensaje");
		eMensaje.setPropiedades(new ArrayList<>(Arrays.asList(
				new Propiedad("hora", mensaje.getHora().toString()),
				new Propiedad("emoticono", String.valueOf(mensaje.getEmoticono())),
				new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getCodigo())),
				new Propiedad("texto", mensaje.getTexto()),
				new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getCodigo())),
				new	Propiedad("grupo", String.valueOf(mensaje.isGroup())))));			

		eMensaje = servPersistencia.registrarEntidad(eMensaje);
		
		mensaje.setCodigo(eMensaje.getId());
		
		PoolDAO.getUnicaInstancia().añadirObjeto(mensaje.getCodigo(), mensaje);
	}
	
	/**
	 * Elimina un mensaje de la base de datos
	 * 
	 * @param mensaje a eliminar
	 */
	@Override
	public void borrarMensaje(Mensaje mensaje) {
		 Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		 servPersistencia.borrarEntidad(eMensaje);
		 
			if (PoolDAO.getUnicaInstancia().contieneID(mensaje.getCodigo())) {
				PoolDAO.getUnicaInstancia().eliminarObjeto(mensaje.getCodigo());
			}
	}
	
	/**
	 * Modifica un mensaje en la base de datos
	 * 
	 * @param mensaje modificado
	 */
	@Override
	public void modificarMensaje(Mensaje mensaje) {
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		
		for (Propiedad p : eMensaje.getPropiedades()) {
			switch (p.getNombre()) {
			case "hora":
				p.setValor(mensaje.getHora().toString());
				break;
			case "emoticono":
				p.setValor(String.valueOf(mensaje.getEmoticono()));
				break;
			case "receptor":
				p.setValor(String.valueOf(mensaje.getReceptor().getCodigo()));
				break;
			case "texto":
				p.setValor(mensaje.getTexto());
				break;
			case "emisor":
				p.setValor(String.valueOf(mensaje.getEmisor().getCodigo()));
				break;
			case "grupo":
				p.setValor(String.valueOf(mensaje.isGroup()));
				break;
			}
			servPersistencia.modificarPropiedad(p);
		}
		
	}

	/**
	 * Recupera un mensaje de la base de datos a traves del codigo
	 * 
	 */
	@Override
	public Mensaje recuperarMensaje(int codigo) {
		//Primero comprobamos que esté en el pool
		if (PoolDAO.getUnicaInstancia().contieneID(codigo)) {
			return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		
		Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);
		
		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
		int emoticono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emoticono"));
		LocalDateTime hora = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, "hora"));
		
		Mensaje mensaje = new Mensaje(texto, emoticono, hora);
		mensaje.setCodigo(codigo);
		
		PoolDAO.getUnicaInstancia().añadirObjeto(codigo, mensaje);
		
		AdaptadorUsuario adaptadorUsuario = AdaptadorUsuario.getUnicaInstancia();
		int codEmisor = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor"));
		Usuario emisor = adaptadorUsuario.recuperarUsuario(codEmisor);
		mensaje.setEmisor(emisor);
		
		AdaptadorContactoIndividual adaptadorContacto = AdaptadorContactoIndividual.getUnicaInstancia();
		int codReceptor = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor"));
		try {
            Contacto receptor = adaptadorContacto.recuperarContacto(codReceptor);
            mensaje.setReceptor(receptor);
        } catch (Exception e) {
        	Contacto receptor = AdaptadorGrupo.getUnicaInstancia().recuperarGrupo(codReceptor);
            mensaje.setReceptor(receptor);
        }
		
		boolean grupo = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eMensaje, "grupo"));
		mensaje.setGroup(grupo);
		
		return mensaje;
	}
	
	
	/**
	 * Recupera todos los mensajes de la base de datos
	 * 
	 * @return lista de mensajes
	 */
	@Override
	public List<Mensaje> recuperarTodosMensajes() {
		List<Mensaje> mensajes = new ArrayList<>();
		List<Entidad> eMensajes = servPersistencia.recuperarEntidades("mensaje");
		
		for (Entidad eMensaje : eMensajes) {
			mensajes.add(recuperarMensaje(eMensaje.getId()));
		}
		return mensajes;
	}

	/**
	 * Registra un mensaje en la base de datos si no existe
	 * 
	 * @param mensaje a registrar
	 */
	public void registrarSiNoExiste(Mensaje mensaje) {
	    if (mensaje == null) return;

	    // Verificar si el mensaje ya existe en la base de datos antes de registrarlo
	    if (existeMensaje(mensaje)) {
	        System.out.println("ℹ️ Mensaje ya registrado: " + mensaje.getTexto());
	        return;
	    }

	}

	/**
	 * Verifica si un mensaje ya existe en la base de datos
	 * 
	 * @param mensaje a verificar
	 * @return true si existe, false en caso contrario
	 */
	private boolean existeMensaje(Mensaje mensaje) {
	    return servPersistencia.recuperarEntidad(mensaje.getCodigo()) != null;
	}

	
	/**
	 * Devuelve todos los mensajes enviados por un número de teléfono específico.
	 *
	 * @param telefono Teléfono del emisor.
	 * @return Lista de mensajes enviados por ese teléfono.
	 */
	public List<Mensaje> getMensajesEnviadosPor(String telefono) {
	    List<Mensaje> todos = recuperarTodosMensajes();
	    List<Mensaje> enviados = new ArrayList<>();

	    for (Mensaje m : todos) {
	        if (m.getEmisor() != null && m.getEmisor().getTelefono().equals(telefono)) {
	            enviados.add(m);
	        }
	    }

	    return enviados;
	}



}
