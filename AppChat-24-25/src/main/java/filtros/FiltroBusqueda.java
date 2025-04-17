package filtros;

import java.util.List;
import appChat.Mensaje;

/**
 * Representa un filtro genérico para aplicar criterios de búsqueda sobre una lista de mensajes.
 */
public interface FiltroBusqueda {

    /**
     * Filtra una lista de mensajes según el criterio implementado por la clase concreta.
     * 
     * @param mensajes La lista de mensajes a filtrar. No debe ser {@code null}.
     * @return Una nueva lista de mensajes que cumplen con el criterio del filtro.
     */
    List<Mensaje> filtrar(List<Mensaje> mensajes);
}
