package persistencia;

import appChat.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorUsuario implements UsuarioDAO {
	
	private static AdaptadorUsuario unicaInstancia = null;
	private static ServicioPersistencia servPersistencia;
	
	/**
	 * Constructor privado Singleton
	 */
	private AdaptadorUsuario() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	/**
	 * Devuelve la única instancia unica de la clase
	 * Garantiza Singleton
	 * @return unicaInstancia
	 */
	public static AdaptadorUsuario getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorUsuario();
		return unicaInstancia;
	}

	/**
	 * Registra un usuario en la base de datos
	 * 
	 * @param usuario a registrar
	 */
	@Override
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;
		
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {			
		}
		
		if(eUsuario != null) 
			return;
		
		usuario.getContactos().forEach(c -> {
            AdaptadorContactoIndividual.getUnicaInstancia().registrarContacto((ContactoIndividual) c);
            });
		
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<>(Arrays.asList(
				new Propiedad("nombre", usuario.getNombre()), 
				new Propiedad("fecha", usuario.getFecha().toString()), 
				new Propiedad("telefono", usuario.getTelefono()),
				new Propiedad("contraseña", usuario.getContraseña()),
				new Propiedad("saludo", usuario.getSaludo()),
				new Propiedad("premium", String.valueOf(usuario.isPremium())),
				new Propiedad("foto", usuario.getFotoPerfil().getDescription()),
				new Propiedad("contactos", usuario.getContactos().toString()), //TODO: ¿Es la forma?
				new Propiedad("fechaRegistro", usuario.getFechaRegistro().toString()),
				new Propiedad("Grupos", usuario.getGrupos().toString()))));
		
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setCodigo(eUsuario.getId());
		PoolDAO.getUnicaInstancia().añadirObjeto(usuario.getCodigo(), usuario);
	}

	@Override
	public void borrarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Usuario recuperarUsuario(int codigo) {
		if (PoolDAO.getUnicaInstancia().contieneID(codigo)) {
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		
		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		LocalDate fecha = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fecha"));
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
		String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
		Boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium"));
		LocalDate fechaRegistro = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaRegistro"));
		String direccionFoto = servPersistencia.recuperarPropiedadEntidad(eUsuario, "foto");
		
		ImageIcon fotoPerfil = new ImageIcon(direccionFoto);
		
		Usuario usuario = new Usuario(nombre, fecha, fotoPerfil, contraseña, telefono, saludo, fechaRegistro, premium);
		
		PoolDAO.getUnicaInstancia().añadirObjeto(codigo, usuario);
		
		//TODO:Recuperar contaxtos	
		
		return usuario;
		
	}

	@Override
	public List<Usuario> recuperarTodosUsuarios() {
		List<Usuario> usuarios = new LinkedList<>();
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
	
		return usuarios;
	}

	@Override
	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		for (Propiedad p : eUsuario.getPropiedades()) {
			switch (p.getNombre()) {
			case "nombre":
				p.setValor(usuario.getNombre());
				break;
			case "fecha":
				p.setValor(usuario.getFecha().toString());
				break;
			case "telefono":
				p.setValor(usuario.getTelefono());
				break;
			case "contraseña":
				p.setValor(usuario.getContraseña());
				break;
			case "saludo":
				p.setValor(usuario.getSaludo());
				break;
			case "premium":
				p.setValor(String.valueOf(usuario.isPremium()));
				break;
			case "foto":
				p.setValor(usuario.getFotoPerfil().getDescription());
				break;
			case "contactos":
				p.setValor(usuario.getContactos().toString());
				break;
			case "fechaRegistro":
				p.setValor(usuario.getFechaRegistro().toString());
				break;
			case "grupos":
				p.setValor(usuario.getGrupos().toString());
				break;
			}
			servPersistencia.modificarPropiedad(p);
		}
		
	}

}
