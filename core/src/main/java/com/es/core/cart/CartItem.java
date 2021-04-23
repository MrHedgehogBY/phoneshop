package com.es.core.cart;

import com.es.core.model.phone.Phone;

import java.math.BigDecimal;

public class CartItem {

    private Phone phone;
    private Long quantity;
    private BigDecimal priceForQuantity;

    public CartItem(Phone phone, Long quantity) {
        this.phone = phone;
        this.quantity = quantity;
        priceForQuantity = BigDecimal.valueOf(quantity).multiply(phone.getPrice());
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceForQuantity() {
        return priceForQuantity;
    }

    public void setPriceForQuantity(BigDecimal priceForQuantity) {
        this.priceForQuantity = priceForQuantity;
    }
}
