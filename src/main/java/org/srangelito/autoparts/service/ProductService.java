package org.srangelito.autoparts.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srangelito.autoparts.entity.ProductEntity;
import org.srangelito.autoparts.repository.ProductRepository;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void setProductRowValues(Row targetRow, Object... values) {
        int cellIndex = 0;

        for (Object value : values) {
            targetRow.createCell(cellIndex).setCellValue(value.toString());
            cellIndex++;
        }
    }

    public byte[] generateExcelReport() throws Exception {
        List<ProductEntity> products = productRepository.findAll();
        Workbook excelWorkbook = new HSSFWorkbook();
        Sheet excelSheet = excelWorkbook.createSheet();
        this.setProductRowValues(excelSheet.createRow(0), "Part Number", "Application", "Quantity", "Private Price", "Public Price");

        int rowIndex = 1;
        for (ProductEntity product : products) {
            this.setProductRowValues(excelSheet.createRow(rowIndex), product.getPartNumber(), product.getApplication(), product.getQuantity(), product.getPrivatePrice(), product.getPublicPrice());
            rowIndex++;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        excelWorkbook.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
