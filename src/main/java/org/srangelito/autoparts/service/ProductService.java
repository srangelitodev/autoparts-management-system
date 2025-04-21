package org.srangelito.autoparts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.srangelito.autoparts.dto.ProductDto;
import org.srangelito.autoparts.entity.ProductEntity;
import org.srangelito.autoparts.enums.ProductSearchOption;
import org.srangelito.autoparts.exceptions.UnexpectedErrorException;
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

    public byte[] getProductsExcelReport() throws IOException {
        List<ProductEntity> products = productRepository.findAll();
        return ExcelUtils.buildProductsExcelReport(products);
    }

    public void upsertProduct(ProductEntity product) {
        productRepository.save(product);
    }

    public void deleteProduct(ProductEntity product) {
        productRepository.delete(product);
    }

    public List<ProductDto> getProducts(String stringToSearch, int pageNumber, ProductSearchOption productSearchOption) throws UnexpectedErrorException {
        Page<ProductEntity> productEntities = null;
        String sortAttribute = productSearchOption == ProductSearchOption.PART_NUMBER ? "part_number" : "application";
        PageRequest pageRequest = PageRequest.of(pageNumber, 30, Sort.by(sortAttribute));
        ArrayList<ProductDto> productDtos = new ArrayList<>();

        switch (productSearchOption) {
            case PART_NUMBER:
                productEntities = productRepository.findAllByPartNumberContainingIgnoreCase(stringToSearch, pageRequest);
            break;
            case APPLICATION:
                productEntities = productRepository.findAllByApplicationContainingIgnoreCase(stringToSearch, pageRequest);
            break;
        }

        if (productEntities == null)
            throw new UnexpectedErrorException("Error: Se produjo un problema al intentar recuperar los productos.");

        for (ProductEntity productEntity : productEntities) {
            productDtos.add(new ProductDto(productEntity));
        }

        return productDtos;
    }

}
