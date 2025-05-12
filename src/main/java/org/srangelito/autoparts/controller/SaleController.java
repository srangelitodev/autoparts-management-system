package org.srangelito.autoparts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.srangelito.autoparts.dto.SaleDto;
import org.srangelito.autoparts.entity.ProductEntity;
import org.srangelito.autoparts.entity.SaleEntity;
import org.srangelito.autoparts.enums.DateComponent;
import org.srangelito.autoparts.service.ProductService;
import org.srangelito.autoparts.service.SaleService;
import org.srangelito.autoparts.utils.HttpUtils;
import org.srangelito.autoparts.utils.PageUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SaleController {
    private final SaleService saleService;
    private final ProductService productService;
    private List<SaleDto> saleDtos;

    @Autowired
    public SaleController(SaleService saleService, ProductService productService) {
        this.saleService = saleService;
        this.productService = productService;
    }

    @PostMapping ("/product/sale")
    public String saleProduct(@RequestParam (name = "salePartNumber") String partNumber, @RequestParam (name = "saleQuantity") Short quantity, Model model) {
        ProductEntity productEntity = productService.getProductByPartNumber(partNumber);
        try {
            if (productEntity == null)
                model.addAttribute("outputMessageContent", "Error: El producto que estás intentando vender no existe");
            else if (productEntity.getQuantity() >= quantity) {
                SaleEntity saleEntity = new SaleEntity(partNumber, LocalDate.now(), quantity, 0f);
                saleService.insertSale(saleEntity);
                model.addAttribute("outputMessageContent", "La venta se realizó con éxito");
            }
            else
                model.addAttribute("outputMessageContent", "Error: No hay suficiente cantidad del producto para realizar la venta");
        }
        catch (Exception exception) {
            model.addAttribute("outputMessageContent", "Error: Ocurrió un error inesperado al intentar vender el producto");
        }

        return "product";
    }

    @GetMapping ("/sale")
    public String showSalePage() {
        return "sale";
    }

    @GetMapping ("/sale/download")
    public Object downloadExcelData(Model model) {
        try {
            byte[] excelProductReportInBytes = saleService.getSalesExcelReport();
            HttpHeaders httpHeaders = HttpUtils.buildAttachmentHeaders("application/vnd.ms-excel", "Reporte de Faltantes");
            return HttpUtils.buildOkResponseEntity(httpHeaders, excelProductReportInBytes);
        }
        catch (Exception exception) {
            model.addAttribute("outputMessageContent", "Error: Ocurrió un error al intentar descargar el reporte de los faltantes");
            return "sale";
        }
    }

    @GetMapping ("/sale/search")
    public String showSales(@RequestParam (name = "criterion") List<String> criteria, @RequestParam (name = "dateToSearch") LocalDate dateToSearch, Model model) {
        ArrayList<DateComponent> dateComponents = new ArrayList<>();
        List<SaleDto> saleDtos;
        List<SaleEntity> saleEntities = saleService.getAllSales();

        for(String criterion : criteria) {
            switch (criterion) {
                case "year":
                    dateComponents.add(DateComponent.YEAR);
                break;
                case "month":
                    dateComponents.add(DateComponent.MONTH);
                break;
                case "day":
                    dateComponents.add(DateComponent.DAY);
                break;
            }
        }

        saleEntities = saleService.getSalesByAllDateComponentsMatch(saleEntities, dateToSearch, dateComponents);
        saleDtos = saleService.saleEntityToSaleDto(saleEntities);
        this.saleDtos = saleDtos;
        Page<SaleDto> pageSaleDtos = PageUtils.convertListToPage(saleDtos, 0, 30);
        model.addAttribute("sales", pageSaleDtos);
        model.addAttribute("searchRequest", true);

        return "sale";
    }

    @GetMapping ("/sale/next")
    public String showNextSales(@RequestParam (name = "pageNumber") int pageNumber, Model model) {
        Page<SaleDto> saleDtos = PageUtils.convertListToPage(this.saleDtos, pageNumber, 30);
        model.addAttribute("sales", saleDtos);
        return "sale";
    }

    @GetMapping ("/sale/previous")
    public String showPreviousSales(@RequestParam (name = "pageNumber") int pageNumber, Model model) {
        Page<SaleDto> saleDtos = PageUtils.convertListToPage(this.saleDtos, pageNumber, 30);
        model.addAttribute("sales", saleDtos);
        return "sale";
    }
}
