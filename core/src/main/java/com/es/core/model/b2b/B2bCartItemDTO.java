package com.es.core.model.b2b;

import javax.validation.constraints.Min;

public class B2bCartItemDTO {

    private Long id;

    @Min(value = 1L)
    private Long quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
