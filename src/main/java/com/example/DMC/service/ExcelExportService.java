package com.example.DMC.service;

import com.example.DMC.model.Producto;
import com.example.DMC.model.Venta;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] exportarVentas(List<Venta> ventas) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte de Ventas");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID Venta", "Fecha", "ID Cliente", "ID Usuario", "Método de Pago", "Total", "Estado"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            double totalGeneral = 0.0;

            for (Venta venta : ventas) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(venta.getIdVenta());

                Cell dateCell = row.createCell(1);
                dateCell.setCellValue(venta.getFechaVenta().format(DATE_FORMATTER));
                dateCell.setCellStyle(dateStyle);

                row.createCell(2).setCellValue(venta.getIdCliente() != null ? venta.getIdCliente() : 0);
                row.createCell(3).setCellValue(venta.getIdUsuario());
                row.createCell(4).setCellValue(venta.getMetodoPago());

                Cell totalCell = row.createCell(5);
                totalCell.setCellValue(venta.getTotalVenta());
                totalCell.setCellStyle(currencyStyle);

                row.createCell(6).setCellValue(venta.getEstado().toString());

                totalGeneral += venta.getTotalVenta();
            }

            // Fila de totales
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(4);
            totalLabelCell.setCellValue("TOTAL GENERAL:");
            totalLabelCell.setCellStyle(headerStyle);

            Cell totalValueCell = totalRow.createCell(5);
            totalValueCell.setCellValue(totalGeneral);
            totalValueCell.setCellStyle(currencyStyle);

            // Auto-ajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel de ventas", e);
        }
    }

    public byte[] exportarInventario(List<Producto> productos) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte de Inventario");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Código Barra", "Nombre", "Categoría", "Stock", "Precio Venta", "Valor Total", "Estado"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            double valorTotal = 0.0;

            for (Producto producto : productos) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(producto.getIdProducto());
                row.createCell(1).setCellValue(producto.getCodigoBarra() != null ? producto.getCodigoBarra() : "");
                row.createCell(2).setCellValue(producto.getNombre());
                row.createCell(3).setCellValue(producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin categoría");
                row.createCell(4).setCellValue(producto.getStock());

                Cell precioCell = row.createCell(5);
                precioCell.setCellValue(producto.getPrecioVenta().doubleValue());
                precioCell.setCellStyle(currencyStyle);

                double valorProducto = producto.getPrecioVenta().doubleValue() * producto.getStock();
                Cell valorCell = row.createCell(6);
                valorCell.setCellValue(valorProducto);
                valorCell.setCellStyle(currencyStyle);

                row.createCell(7).setCellValue(producto.getActivo() ? "Activo" : "Inactivo");

                valorTotal += valorProducto;
            }

            // Fila de totales
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(5);
            totalLabelCell.setCellValue("VALOR TOTAL:");
            totalLabelCell.setCellStyle(headerStyle);

            Cell totalValueCell = totalRow.createCell(6);
            totalValueCell.setCellValue(valorTotal);
            totalValueCell.setCellStyle(currencyStyle);

            // Auto-ajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel de inventario", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
