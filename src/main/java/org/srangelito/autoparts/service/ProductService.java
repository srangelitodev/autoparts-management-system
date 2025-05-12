package org.srangelito.autoparts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.srangelito.autoparts.dto.ProductDto;
import org.srangelito.autoparts.entity.ProductEntity;
import org.srangelito.autoparts.enums.ProductSearchOption;
import org.srangelito.autoparts.repository.ProductRepository;
import org.srangelito.autoparts.utils.ExcelUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean doesProductExist(String partNumber) {
        return productRepository.existsById(partNumber);
    }

    public byte[] getProductsExcelReport() throws IOException {
        List<ProductEntity> products = productRepository.findAll();
        return ExcelUtils.buildExcelReport(products);
    }

    public void upsertProduct(ProductEntity product) {
        productRepository.save(product);
    }

    public void upsertProducts(List<ProductEntity> products) {
        productRepository.saveAll(products);
    }

    public void deleteProduct(String partNumber) {
        productRepository.deleteById(partNumber);
    }

    public ProductEntity getProductByPartNumber(String partNumber) {
        return productRepository.findById(partNumber).orElse(null);
    }

    public Page<ProductEntity> getProducts(String stringToSearch, int pageNumber, ProductSearchOption productSearchOption) {
        Page<ProductEntity> productEntities = null;
        String sortAttribute = productSearchOption == ProductSearchOption.PART_NUMBER ? "partNumber" : "application";
        PageRequest pageRequest = PageRequest.of(pageNumber, 30, Sort.by(sortAttribute));

        switch (productSearchOption) {
            case PART_NUMBER:
                productEntities = productRepository.findAllByPartNumberContainingIgnoreCase(stringToSearch, pageRequest);
            break;
            case APPLICATION:
                productEntities = productRepository.findAllByApplicationContainingIgnoreCase(stringToSearch, pageRequest);
            break;
        }

        return productEntities;
    }

    public List<ProductDto> productEntityToDto(Page<ProductEntity> productEntities) {
        ArrayList<ProductDto> productDtos = new ArrayList<>();
        for (ProductEntity productEntity : productEntities) {
            productDtos.add(new ProductDto(productEntity));
        }

        return productDtos;
    }
}
