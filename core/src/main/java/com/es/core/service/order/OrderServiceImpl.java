package com.es.core.service.order;

import com.es.core.model.cart.Cart;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDataDTO;
import com.es.core.model.order.OrderBuilder;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private StockDao stockDao;

    @Override
    public Order getOrder(String id) {
        Optional<Order> order;
        try {
            order = orderDao.get(Long.valueOf(id));
        } catch (NumberFormatException | EmptyResultDataAccessException e) {
            throw new NoElementWithSuchIdException(id);
        }
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new NoElementWithSuchIdException(id);
        }
    }

    @Override
    public Long placeOrder(Cart cart, OrderDataDTO orderDataDTO, Long deliveryPrice) {
        OrderBuilder orderBuilder = new OrderBuilder(cart, orderDataDTO, deliveryPrice);
        Order order = orderBuilder.getOrder();
        order.getOrderItems().forEach(orderItem -> {
            Optional<Stock> optionalStock = stockDao.get(orderItem.getPhone().getId());
            if (optionalStock.isPresent()) {
                Stock stock = optionalStock.get();
                stockDao.update(orderItem.getPhone().getId(), stock.getStock() - orderItem.getQuantity(),
                        stock.getReserved().longValue());
            } else {
                throw new NoElementWithSuchIdException(orderItem.getPhone().getId().toString());
            }
        });
        order.setOrderPlacingDate(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)));
        return orderDao.save(order);
    }

    @Override
    public List<Order> findAllOrders(int offset, int limit) {
        return orderDao.findAll(offset, limit);
    }

    @Override
    public Long countAllOrders() {
        return orderDao.count();
    }

    @Override
    public void updateStatus(Long key, OrderStatus status) {
        orderDao.updateStatus(key, status);
    }
}
