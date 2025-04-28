package org.srangelito.autoparts.controller;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.srangelito.autoparts.dto.ProductDto;
import org.srangelito.autoparts.entity.ProductEntity;
import org.srangelito.autoparts.enums.ProductSearchOption;
import org.srangelito.autoparts.exceptions.ExcelException;
import org.srangelito.autoparts.service.ProductService;
import org.srangelito.autoparts.utils.ExcelUtils;
import org.srangelito.autoparts.utils.HttpUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class ProductController {
    private ProductService productService;
    private int pageNumber;
    private String stringToSearch;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping ("/product")
    public String showProductPage() {
        return "product";
    }

    @PostMapping ("/product/upload")
    public String uploadExcelData(@RequestParam (name = "excelFile") MultipartFile excelFile, Model model) {
        try {
        productService.upsertProducts(ExcelUtils.getProductsByMultipartFile(excelFile));
        model.addAttribute("outputMessageContent", "El archivo Excel se cargó exitosamente");
        }
        catch (Exception exception) {
            if (exception instanceof ExcelException) {
                model.addAttribute("outputMessageContent", exception.getMessage());
            }
            else
                model.addAttribute("outputMessageContent", "Error:  Ocurrió un problema al intentar cargar el archivo de Excel");
        }

        return "product";
    }

    @PostMapping ("/product/add")
    public String addProduct(@RequestParam (name = "partNumber") String partNumber, @RequestParam (name = "application") String application, @RequestParam (name = "quantity") Short quantity, @RequestParam (name = "privatePrice") Float privatePrice, @RequestParam (name = "publicPrice") Float publicPrice, Model model) {
        if (productService.doesProductExist(partNumber))
            model.addAttribute("outputMessageContent", "Error: El producto ya existe, por lo tanto no puede ser agregado");
        else {
            ProductEntity productEntity = new ProductEntity(partNumber, application, quantity, privatePrice, publicPrice);
            productService.upsertProduct(productEntity);
            model.addAttribute("outputMessageContent", "El producto se agregó exitosamente");
        }

        return "product";
    }

    @GetMapping ("/product/download")
    public Object downloadExcelData(Model model) {
        try {
            byte[] excelProductReportInBytes = productService.getProductsExcelReport();
            HttpHeaders httpHeaders = HttpUtils.buildAttachmentHeaders("application/vnd.ms-excel", "Reporte de Productos");
            return HttpUtils.buildOkResponseEntity(httpHeaders, excelProductReportInBytes);
        }
        catch (Exception exception) {
            model.addAttribute("outputMessageContent", "Error: Ocurrió un error al intentar descargar el reporte de productos");
            return "product";
        }
    }

    @GetMapping ("/product/search")
    public String showProducts(@RequestParam (name = "criterion") String criterion, @RequestParam (name = "stringToSearch") String stringToSearch, Model model) {
        try {
            this.stringToSearch = stringToSearch;
            this.pageNumber = 0;
            ProductSearchOption productSearchOption = null;

            switch (criterion) {
                case "partNumber":
                    productSearchOption = ProductSearchOption.PART_NUMBER;
                break;
                case "application":
                    productSearchOption = ProductSearchOption.APPLICATION;
                break;
            }

            Page<ProductEntity> productsPage = productService.getProducts(stringToSearch, pageNumber, productSearchOption);
            List<ProductDto> productDtos = productService.productEntityToDto(productsPage);
            model.addAttribute("productsPage", productsPage);
            model.addAttribute("products", productDtos);
        }
        catch (Exception exception) {
            model.addAttribute("outputMessageContent", "Error: Ocurrió un error inesperado al intentar obtener los productos");
        }

        return "product";
    }


}
