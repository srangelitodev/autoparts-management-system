package org.srangelito.autoparts.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table (name = "sale")
public class SaleEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE)
    @Column (name = "id")
    private Integer id;
    @Column (name = "part_number")
    private String partNumber;
    @Column (name = "date")
    private LocalDate date;
    @Column (name = "quantity")
    private Short quantity;
    @Column (name = "total")
    private Float total;

    public SaleEntity() {
    }

    public SaleEntity(String partNumber, LocalDate date, Short quantity, Float total) {
        this.partNumber = partNumber;
        this.date = date;
        this.quantity = quantity;
        this.total = total;
    }

    public Integer getId() {
        return this.id;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
}
