package com.es.core.model.b2b;

import javax.validation.Valid;
import java.util.List;

public class B2bCartDTO {

    @Valid
    private List<B2bCartItemDTO> b2bCartItems;

    public List<B2bCartItemDTO> getB2bCartItems() {
        return b2bCartItems;
    }

    public void setB2bCartItems(List<B2bCartItemDTO> b2bCartItems) {
        this.b2bCartItems = b2bCartItems;
    }
}
