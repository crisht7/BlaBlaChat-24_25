package filtros;

import java.util.List;
import java.util.stream.Collectors;
import appChat.Mensaje;

/**
 * Filtro que selecciona mensajes cuyo emisor tiene un número de teléfono específico.
 */
public class FiltroPorTelefono implements FiltroBusqueda {
    private String numeroTelefono;

    /**
     * Constructor que inicializa el filtro con un número de teléfono específico.
     * @param numeroTelefono
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
                .filter(m -> m.getEmisor().getTelefono().equals(numeroTelefono))
                .collect(Collectors.toList());
    }
}

