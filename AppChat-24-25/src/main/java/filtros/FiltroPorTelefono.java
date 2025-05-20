package filtros;

import java.util.List;
import java.util.stream.Collectors;

import appChat.Contacto;
import appChat.ContactoIndividual;
import appChat.Mensaje;
import appChat.Usuario;
import controlador.Controlador;

/**
 * Filtro que selecciona mensajes cuyo emisor tiene un número de teléfono específico.
 */
public class FiltroPorTelefono implements FiltroBusqueda {
	/**
	 * Número de teléfono del emisor a filtrar.
	 */
    private String numeroTelefono;

    /**
     * Constructor que inicializa el filtro con un número de teléfono específico.
     * @param numeroTelefono Número de teléfono del emisor a filtrar.
     */
    public FiltroPorTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

	/**
	 * Filtra una lista de mensajes según el número de teléfono del emisor.
	 * 
	 * @param mensajes La lista de mensajes a filtrar.
	 * @return Una nueva lista de mensajes que cumplen con el criterio del filtro.
	 */
    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        if (numeroTelefono == null || numeroTelefono.isEmpty()) return mensajes;

        return mensajes.stream()
                .filter(m -> {
                    Usuario emisor = m.getEmisor();
                    Usuario receptor =Controlador.getInstancia().getUsuarioPorTelefono(numeroTelefono);

                    boolean emisorCoincide = emisor.getTelefono().equals(numeroTelefono);
                    boolean receptorCoincide = receptor.getTelefono().equals(numeroTelefono);

                    return emisorCoincide || receptorCoincide;
                })
                .collect(Collectors.toList());
        
        
        
    }
}

