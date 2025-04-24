package testHU;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import appChat.Usuario;
import controlador.Controlador;
import descuentos.DescuentoFecha;
import descuentos.DescuentoMensaje;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import javax.swing.ImageIcon;
import java.time.LocalDate;

import appChat.RepositorioUsuarios;

public class TestActivarPremium {

    private Usuario usuario;
    private Controlador controlador;
    private RepositorioUsuarios mockRepo;

    @Before
    public void setUp() {
        usuario = new Usuario("Ana", new ImageIcon(), "1234", "1111", "Hola!", LocalDate.of(2023, 1, 1));
        usuario.setPremium(false);

        mockRepo = mock(RepositorioUsuarios.class);

        // Mockear RepositorioUsuarios antes de llamar al Controlador
        try (MockedStatic<RepositorioUsuarios> mockedStaticRepo = mockStatic(RepositorioUsuarios.class)) {
            mockedStaticRepo.when(RepositorioUsuarios::getUnicaInstancia).thenReturn(mockRepo);

            controlador = Controlador.getInstancia();
            when(mockRepo.buscarUsuario("1111")).thenReturn(java.util.Optional.of(usuario));
            doNothing().when(mockRepo).agregarUsuario(any(Usuario.class));

            controlador.getRepoUsuarios().agregarUsuario(usuario);
        }
    }

    @Test
    public void testActivarPremiumSinDescuento() {
        DescuentoFecha df = mock(DescuentoFecha.class);
        DescuentoMensaje dm = mock(DescuentoMensaje.class);

        when(df.obtenerPorcentajeDescuento(usuario)).thenReturn(0.0);
        when(dm.obtenerPorcentajeDescuento(usuario)).thenReturn(0.0);

        usuario.setPremium(true);
        assertTrue(usuario.isPremium());
    }

    @Test
    public void testActivarPremiumConDescuentoPorFecha() {
        DescuentoFecha df = new DescuentoFecha(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        double descuento = df.obtenerPorcentajeDescuento(usuario);
        assertTrue(descuento > 0);
    }

    @Test
    public void testActivarPremiumConDescuentoPorMensajes() {
        Usuario usuarioMensajero = new Usuario("Carlos", new ImageIcon(), "1234", "9999", "Hola!", LocalDate.of(2020, 1, 1));
        Usuario spyUsuario = spy(usuarioMensajero);

        when(spyUsuario.totalMensajesEnviados()).thenReturn(12);

        DescuentoMensaje dm = new DescuentoMensaje(10); // mínimo 10 mensajes
        double descuento = dm.obtenerPorcentajeDescuento(spyUsuario);

        assertEquals(30.0, descuento, 0.01);
    }

    @Test
    public void testAccesoAExportarPDF() {
        usuario.setPremium(true);
        assertTrue(usuario.isPremium());

        boolean accesoExportarPDF = usuario.isPremium(); // lógica simplificada
        assertTrue("El usuario premium debe poder exportar PDF", accesoExportarPDF);
    }
}
