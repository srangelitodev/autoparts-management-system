package org.srangelito.autoparts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.srangelito.autoparts.dto.SaleDto;
import org.srangelito.autoparts.entity.ProductEntity;
import org.srangelito.autoparts.entity.SaleEntity;
import org.srangelito.autoparts.enums.DateComponent;
import org.srangelito.autoparts.repository.SaleRepository;
import org.srangelito.autoparts.utils.ExcelUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {
    private SaleRepository saleRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public void insertSale(SaleEntity saleEntity)  {
        saleRepository.save(saleEntity);
    }

    public List<SaleEntity> getAllSales() {
        return saleRepository.findAll(Sort.by("date"));
    }

    public List<SaleEntity> getSalesByDateComponentMatch(List<SaleEntity> SalesEntities, LocalDate dateToSearch, DateComponent dateComponent) {
        ArrayList<SaleEntity> resultSalesEntities = new ArrayList<>();

        switch (dateComponent) {
            case YEAR:
                for (SaleEntity saleEntity : SalesEntities)
                    if (saleEntity.getDate().getYear() == dateToSearch.getYear())
                        resultSalesEntities.add(saleEntity);
            break;
            case MONTH:
                for (SaleEntity saleEntity : SalesEntities)
                    if (saleEntity.getDate().getMonth() == dateToSearch.getMonth())
                        resultSalesEntities.add(saleEntity);
            break;
            case DAY:
                for (SaleEntity saleEntity : SalesEntities)
                    if (saleEntity.getDate().getDayOfMonth() == dateToSearch.getDayOfMonth())
                        resultSalesEntities.add(saleEntity);;
            break;
       }

       return resultSalesEntities;
    }

    public List<SaleEntity> getSalesByAllDateComponentsMatch(List<SaleEntity> salesEntities, LocalDate dateToSearch, List<DateComponent> dateComponents) {
        for (DateComponent dateComponent : dateComponents)
            salesEntities = this.getSalesByDateComponentMatch(salesEntities, dateToSearch, dateComponent);

        return salesEntities;
    }

    public List<SaleDto> saleEntityToSaleDto(List<SaleEntity> SalesEntities) {
        ArrayList<SaleDto> saleDtos = new ArrayList<>();
        for (SaleEntity saleEntity : SalesEntities)
            saleDtos.add(new SaleDto(saleEntity));

        return saleDtos;
    }

    public byte[] getSalesExcelReport() throws IOException {
        List<SaleEntity> sales = saleRepository.findAll();
        return ExcelUtils.buildExcelReport(sales);
    }
}
