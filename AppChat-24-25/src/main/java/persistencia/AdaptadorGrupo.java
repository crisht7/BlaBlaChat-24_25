package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


import javax.swing.ImageIcon;

import appChat.ContactoIndividual;
import appChat.Grupo;
import appChat.Mensaje;
import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Clase que implementa el Adaptador de Grupo para base de datos
 */
public class AdaptadorGrupo implements GrupoDAO{
	
	/**
	 * Instancia única de la clase AdaptadorGrupo (Singleton)
	 */
	private static AdaptadorGrupo unicaInstancia = null;
	/**
	 * Servicio de persistencia
	 */
	private static ServicioPersistencia servPersistencia;
	
	/**
	 * Instacia unica para mantener el patron singleton 
	 * @return unicaInstancia
	 */
	public static AdaptadorGrupo getUnicaInstancia() {
		if (unicaInstancia == null)
            unicaInstancia = new AdaptadorGrupo();
        return unicaInstancia;
	}

	/**
	 * Constructor privado para mantener el patron singleton
	 */
	private AdaptadorGrupo() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	/**
	 * Registra un grupo en la base de datos
	 * 
	 * @param grupo a registrar
	 */
	@Override
	public void registrarGrupo(Grupo grupo) {
		Entidad eContacto = null;
		
		try {
			eContacto = servPersistencia.recuperarEntidad(grupo.getCodigo());
		} catch (NullPointerException e) {
			System.err.println("No se ha encontrado la entidad de contacto");
		}
		
		if (eContacto != null) {
			return;
		}
		
		AdaptadorContactoIndividual adaptadorContactoIndividual = AdaptadorContactoIndividual.getUnicaInstancia();
		for (ContactoIndividual contacto : grupo.getIntegrantes()) {
			adaptadorContactoIndividual.registrarContacto(contacto);
		}
		AdaptadorMensaje adaptadorMensaje = AdaptadorMensaje.getUnicaInstancia();
		for (Mensaje mensaje : grupo.getMensajesEnviados()) {
			adaptadorMensaje.registrarMensaje(mensaje);
		}
		Entidad eContactoGrupo = new Entidad();
		eContactoGrupo.setNombre("grupo");
		eContactoGrupo.setPropiedades(new ArrayList<>(Arrays.asList(new Propiedad("nombre", grupo.getNombre()),
				new Propiedad("imagen", grupo.getFoto().getDescription()),
				new Propiedad("integrantes", obtenerCodigosMiembros(grupo.getIntegrantes())),
				new Propiedad("mensajes", obtenerCodigosMensajes(grupo.getMensajesEnviados())))));
		
		eContactoGrupo = servPersistencia.registrarEntidad(eContactoGrupo);
		grupo.setCodigo(eContactoGrupo.getId());
	}

	/**
	 * Elimina un grupo de la base de datos
	 * 
	 * @param grupo a eliminar
	 */
	@Override
	public void borrarGrupo(Grupo grupo) {
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		servPersistencia.borrarEntidad(eGrupo);
	}

	/**
	 * Recupera un grupo de la base de datos a traves del codigo
	 * 
	 * @param codigo codigo del grupo a recuperar
	 * @return grupo
	 */
	@Override
	public Grupo recuperarGrupo(int codigo) {
		if (PoolDAO.getUnicaInstancia().contieneID(codigo)) {
			return (Grupo) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		
		Entidad eContactoGrupo = servPersistencia.recuperarEntidad(codigo);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContactoGrupo, "nombre");
		String imagen = servPersistencia.recuperarPropiedadEntidad(eContactoGrupo, "imagen");
		ImageIcon pfp = getImageIcon(imagen);
		
		Grupo grupo = new Grupo(nombre, pfp);
		PoolDAO.getUnicaInstancia().añadirObjeto(codigo, grupo);
		
		List<ContactoIndividual> miembros = obtenerMiembros(
			servPersistencia.recuperarPropiedadEntidad(eContactoGrupo, "integrantes"));
		for (ContactoIndividual contacto : miembros) {
            grupo.agregarIntegrante(contacto);
        }
		
		List<Mensaje> mensajes = obtenerMensajes(
				servPersistencia.recuperarPropiedadEntidad(eContactoGrupo, "mensajes"));
		grupo.añadirListaMensajes(mensajes);
		
		grupo.setCodigo(codigo);
		return grupo;
	}

