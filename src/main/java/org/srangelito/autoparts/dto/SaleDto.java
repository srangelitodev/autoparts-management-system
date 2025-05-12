package org.srangelito.autoparts.dto;

import org.srangelito.autoparts.entity.SaleEntity;

import java.time.LocalDate;

public class SaleDto {
    private String partNumber;
    private LocalDate date;
    private Short quantity;
    private Float total;

    public SaleDto(SaleEntity saleEntity) {
        this.partNumber = saleEntity.getPartNumber();
        this.date = saleEntity.getDate();
        this.quantity = saleEntity.getQuantity();
        this.total = saleEntity.getTotal();
    }

    public String getPartNumber() {
        return this.partNumber;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Short getQuantity() {
        return this.quantity;
    }

    public Float getTotal() {
        return this.total;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setDate(LocalDate saleDate) {
        this.date = saleDate;
    }

    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }

    public void setTotal(Float saleTotal) {
        this.total = saleTotal;
    }
}
