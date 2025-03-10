package vista;
import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.List;
import appChat.Contacto;

public class ModeloListaContactos extends AbstractListModel<Contacto> {
    private List<Contacto> contactos;

    public ModeloListaContactos(List<Contacto> contactos) {
        this.contactos = (contactos != null) ? new ArrayList<>(contactos) : new ArrayList<>();
    }

    @Override
    public int getSize() {
        return contactos.size();
    }

    @Override
    public Contacto getElementAt(int index) {
        return contactos.get(index);
    }

    public void actualizarDatos(List<Contacto> nuevosContactos) {
        contactos.clear();
        contactos.addAll(nuevosContactos);
        fireContentsChanged(this, 0, contactos.size() - 1);
    }
}
