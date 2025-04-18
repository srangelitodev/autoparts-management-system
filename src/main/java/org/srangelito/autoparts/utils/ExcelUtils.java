package org.srangelito.autoparts.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.srangelito.autoparts.entity.ProductEntity;
import org.srangelito.autoparts.exception.DuplicateAttributeColumnException;
import org.srangelito.autoparts.exception.MissingAttributeColumnsException;
import org.srangelito.autoparts.exception.SheetTitleNotFoundException;
import org.srangelito.autoparts.exception.UnsupportedFileTypeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExcelUtils {

    private ExcelUtils() {
        throw new UnsupportedOperationException();
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell.getCellType() == CellType.STRING)
            return cell.getStringCellValue();
        else if (cell.getCellType() == CellType.NUMERIC)
            return String.valueOf((int) cell.getNumericCellValue());
        return null;
    }

    private static Float getCellValueAsFloat(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC)
            return (float) cell.getNumericCellValue();
        return null;
    }

    private static Short getCellValueAsShort(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC)
            return (short) cell.getNumericCellValue();
        return null;
    }

    private static XSSFWorkbook getXssfWorkbookByMultipartFile(MultipartFile multipartFile) throws UnsupportedFileTypeException, IOException {
        if (!multipartFile.getOriginalFilename().endsWith(".xlsx"))
            throw new UnsupportedFileTypeException("Error: Formato de archivo no válido.");
        return new XSSFWorkbook(multipartFile.getInputStream());
    }

    private static int getXssfSheetTitleRowIndex (XSSFSheet xssfSheet, int xssfSheetIndex) throws SheetTitleNotFoundException {
        for (Row row : xssfSheet) {
            for (Cell cell : row)
                if (cell.getCellType() == CellType.STRING)
                    return row.getRowNum();
        }

        throw new SheetTitleNotFoundException("Error: No se pudo encontrar el título de la hoja de cálculo número:" + (xssfSheetIndex + 1));
    }

    private static Map<String, Integer> mapAttributeColumns(XSSFSheet xssfSheet, int xssfSheetIndex, int sheetTitleRowIndex, Map<String, String> prefixToAttributeMap) throws MissingAttributeColumnsException, DuplicateAttributeColumnException {
        XSSFRow attributesRow = xssfSheet.getRow(sheetTitleRowIndex + 1);
        Map<String, Integer> attributeToIndexMap = new HashMap<>();

        for (Cell cell : attributesRow) {
            for (Map.Entry<String, String> prefixToAttribute : prefixToAttributeMap.entrySet()) {
                if (cell.getCellType() == CellType.STRING) {

                    if (cell.getStringCellValue().trim().toLowerCase().startsWith(prefixToAttribute.getKey())) {
                        if (attributeToIndexMap.get(prefixToAttribute.getValue()) == null) {
                            attributeToIndexMap.put(prefixToAttribute.getValue(), cell.getColumnIndex());
                            break;
                        }
                        else
                            throw new DuplicateAttributeColumnException("Error: Uno o más atributos están duplicados en la hoja de cálculo número:" + (xssfSheetIndex + 1));
                    }
                }
            }

            if (attributeToIndexMap.size() == 5)
                return attributeToIndexMap;
        }

        throw new MissingAttributeColumnsException("Error: Faltan algunos atributos en la hoja de cálculo número:" + (xssfSheetIndex + 1));
    }

    public static List<ProductEntity> getProductsByMultipartFile(MultipartFile multipartFile) throws Exception{
        XSSFWorkbook excelWorkbook = ExcelUtils.getXssfWorkbookByMultipartFile(multipartFile);
        ArrayList<ProductEntity> products = new ArrayList<>();
        Map<String, String> prefixToAttributeMap = Map.of(
                "can", "Quantity",
                "n", "Part Number",
                "ap", "Application",
                "pre", "Private Price",
                "cos", "Private Price",
                "pu", "Public Price"
        );

        for (int i = 0; excelWorkbook.getNumberOfSheets() > i; i++) {
            XSSFSheet excelSheet = excelWorkbook.getSheetAt(i);
            int sheetTitleRowIndex = ExcelUtils.getXssfSheetTitleRowIndex(excelSheet, i);

            Map<String, Integer> attributeToIndexMap = ExcelUtils.mapAttributeColumns(excelSheet, i, sheetTitleRowIndex, prefixToAttributeMap);

            for (int j = sheetTitleRowIndex + 2; j < excelSheet.getLastRowNum() + 1; j++) {
                XSSFRow excelRow = excelSheet.getRow(j);

                String partNumber = getCellValueAsString(excelRow.getCell(attributeToIndexMap.get("Part Number")));
                String application = getCellValueAsString(excelRow.getCell(attributeToIndexMap.get("Application")));
                Short quantity = getCellValueAsShort(excelRow.getCell(attributeToIndexMap.get("Quantity")));
                Float privatePrice = getCellValueAsFloat(excelRow.getCell(attributeToIndexMap.get("Private Price")));
                Float publicPrice = getCellValueAsFloat(excelRow.getCell(attributeToIndexMap.get("Public Price")));

                products.add(new ProductEntity(partNumber, application, quantity, privatePrice, publicPrice));
            }
        }

        return products;
    }

    private static void setRowValues(Row targetRow, Object... values) {
        int cellIndex = 0;

        for (Object value : values) {
            targetRow.createCell(cellIndex).setCellValue(value.toString());
            cellIndex++;
        }
    }

    public static byte[] buildProductsExcelReport(List<ProductEntity> products) throws IOException {
        XSSFWorkbook excelWorkbook = new XSSFWorkbook();
        XSSFSheet excelSheet = excelWorkbook.createSheet();
        ExcelUtils.setRowValues(excelSheet.createRow(0), "Part Number", "Application", "Quantity", "Private Price", "Public Price");

        int rowIndex = 1;
        for (ProductEntity product : products) {
            ExcelUtils.setRowValues(excelSheet.createRow(rowIndex), product.getPartNumber(), product.getApplication(), product.getQuantity(), product.getPrivatePrice(), product.getPublicPrice());
            rowIndex++;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        excelWorkbook.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}