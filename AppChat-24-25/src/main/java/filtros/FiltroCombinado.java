package filtros;

import java.util.ArrayList;
import java.util.List;
import appChat.Mensaje;

/**
 * Filtro que permite combinar múltiples filtros aplicándolos en secuencia.
 */
public class FiltroCombinado implements FiltroBusqueda {
    private List<FiltroBusqueda> filtros;

    public FiltroCombinado() {
        this.filtros = new ArrayList<>();
    }

    public void añadirFiltro(FiltroBusqueda filtro) {
        if (filtro != null) {
            filtros.add(filtro);
        }
    }

    public boolean eliminarFiltro(FiltroBusqueda filtro) {
        return filtros.remove(filtro);
    }

    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        List<Mensaje> resultado = new ArrayList<>(mensajes);
        for (FiltroBusqueda filtro : filtros) {
            resultado = filtro.filtrar(resultado);
        }
        return resultado;
    }

    public List<FiltroBusqueda> getFiltros() {
        return new ArrayList<>(filtros);
    }
    
    public boolean estaVacio() {
        return filtros.isEmpty();
    }

}
