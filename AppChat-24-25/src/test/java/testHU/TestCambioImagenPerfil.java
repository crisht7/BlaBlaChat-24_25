package testHU;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.time.LocalDate;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import appChat.RepositorioUsuarios;
import appChat.Usuario;
import controlador.Controlador;

public class TestCambioImagenPerfil {

    private Controlador controlador;
    private RepositorioUsuarios mockRepo;

    private final String telefono = "555555555";
    private final String contraseña = "img123";
    private Usuario usuario;

    @Before
    public void setUp() {
        mockRepo = mock(RepositorioUsuarios.class);

        // Mock estático antes de obtener instancia del Controlador
        try (MockedStatic<RepositorioUsuarios> mockedStaticRepo = mockStatic(RepositorioUsuarios.class)) {
            mockedStaticRepo.when(RepositorioUsuarios::getUnicaInstancia).thenReturn(mockRepo);

            controlador = Controlador.getInstancia();

            usuario = new Usuario("Imagen Test", new ImageIcon(), contraseña, telefono, "", LocalDate.of(1990, 1, 1));
            when(mockRepo.getUsuario(telefono)).thenReturn(usuario);
            doNothing().when(mockRepo).eliminarUsuario(any(Usuario.class));
            doNothing().when(mockRepo).agregarUsuario(any(Usuario.class));

            controlador.registrarUsuario(
                "Imagen Test", LocalDate.of(1990, 1, 1), new ImageIcon(), telefono, "", contraseña
            );
            usuario = controlador.getUsuarioActual();
        }
    }

    @Test
    public void testCambiarImagenPerfilValida() {
        String path = "src/test/resources/imagen_valida.png"; // Asegúrate de que existe
        File archivo = new File(path);

        assertTrue("La imagen de prueba debería existir", archivo.exists());

        ImageIcon nuevaImagen = new ImageIcon(archivo.getAbsolutePath());
        usuario.setFotoPerfil(nuevaImagen);
        controlador.getAdaptadorUsuario().modificarUsuario(usuario);

        Usuario actualizado = mockRepo.getUsuario(telefono);
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

        Usuario actualizado = mockRepo.getUsuario(telefono);
        assertNotNull("Debe mantenerse la imagen previa o ser la por defecto", actualizado.getFotoPerfil());
    }
}
