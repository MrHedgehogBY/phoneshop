package com.es.core.model.phone;

import com.es.core.model.cart.CartItem;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class PhoneArrayDTO {

    @Valid
    private List<PhoneDTO> phoneDTOItems;

    public PhoneArrayDTO() {

    }

    public PhoneArrayDTO(List<CartItem> cartItems) {
        phoneDTOItems = new ArrayList<>();
        cartItems.forEach(cartItem -> {
            phoneDTOItems.add(new PhoneDTO(cartItem.getPhone().getId(), cartItem.getQuantity()));
        });
    }

    public List<PhoneDTO> getPhoneDTOItems() {
        return phoneDTOItems;
    }

    public void setPhoneDTOItems(List<PhoneDTO> phoneDTOItems) {
        this.phoneDTOItems = phoneDTOItems;
    }
}
