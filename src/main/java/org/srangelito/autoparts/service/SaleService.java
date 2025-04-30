package org.srangelito.autoparts.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.srangelito.autoparts.dto.SaleDto;
import org.srangelito.autoparts.entity.SaleEntity;
import org.srangelito.autoparts.enums.DateComponent;
import org.srangelito.autoparts.exceptions.UnsupportedOperationException;
import org.srangelito.autoparts.repository.SaleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {
    private SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public void insertSale(SaleEntity saleEntity) throws UnsupportedOperationException {
        if(saleRepository.existsById(saleEntity.getId()))
            throw new UnsupportedOperationException("Error: El identificador de la venta ya existe, por lo que no es posible completar la operación.");
        saleRepository.save(saleEntity);
    }

    public List<SaleDto> getAllSales(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 30, Sort.by("sale_date").descending());
        Page<SaleEntity> salesEntities = saleRepository.findAll(pageable);
        ArrayList<SaleDto> salesDtos = new ArrayList<>();

        for (SaleEntity saleEntity : salesEntities) {
            salesDtos.add(new SaleDto(saleEntity));
        }

        return salesDtos;
    }

    public List<SaleDto> getSalesByDateComponentMatch(String stringToMatch, DateComponent dateComponent, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 30, Sort.by("sale_date").descending());
        Page<SaleEntity> salesEntities = saleRepository.findAll(pageable);
        ArrayList<SaleDto> salesDtos = new ArrayList<>();

        switch (dateComponent) {
            case YEAR:
                for (SaleEntity saleEntity : salesEntities)
                    if (String.valueOf(saleEntity.getDate().getYear()).equals(stringToMatch))
                        salesDtos.add(new SaleDto(saleEntity));
            break;
            case MONTH:
                for (SaleEntity saleEntity : salesEntities)
                    if (String.valueOf(saleEntity.getDate().getMonth()).equals(stringToMatch))
                        salesDtos.add(new SaleDto(saleEntity));
            break;
            case DAY:
                for (SaleEntity saleEntity : salesEntities)
                    if (String.valueOf(saleEntity.getDate().getDayOfMonth()).equals(stringToMatch))
                        salesDtos.add(new SaleDto(saleEntity));
            break;
       }

       return salesDtos;
    }
}