	/**
	 * Recupera los grupos almacenados en la base de datos
	 * 
	 * @return lista de grupos
	 */
	@Override
	public List<Grupo> recuperarTodosGrupos() {
		List<Grupo> grupos = new ArrayList<>();
		List<Entidad> eGrupos = servPersistencia.recuperarEntidades("grupo");
		
		for (Entidad eGrupo : eGrupos) {
			grupos.add(recuperarGrupo(eGrupo.getId()));
		}
		
		return grupos;
	}

	/**
	 * Modifica un grupo de la base de datos
	 * 
	 * @param grupo a modificar
	 */
	@Override
	public void modificarGrupo(Grupo grupo) {
		Entidad eContactoGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());

		for (Propiedad p : eContactoGrupo.getPropiedades()) {
			switch (p.getNombre()) {
			case "codigo":
				p.setValor(String.valueOf(grupo.getCodigo()));
				break;
			case "nombre":
				p.setValor(grupo.getNombre());
				break;
			case "imagen":
				p.setValor(grupo.getFoto().getDescription());
				break;
			case "integrantes":
				p.setValor(obtenerCodigosMiembros(grupo.getIntegrantes()));
				break;
			case "mensajes":
				p.setValor(obtenerCodigosMensajes(grupo.getMensajesEnviados()));
				break;
			}
			servPersistencia.modificarPropiedad(p);
		}
		
	}
	
	/**
	 * Convierte una cadena de texto a un ImageIcon
	 * 
	 * @param path ruta de la imagen
	 * @return ImageIcon
	 */
	private ImageIcon getImageIcon(String path) {
		return new ImageIcon(path);
	}

	/**
	 * Convierte una lista de mensajes a un String de codigos
	 * 
	 * @param mensajesRecibidos lista de mensajes
	 * @return codigo del mensaje
	 */
	private String obtenerCodigosMensajes(List<Mensaje> mensajesRecibidos) {
		return mensajesRecibidos.stream().
				map(m -> String.valueOf(m.getCodigo())).reduce("",(l, c) -> l + c + " ").trim();

	}
	
	/**
	 * Convierte una lista de miembros a un String de codigos
	 * 
	 * @param miembros lista de miembros
	 * @return codigo del miembro
	 */
	private String obtenerCodigosMiembros(List<ContactoIndividual> miembros) {
		return miembros.stream().
				map(m -> String.valueOf(m.getCodigo())).reduce("",(l, c) -> l + c + " ").trim();
	}
	
	
	/**
	 * Convierte un String de codigos a una lista de miembros
	 * 
	 * @param codigos a convertir
	 * @return lista de miembros
	 */
	private List<ContactoIndividual> obtenerMiembros(String codigos) {
		List<ContactoIndividual> miembros = new LinkedList<>();
        StringTokenizer codigosMiembros = new StringTokenizer(codigos, " ");
        AdaptadorContactoIndividual adaptadorCI = AdaptadorContactoIndividual.getUnicaInstancia();
		while (codigosMiembros.hasMoreTokens()) {
			miembros.add(adaptadorCI.recuperarContacto(Integer.valueOf(codigosMiembros.nextToken()))); // Se obtiene el contacto individual a partir del código
		}
        return miembros;
    }
	
	/**
	 * Convierte un String de codigos a una lista de mensajes
	 * 
	 * @param mensajes a convertir
	 * @return lista de mensajes
	 */
	private List<Mensaje> obtenerMensajes(String mensajes) {
		List<Mensaje> listMensajes = new ArrayList<>();
		StringTokenizer codigosMensajes = new StringTokenizer(mensajes, " ");
		AdaptadorMensaje adaptadorMensaje = AdaptadorMensaje.getUnicaInstancia();
		while (codigosMensajes.hasMoreTokens()) {
			listMensajes.add(adaptadorMensaje.recuperarMensaje(Integer.valueOf(codigosMensajes.nextToken())));
		}
		return listMensajes;
	}
}
	
	
	



