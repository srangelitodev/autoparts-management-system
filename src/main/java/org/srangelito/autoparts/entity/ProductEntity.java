package org.srangelito.autoparts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "product")
public class ProductEntity {
    @Id
    @Column (name = "part_number")
    private String partNumber;
    @Column (name = "application")
    private String application;
    @Column (name = "quantity")
    private Short quantity;
    @Column (name = "private_price")
    private Float privatePrice;
    @Column (name = "public_price")
    private Float publicPrice;

    public ProductEntity() {
    }

    public ProductEntity(String partNumber, String application, Short quantity, Float privatePrice, Float publicPrice) {
        this.partNumber = partNumber;
        this.application = application;
        this.quantity = quantity;
        this.privatePrice = privatePrice;
        this.publicPrice = publicPrice;
    }

    public String getPartNumber() {
        return this.partNumber;
    }

    public String getApplication() {
        return this.application;
    }

    public Short getQuantity() {
        return this.quantity;
    }

    public Float getPrivatePrice() {
        return this.privatePrice;
    }

    public Float getPublicPrice() {
        return this.publicPrice;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }

    public void setPrivatePrice(Float privatePrice) {
        this.privatePrice = privatePrice;
    }

    public void setPublicPrice(Float publicPrice) {
        this.publicPrice = publicPrice;
    }
}
