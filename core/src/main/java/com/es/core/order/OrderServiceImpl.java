package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderDataDTO;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
@PropertySource("classpath:/properties/application-service.properties")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao jdbcOrderDao;

    @Resource
    private StockDao jdbcStockDao;

    private Long validId = 0L;

    @Override
    public Order createOrder(Cart cart, OrderDataDTO orderDataDTO, Long deliveryPrice) {
        return new Order(getValidId(), cart, orderDataDTO, deliveryPrice);
    }

    @Override
    public void placeOrder(Order order) {
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
        jdbcOrderDao.save(order);
    }

    private Long getValidId() {
        validId++;
        return validId;
    }
}
