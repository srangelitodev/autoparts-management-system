package org.srangelito.autoparts.dto;

import org.srangelito.autoparts.entity.SaleEntity;

import java.time.LocalDate;

public class SaleDto {
    private String partNumber;
    private LocalDate saleDate;
    private Float saleTotal;

    public SaleDto(SaleEntity saleEntity) {
        this.partNumber = saleEntity.getPartNumber();
        this.saleDate = saleEntity.getSaleDate();
        this.saleTotal = saleEntity.getSaleTotal();
    }

    public String getPartNumber() {
        return this.partNumber;
    }

    public LocalDate getSaleDate() {
        return this.saleDate;
    }

    public Float getSaleTotal() {
        return this.saleTotal;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public void setSaleTotal(Float saleTotal) {
        this.saleTotal = saleTotal;
    }
}
