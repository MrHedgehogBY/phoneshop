package com.es.core.service.order;

import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDataDTO;
import com.es.core.model.order.OrderStatus;

import java.util.List;

public interface OrderService {
    Order getOrder(String id);

    Long placeOrder(Cart cart, OrderDataDTO orderDataDTO, Long deliveryPrice);

    List<Order> findAllOrders(int offset, int limit);

    Long countAllOrders();

    void updateStatus(Long key, OrderStatus status);
}
