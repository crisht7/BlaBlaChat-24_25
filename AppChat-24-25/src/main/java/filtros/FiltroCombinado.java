package filtros;

import java.util.ArrayList;
import java.util.List;
import appChat.Mensaje;

/**
 * Filtro que permite combinar múltiples filtros aplicándolos en secuencia.
 */
public class FiltroCombinado implements FiltroBusqueda {
    private List<FiltroBusqueda> filtros;

	/**
	 * Constructor por defecto que inicializa la lista de filtros.
	 */
    public FiltroCombinado() {
        this.filtros = new ArrayList<>();
    }

    /**
     * Añade un filtro a la lista de filtros.
     * @param filtro Filtro a añadir.
     */
    public void añadirFiltro(FiltroBusqueda filtro) {
        if (filtro != null) {
            filtros.add(filtro);
        }
    }

	/**
	 * Elimina un filtro de la lista de filtros.
	 * 
	 * @param filtro Filtro a eliminar.
	 * @return true si se eliminó el filtro, false si no estaba en la lista.
	 */
    public boolean eliminarFiltro(FiltroBusqueda filtro) {
        return filtros.remove(filtro);
    }

    /**
     * Filtra una lista de mensajes aplicando todos los filtros en secuencia.
     * @param mensajes La lista de mensajes a filtrar.
     * @return Una nueva lista de mensajes que cumplen con todos los filtros.
     */
    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        List<Mensaje> resultado = new ArrayList<>(mensajes);
        for (FiltroBusqueda filtro : filtros) {
            resultado = filtro.filtrar(resultado);
        }
        return resultado;
    }

	/**
	 * Devuelve la lista de filtros.
	 * 
	 * @return Lista de filtros.
	 */
    public List<FiltroBusqueda> getFiltros() {
        return new ArrayList<>(filtros);
    }
    
	/**
	 * Comprueba si la lista de filtros está vacía.
	 * 
	 * @return true si la lista de filtros está vacía, false en caso contrario.
	 */
    public boolean estaVacio() {
        return filtros.isEmpty();
    }

}
