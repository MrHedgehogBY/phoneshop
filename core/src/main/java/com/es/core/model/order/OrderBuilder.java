package com.es.core.model.order;

import com.es.core.cart.Cart;

import java.math.BigDecimal;

public class OrderBuilder {

    private Cart cart;
    private OrderDataDTO orderDataDTO;
    private Long deliveryPrice;
    private Order order;

    public OrderBuilder(Cart cart, OrderDataDTO orderDataDTO, Long deliveryPrice) {
        this.cart = cart;
        this.orderDataDTO = orderDataDTO;
        this.deliveryPrice = deliveryPrice;
        order = new Order();
    }

    public Order getOrder() {
        order.setFirstName(orderDataDTO.getFirstName());
        order.setLastName(orderDataDTO.getLastName());
        order.setContactPhoneNo(orderDataDTO.getPhone());
        order.setDeliveryAddress(orderDataDTO.getAddress());
        order.setAdditionalInformation(orderDataDTO.getAdditionalInformation());
        cart.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem(cartItem, order);
            order.getOrderItems().add(orderItem);
        });
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryPrice(BigDecimal.valueOf(deliveryPrice));
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));
        order.setStatus(OrderStatus.NEW);
        return order;
    }
}
