package org.srangelito.autoparts.dto;

import org.srangelito.autoparts.entity.ProductEntity;

public class ProductDto {
    private String partNumber;
    private String application;
    private Short quantity;
    private Float privatePrice;
    private Float publicPrice;

    public ProductDto(ProductEntity productEntity) {
       this.partNumber = productEntity.getPartNumber();
       this.application = productEntity.getApplication();
       this.quantity = productEntity.getQuantity();
       this.privatePrice = productEntity.getPrivatePrice();
       this.publicPrice = productEntity.getPublicPrice();
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
