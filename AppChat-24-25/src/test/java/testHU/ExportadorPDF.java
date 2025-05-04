package testHU;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import appChat.Contacto;
import appChat.Mensaje;
import appChat.Usuario;

import java.io.FileOutputStream;
import java.util.List;

public class ExportadorPDF {
	
    public boolean exportar(Usuario usuario, Contacto contacto, List<Mensaje> mensajes, String rutaArchivo) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
            document.open();

            document.add(new Paragraph("Historial de conversación entre " +
                usuario.getNombre() + " y " + contacto.getNombre()));
            document.add(new Paragraph(" "));

            mensajes.stream()
                    .sorted()
                    .forEach(m -> {
                        try {
                            String emisor = m.getEmisor().getNombre();
                            String fecha = m.getHora().toString();
                            String texto = m.getTexto().isEmpty() ? "[Emoticono]" : m.getTexto();
                            document.add(new Paragraph(emisor + " (" + fecha + "): " + texto));
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    });

            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
