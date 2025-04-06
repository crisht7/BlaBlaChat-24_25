package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import appChat.ContactoIndividual;
import appChat.Grupo;
import appChat.Mensaje;
import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorGrupo implements GrupoDAO{

	private static AdaptadorGrupo unicaInstancia = null;
	private static ServicioPersistencia servPersistencia;
	
	/**
	 * Instacia unica para mantener el patron singleton
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
	 * @param grupo
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
	 * @param grupo
	 */
	@Override
	public void borrarGrupo(Grupo grupo) {
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		servPersistencia.borrarEntidad(eGrupo);
	}

	
	/**
	 * Recupera un grupo de la base de datos a traves del codigo
	 * 
	 * @param codigo
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
	
	private ImageIcon getImageIcon(String path) {
		//METODO SIN IMPLEMENTAR
		return new ImageIcon(path);
	}

	/**
	 * Convierte una lista de mensajes a un String de codigos
	 * 
	 * @return codigo
	 */
	private String obtenerCodigosMensajes(List<Mensaje> mensajesRecibidos) {
		return mensajesRecibidos.stream()
                .map(m -> String.valueOf(m.getCodigo()))
                .collect(Collectors.joining(","));
	}
	
	/**
	 * Convierte una lista de miembros a un String de codigos
	 * 
	 * @return codigo
	 */
	private String obtenerCodigosMiembros(List<ContactoIndividual> miembros) {
		return miembros.stream().
				map(m -> String.valueOf(m.getCodigo())).
				collect(Collectors.joining(","));
	}
	
	
	/**
	 * Convierte un String de codigos a una lista de miembros
	 * 
	 * @return lista de miembros
	 */
	private List<ContactoIndividual> obtenerMiembros(String codigos) {
		List<ContactoIndividual> miembros = new ArrayList<>();
        String[] codigosMiembros = codigos.split(",");
        for (String codigo : codigosMiembros) {
            miembros.add(AdaptadorContactoIndividual.getUnicaInstancia().recuperarContacto(Integer.valueOf(codigo)));
        }
        return miembros;
    }
	
	/**
	 * Convierte un String de codigos a una lista de mensajes
	 * 
	 * @param recuperarPropiedadEntidad
	 * @return
	 */
	private List<Mensaje> obtenerMensajes(String recuperarPropiedadEntidad) {
		List<Mensaje> mensajes = new ArrayList<>();
		String[] codigosMensajes = recuperarPropiedadEntidad.split(",");
		AdaptadorMensaje adaptadorMensaje = AdaptadorMensaje.getUnicaInstancia();
		for (String codigo : codigosMensajes) {
			mensajes.add(adaptadorMensaje.recuperarMensaje(Integer.valueOf(codigo)));
		}
		return mensajes;
	}
}
	
	
	



