package filtros;

import java.util.List;
import java.util.stream.Collectors;

import appChat.Contacto;
import appChat.ContactoIndividual;
import appChat.Grupo;
import appChat.Mensaje;
import appChat.Usuario;

/**
 * Filtro que selecciona mensajes relacionados con un nombre de usuario o contacto.
 */
public class FiltroPorNombre implements FiltroBusqueda {
    private String nombre;

    public FiltroPorNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        if (nombre == null || nombre.isEmpty()) return mensajes;

        return mensajes.stream()
            .filter(m -> {
                Usuario emisor = m.getEmisor();
                Contacto receptor = m.getReceptor();

                boolean emisorCoincide = emisor.getNombre().equals(nombre);

                boolean receptorCoincide = false;
                if (receptor instanceof ContactoIndividual) {
                    receptorCoincide = receptor.getNombre().equals(nombre);
                } else if (receptor instanceof Grupo) {
                    receptorCoincide = ((Grupo) receptor).getIntegrantes().stream()
                            .anyMatch(u -> u.getNombre().equals(nombre));
                }

                return emisorCoincide || receptorCoincide;
            })
            .collect(Collectors.toList());
    }
}
