package appChat;
import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

import appChat.RepositorioUsuarios;
import appChat.Usuario;
import controlador.Controlador;

public class TestCambioImagenPerfil {

    private Controlador controlador;
    private RepositorioUsuarios repo;

    private final String telefono = "555555555";
    private final String contraseña = "img123";
    private Usuario usuario;

    @Before
    public void setUp() {
        controlador = Controlador.getInstancia();
        repo = RepositorioUsuarios.getUnicaInstancia();

        // Eliminar usuario si ya existe
        Usuario existente = repo.getUsuario(telefono);
        if (existente != null) {
            repo.eliminarUsuario(existente);
            controlador.getAdaptadorUsuario().borrarUsuario(existente);
        }

        // Crear nuevo usuario
        controlador.registrarUsuario("Imagen Test", LocalDate.of(1990, 1, 1), new ImageIcon(), telefono, "", contraseña);
        usuario = controlador.getUsuarioActual();
    }

    @Test
    public void testCambiarImagenPerfilValida() {
        String path = "src/test/resources/imagen_valida.png"; // Debe existir en tu proyecto
        File archivo = new File(path);

        assertTrue("La imagen de prueba debería existir", archivo.exists());

        ImageIcon nuevaImagen = new ImageIcon(archivo.getAbsolutePath());
        usuario.setFotoPerfil(nuevaImagen);
        controlador.getAdaptadorUsuario().modificarUsuario(usuario);

        Usuario actualizado = repo.getUsuario(telefono);
        assertNotNull("La imagen debería haberse actualizado", actualizado.getFotoPerfil());
    }

    @Test
    public void testCambiarImagenPerfilInvalida() {
        String pathInvalido = "src/test/resources/archivo_inexistente.jpg";
        File archivo = new File(pathInvalido);

        assertFalse("El archivo no debería existir", archivo.exists());

        try {
            ImageIcon imagenFallida = new ImageIcon(archivo.getAbsolutePath());
            usuario.setFotoPerfil(imagenFallida);
            controlador.getAdaptadorUsuario().modificarUsuario(usuario);
        } catch (Exception e) {
            fail("No debería lanzar excepciones al intentar cargar imagen inválida, debe manejarse en la vista");
        }

        // No cambia la imagen porque la imagen inválida es nula
        Usuario actualizado = repo.getUsuario(telefono);
        assertNotNull("Debe mantenerse la imagen previa o ser la por defecto", actualizado.getFotoPerfil());
    }
} 
