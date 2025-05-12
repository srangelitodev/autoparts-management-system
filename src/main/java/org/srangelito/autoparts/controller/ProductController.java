package org.srangelito.autoparts.controller;

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
import java.util.List;


@Controller
public class ProductController {
    private final ProductService productService;
    private String stringToSearch;
    private ProductSearchOption productSearchOption;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
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

    @PostMapping ("/product/delete")
    public String deleteProduct(@RequestParam (name = "partNumber") String partNumber, Model model) {
        productService.deleteProduct(partNumber);
        model.addAttribute("outputMessageContent", "El producto se eliminado exitosamente");
        return "product";
    }

    @PostMapping ("/product/update")
    public String editProduct(@RequestParam (name = "partNumber") String partNumber, @RequestParam (name = "application") String application, @RequestParam (name = "quantity") Short quantity, @RequestParam (name = "privatePrice") Float privatePrice, @RequestParam (name = "publicPrice") Float publicPrice, Model model) {
        ProductEntity productEntity = new ProductEntity(partNumber, application, quantity, privatePrice, publicPrice);
        productService.upsertProduct(productEntity);
        model.addAttribute("outputMessageContent", "El producto se ha editado exitosamente");
        return "product";
    }

    @GetMapping ("/product")
    public String showProductPage() {
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
            ProductSearchOption productSearchOption = null;

            switch (criterion) {
                case "partNumber":
                    productSearchOption = ProductSearchOption.PART_NUMBER;
                break;
                case "application":
                    productSearchOption = ProductSearchOption.APPLICATION;
                break;
            }

            this.stringToSearch = stringToSearch;
            this.productSearchOption = productSearchOption;

            Page<ProductEntity> productsPage = productService.getProducts(stringToSearch, 0, productSearchOption);
            List<ProductDto> productDtos = productService.productEntityToDto(productsPage);
            model.addAttribute("productsPage", productsPage);
            model.addAttribute("products", productDtos);
            model.addAttribute("searchRequest", true);
        }
        catch (Exception exception) {
            model.addAttribute("outputMessageContent", "Error: Ocurrió un error inesperado al intentar obtener los productos");
        }

        return "product";
    }

    @GetMapping ("/product/next")
    public String showNextProducts(@RequestParam (name = "pageNumber") int pageNumber, Model model) {
        Page<ProductEntity> productsPage = productService.getProducts(stringToSearch, pageNumber, productSearchOption);
        List<ProductDto> productDtos = productService.productEntityToDto(productsPage);
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("products", productDtos);

        return "product";
    }

    @GetMapping ("/product/previous")
    public String showPreviousProducts(@RequestParam (name = "pageNumber") int pageNumber, Model model) {
        Page<ProductEntity> productsPage = productService.getProducts(stringToSearch, pageNumber, productSearchOption);
        List<ProductDto> productDtos = productService.productEntityToDto(productsPage);
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("products", productDtos);

        return "product";
    }

}
