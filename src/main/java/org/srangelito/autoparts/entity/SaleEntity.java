package org.srangelito.autoparts.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table (name = "sale")
public class SaleEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE)
    @Column (name = "sale_id")
    private Integer saleId;
    @Column (name = "part_number")
    private String partNumber;
    @Column (name = "sale_date")
    private LocalDate saleDate;
    @Column (name = "sale_total")
    private Float saleTotal;

    public SaleEntity() {
    }

    public SaleEntity(Integer saleId, String partNumber, LocalDate saleDate, Float saleTotal) {
        this.saleId = saleId;
        this.partNumber = partNumber;
        this.saleDate = saleDate;
        this.saleTotal = saleTotal;
    }

    public Integer getSaleId() {
        return this.saleId;
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

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
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
