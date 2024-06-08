/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ericksocop.controller;
import javax.swing.JOptionPane;
import org.ericksocop.bean.Facturas;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.property.TextAlignment;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

/**
 *
 * @author mauco
 */
public class FacturaReportePDFController {

    public void FacturaPDF(Facturas factura, String firmaURL, String membreteURL, String rutaProyecto) {
        try {
            String rutaArchivo = rutaProyecto + "facturaNum-" + factura.getClienteID() + ".pdf";
            PdfWriter writer = new PdfWriter(rutaArchivo);
            PdfDocument pdf = new PdfDocument(writer);
            DeviceRgb colorN = new DeviceRgb(255, 121, 54);
            DeviceRgb colorGet = new DeviceRgb(0, 0, 0);
            Document document = new Document(pdf);
            PdfFont fuenteN = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fuenteGet = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            if (membreteURL != null && !membreteURL.isEmpty()) {
                Image membrete = new Image(ImageDataFactory.create(membreteURL));
                membrete.setWidth(2000);
                document.add(membrete);
            }
            if (firmaURL != null && !firmaURL.isEmpty()) {
                Image firma = new Image(ImageDataFactory.create(firmaURL));
                firma.setWidth(100);
                firma.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                document.add(firma);
            }

            float[] ancho = {200f, 300f}; 
            Table tabla = new Table(ancho);
            tabla.addCell(new Cell().add(new Paragraph("Número de factura ")).setFontColor(colorN).setFont(fuenteN));
            tabla.addCell(new Cell().add(new Paragraph(String.valueOf(factura.getNumeroDeFactura())).setTextAlignment(TextAlignment.RIGHT).setFontColor(colorGet).setFont(fuenteGet)));
            tabla.addCell(new Cell().add(new Paragraph("Estado  ")).setFontColor(colorN).setFont(fuenteN));
            tabla.addCell(new Cell().add(new Paragraph(factura.getEstado()).setTextAlignment(TextAlignment.RIGHT).setFontColor(colorGet).setFont(fuenteGet)));
            tabla.addCell(new Cell().add(new Paragraph("Total ")).setFontColor(colorN).setFont(fuenteN));
            tabla.addCell(new Cell().add(new Paragraph(String.valueOf(factura.getTotalFactura())).setTextAlignment(TextAlignment.RIGHT).setFontColor(colorGet).setFont(fuenteGet)));
            tabla.addCell(new Cell().add(new Paragraph("Fecha ")).setFontColor(colorN).setFont(fuenteN));
            tabla.addCell(new Cell().add(new Paragraph(convertirFecha(factura.getFechaFactura())).setTextAlignment(TextAlignment.RIGHT).setFontColor(colorGet).setFont(fuenteGet)));
            tabla.addCell(new Cell().add(new Paragraph("Cliente ")).setFontColor(colorN).setFont(fuenteN));
            tabla.addCell(new Cell().add(new Paragraph(String.valueOf(factura.getClienteID())).setTextAlignment(TextAlignment.RIGHT).setFontColor(colorGet).setFont(fuenteGet)));
            tabla.addCell(new Cell().add(new Paragraph("Código del Empleado ")).setFontColor(colorN).setFont(fuenteN));
            tabla.addCell(new Cell().add(new Paragraph(String.valueOf(factura.getCodigoEmpleado())).setTextAlignment(TextAlignment.RIGHT).setFontColor(colorGet).setFont(fuenteGet)));
            document.add(tabla);
            document.close();
            JOptionPane.showMessageDialog(null, "El PDF ha sido generado exitosamente","MENSAJE",INFORMATION_MESSAGE);
            System.out.println("El PDF ha sido generado en: " + rutaArchivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "El PDF ha sido generado exitosamente", "ERROR", ERROR_MESSAGE);
            System.err.println("Error al generar el PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String convertirFecha(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatoFecha.parse(fecha);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(date);
        } catch (Exception e) {
            return fecha;
        }
    }
}
