package filtros;

import java.util.List;
import java.util.stream.Collectors;
import appChat.Mensaje;

/**
 * Filtro que selecciona mensajes cuyo emisor tiene un número de teléfono específico.
 */
public class FiltroPorTelefono implements FiltroBusqueda {
    private String numeroTelefono;

    public FiltroPorTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        if (numeroTelefono == null || numeroTelefono.isEmpty()) return mensajes;

        return mensajes.stream()
                .filter(m -> m.getEmisor().getTelefono().equals(numeroTelefono))
                .collect(Collectors.toList());
    }
}

