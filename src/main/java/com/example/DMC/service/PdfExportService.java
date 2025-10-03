package com.example.DMC.service;

import com.example.DMC.model.Producto;
import com.example.DMC.model.Venta;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_SIMPLE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] exportarVentas(List<Venta> ventas, LocalDate fechaInicio, LocalDate fechaFin) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph title = new Paragraph("REPORTE DE VENTAS")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Período
            String periodo = String.format("Período: %s - %s",
                    fechaInicio != null ? fechaInicio.format(DATE_SIMPLE) : "Inicio",
                    fechaFin != null ? fechaFin.format(DATE_SIMPLE) : "Fin");
            Paragraph periodoPara = new Paragraph(periodo)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(periodoPara);

            // Tabla
            float[] columnWidths = {1, 3, 2, 2, 3, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Encabezados
            String[] headers = {"ID", "Fecha", "ID Cliente", "ID Usuario", "Método Pago", "Total", "Estado"};
            for (String header : headers) {
                Cell cell = new Cell().add(new Paragraph(header).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER);
                table.addHeaderCell(cell);
            }

            // Datos
            double totalGeneral = 0.0;
            for (Venta venta : ventas) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(venta.getIdVenta()))));
                table.addCell(new Cell().add(new Paragraph(venta.getFechaVenta().format(DATE_FORMATTER))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(venta.getIdCliente() != null ? venta.getIdCliente() : 0))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(venta.getIdUsuario()))));
                table.addCell(new Cell().add(new Paragraph(venta.getMetodoPago())));
                table.addCell(new Cell().add(new Paragraph("$" + venta.getTotalVenta())).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph(venta.getEstado().toString())));

                totalGeneral += venta.getTotalVenta();
            }

            // Total
            table.addCell(new Cell(1, 5).add(new Paragraph("TOTAL GENERAL:").setBold())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("$" + totalGeneral).setBold())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph(""))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));

            document.add(table);

            // Pie de página
            Paragraph footer = new Paragraph("Generado el: " + LocalDate.now().format(DATE_SIMPLE))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de ventas", e);
        }
    }

    public byte[] exportarInventario(List<Producto> productos) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph title = new Paragraph("REPORTE DE INVENTARIO")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Tabla
            float[] columnWidths = {1, 2, 4, 3, 2, 2, 3, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Encabezados
            String[] headers = {"ID", "Código Barra", "Nombre", "Categoría", "Stock", "Precio", "Valor Total", "Estado"};
            for (String header : headers) {
                Cell cell = new Cell().add(new Paragraph(header).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(9);
                table.addHeaderCell(cell);
            }

            // Datos
            double valorTotal = 0.0;
            for (Producto producto : productos) {
                double valorProducto = producto.getPrecioVenta().doubleValue() * producto.getStock();

                table.addCell(new Cell().add(new Paragraph(String.valueOf(producto.getIdProducto())).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(producto.getCodigoBarra() != null ? producto.getCodigoBarra() : "").setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(producto.getNombre()).setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin categoría").setFontSize(8)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(producto.getStock())).setFontSize(8)).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph("$" + producto.getPrecioVenta()).setFontSize(8)).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph("$" + String.format("%.2f", valorProducto)).setFontSize(8)).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph(producto.getActivo() ? "Activo" : "Inactivo").setFontSize(8)).setTextAlignment(TextAlignment.CENTER));

                valorTotal += valorProducto;
            }

            // Total
            table.addCell(new Cell(1, 6).add(new Paragraph("VALOR TOTAL INVENTARIO:").setBold())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell(1, 2).add(new Paragraph("$" + String.format("%.2f", valorTotal)).setBold())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));

            document.add(table);

            // Pie de página
            Paragraph footer = new Paragraph("Generado el: " + LocalDate.now().format(DATE_SIMPLE))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de inventario", e);
        }
    }
}
