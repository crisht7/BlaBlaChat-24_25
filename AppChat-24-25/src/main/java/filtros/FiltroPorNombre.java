package filtros;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

import appChat.Contacto;
import appChat.ContactoIndividual;
import appChat.Mensaje;
import appChat.Usuario;

/**
 * Filtro que selecciona mensajes relacionados con un nombre de usuario o contacto.
 */
public class FiltroPorNombre implements FiltroBusqueda {
	/**
	 * Nombre del usuario o contacto a filtrar.
	 */
	private String nombre;

	/**
	 * Constructor que inicializa el filtro con un nombre específico.
	 * 
	 * @param nombre Nombre del usuario o contacto a filtrar.
	 */
    public FiltroPorNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Filtra una lista de mensajes según el nombre del usuario o contacto.
     * @param mensajes La lista de mensajes a filtrar.
     * @return Una nueva lista de mensajes que cumplen con el criterio del filtro.
     */
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

    /**
     * Normaliza un texto eliminando acentos y convirtiéndolo a minúsculas.
     * 
     * @param texto Texto a normalizar.
     * @return texto normalizado
     */
    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoNormalizado.replaceAll("\\p{M}", "").toLowerCase().trim();
    }
}
