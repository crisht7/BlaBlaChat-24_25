package testAFallos;

import static org.mockito.Mockito.*;

import java.awt.Image;

import static org.junit.Assert.*;

import appChat.Usuario;
import controlador.Controlador;
import vista.VentanaLogin;
import vista.VentanaMain;
import vista.VentanaPerfil;

import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class TestCerrarSesionPersistencia {

    @Test
    public void testCerrarSesionPersisteCorrectamente() {
        // Mockeamos las clases estáticas
        try (MockedStatic<Controlador> controladorMockedStatic = mockStatic(Controlador.class);
             MockedStatic<VentanaMain> ventanaMainMockedStatic = mockStatic(VentanaMain.class);
             MockedConstruction<VentanaLogin> ventanaLoginMockedConstruction = mockConstruction(VentanaLogin.class)) {

            // Mock de Controlador
            Controlador mockControlador = mock(Controlador.class);
            controladorMockedStatic.when(Controlador::getInstancia).thenReturn(mockControlador);

            // Mock de VentanaMain
            VentanaMain mockVentanaMain = mock(VentanaMain.class);
            ventanaMainMockedStatic.when(VentanaMain::getInstancia).thenReturn(mockVentanaMain);

            // Mock de usuario actual
            Usuario usuarioMock = mock(Usuario.class);
            when(mockControlador.getUsuarioActual()).thenReturn(usuarioMock);
            
            ImageIcon imagenDummy = mock(ImageIcon.class);
            Image imagenReal = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB); // imagen real vacía
            when(imagenDummy.getImage()).thenReturn(imagenReal);
            when(usuarioMock.getFotoPerfil()).thenReturn(imagenDummy);
            // Creamos la ventana a testear (sin necesidad de JFrame real)
            VentanaPerfil ventanaPerfil = new VentanaPerfil(new JFrame());

            // Simulamos que el usuario confirma cerrar sesión
            // Para ello mockeamos JOptionPane.showConfirmDialog
            try (MockedStatic<JOptionPane> jOptionPaneMockedStatic = mockStatic(JOptionPane.class)) {
                jOptionPaneMockedStatic.when(() -> 
                    JOptionPane.showConfirmDialog(any(), anyString(), anyString(), anyInt())
                ).thenReturn(JOptionPane.YES_OPTION);

                try (MockedStatic<SwingUtilities> swingUtilitiesMockedStatic = mockStatic(SwingUtilities.class)) {
                    swingUtilitiesMockedStatic.when(() -> SwingUtilities.invokeLater(any()))
                        .thenAnswer(invocation -> {
                            Runnable runnable = invocation.getArgument(0);
                            runnable.run(); // ejecuta inmediatamente
                            return null;
                        });
                   
                
                
                    // PARA HACER EL TEST, HAY QUE DESCOMENTAR ESTA SECCION Y PONER EL METODO EN PUBLICO, POR TEMAS 
                    // DE INTEGRIDAD, LO DEJAMOS COMENTADO
                    //ventanaPerfil.cerrarSesion();
                }
                // Verificamos que se llamó a setUsuarioActual(null)
                verify(mockControlador).setUsuarioActual(null);

                // Verificamos que se llamó a cerrar la VentanaMain
                verify(mockVentanaMain).dispose();

                // Verificamos que se creó una nueva instancia de VentanaLogin
                assertEquals(1, ventanaLoginMockedConstruction.constructed().size());

                // Verificamos que el VentanaLogin fue hecho visible
                VentanaLogin ventanaLoginCreada = ventanaLoginMockedConstruction.constructed().get(0);
                verify(ventanaLoginCreada).setVisible(true);
            }
        }
    }
}
