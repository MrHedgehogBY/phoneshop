package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderBuilder;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderDataDTO;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao jdbcOrderDao;

    @Resource
    private StockDao jdbcStockDao;

    @Override
    public Long placeOrder(Cart cart, OrderDataDTO orderDataDTO, Long deliveryPrice) {
        OrderBuilder orderBuilder = new OrderBuilder(cart, orderDataDTO, deliveryPrice);
        Order order = orderBuilder.getOrder();
        order.getOrderItems().forEach(orderItem -> {
            Optional<Stock> optionalStock = jdbcStockDao.get(orderItem.getPhone().getId());
            if (optionalStock.isPresent()) {
                Stock stock = optionalStock.get();
                jdbcStockDao.update(orderItem.getPhone().getId(), stock.getStock() - orderItem.getQuantity(),
                        stock.getReserved().longValue());
            } else {
                throw new NoElementWithSuchIdException(orderItem.getPhone().getId());
            }
        });
        return jdbcOrderDao.save(order);
    }
}
