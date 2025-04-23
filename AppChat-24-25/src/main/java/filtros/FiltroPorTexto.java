package filtros;

import java.util.List;
import java.util.stream.Collectors;
import appChat.Mensaje;

/**
 * Filtro que selecciona mensajes que contienen un texto específico.
 */
public class FiltroPorTexto implements FiltroBusqueda {
    private String texto;

    public FiltroPorTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        if (texto == null || texto.isEmpty()) return mensajes;

        return mensajes.stream()
        	    .filter(m -> m.getTexto() != null && m.getTexto().toLowerCase().contains(texto.toLowerCase()))
        	    .collect(Collectors.toList());

    }
}
