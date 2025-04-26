package filtros;

import java.text.Normalizer;
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

        String nombreNormalizado = normalizarTexto(nombre);

        return mensajes.stream()
            .filter(m -> {
                Usuario emisor = m.getEmisor();
                Contacto receptor = m.getReceptor();

                boolean emisorCoincide = normalizarTexto(emisor.getNombre()).equals(nombreNormalizado);
                boolean receptorCoincide = (receptor instanceof ContactoIndividual) &&
                        normalizarTexto(receptor.getNombre()).equals(nombreNormalizado);

                return emisorCoincide || receptorCoincide;
            })
            .collect(Collectors.toList());
    }


    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoNormalizado.replaceAll("\\p{M}", "").toLowerCase().trim();
    }
}
