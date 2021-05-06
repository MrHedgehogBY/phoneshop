package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDataDTO;

public interface OrderService {
    Order createOrder(Cart cart, OrderDataDTO orderDataDTO, Long deliveryPrice);

    void placeOrder(Order order);
}
