package filtros;

import java.util.List;
import java.util.stream.Collectors;
import appChat.Mensaje;

/**
 * Filtro que selecciona mensajes que contienen un texto específico.
 */
public class FiltroPorTexto implements FiltroBusqueda {
    private String texto;

    /**
     * Constructor que inicializa el filtro con un texto específico.
     * @param texto
     */
    public FiltroPorTexto(String texto) {
        this.texto = texto;
    }

	/**
	 * Filtra una lista de mensajes según el texto.
	 * 
	 * @param mensajes La lista de mensajes a filtrar.
	 * @return Una nueva lista de mensajes que cumplen con el criterio del filtro.
	 */
    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        if (texto == null || texto.isEmpty()) return mensajes;

        return mensajes.stream()
        	    .filter(m -> m.getTexto() != null && m.getTexto().toLowerCase().contains(texto.toLowerCase()))
        	    .collect(Collectors.toList());

    }
}
