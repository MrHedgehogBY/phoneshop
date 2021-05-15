package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.OrderDataDTO;

public interface OrderService {
    Long placeOrder(Cart cart, OrderDataDTO orderDataDTO, Long deliveryPrice);
}
