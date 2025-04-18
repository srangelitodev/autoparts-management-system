package org.srangelito.autoparts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srangelito.autoparts.entity.ProductEntity;
import org.srangelito.autoparts.repository.ProductRepository;
import org.srangelito.autoparts.utils.ExcelUtils;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public byte[] getProductsExcelReport() throws IOException {
        List<ProductEntity> products = productRepository.findAll();
        return ExcelUtils.buildProductsExcelReport(products);
    }


}
