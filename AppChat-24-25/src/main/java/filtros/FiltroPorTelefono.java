package filtros;

import java.util.List;
import java.util.stream.Collectors;

import appChat.Contacto;
import appChat.ContactoIndividual;
import appChat.Grupo;
import appChat.Mensaje;
import appChat.Usuario;

/**
 * Filtro que selecciona mensajes relacionados con un número de teléfono.
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
            .filter(m -> {
                Usuario emisor = m.getEmisor();
                Contacto receptor = m.getReceptor();

                boolean emisorCoincide = emisor.getTelefono().equals(numeroTelefono);

                boolean receptorCoincide = false;
                if (receptor instanceof ContactoIndividual) {
                    receptorCoincide = ((ContactoIndividual) receptor).getTelefono().equals(numeroTelefono);
                } else if (receptor instanceof Grupo) {
                    receptorCoincide = ((Grupo) receptor).getIntegrantes().stream()
                            .anyMatch(u -> u.getTelefono().equals(numeroTelefono));
                }

                return emisorCoincide || receptorCoincide;
            })
            .collect(Collectors.toList());
    }
}
