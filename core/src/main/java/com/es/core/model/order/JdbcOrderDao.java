package com.es.core.model.order;

import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcOrderDao implements OrderDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private PhoneDao jdbcPhoneDao;

    private static final String SQL_SELECT_FOR_GET = "select * from orders where id = ";
    private static final String SQL_SELECT_FOR_MAP_ROW = "select * from orderItems where orderId = ";
    private static final String SQL_SAVE_ORDER = "insert into orders (id, subtotal, deliveryPrice, totalPrice, firstName," +
            " lastName, deliveryAddress, contactPhoneNo, additionalInformation, status) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SAVE_ORDER_ITEM = "insert into orderItems (phoneId, orderId, quantity) " +
            "values (?, ?, ?)";

    @Override
    public Optional<Order> get(Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_FOR_GET + key,
                new JdbcOrderDao.OrderBeanPropertyRowMapper()));
    }

    @Override
    public void save(Order order) {
        jdbcTemplate.update(SQL_SAVE_ORDER, order.getId(), order.getSubtotal(), order.getDeliveryPrice(),
                order.getTotalPrice(), order.getFirstName(), order.getLastName(), order.getDeliveryAddress(),
                order.getContactPhoneNo(), order.getAdditionalInformation(), order.getStatus().toString());
        List<OrderItem> orderItems = order.getOrderItems();
        for (int i = 0; i < orderItems.size(); i++) {
            jdbcTemplate.update(SQL_SAVE_ORDER_ITEM, orderItems.get(i).getPhone().getId(),
                    orderItems.get(i).getOrder().getId(), orderItems.get(i).getQuantity());
        }
    }

    private final class OrderBeanPropertyRowMapper extends BeanPropertyRowMapper<Order> {

        public OrderBeanPropertyRowMapper() {
            this.initialize(Order.class);
        }

        @Override
        public Order mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Order order = super.mapRow(rs, rowNumber);
            List<OrderItemBuffer> orderItemsBuffer = jdbcTemplate.query(SQL_SELECT_FOR_MAP_ROW + order.getId(),
                    new BeanPropertyRowMapper<>(OrderItemBuffer.class));
            List<OrderItem> orderItems = new ArrayList<>();
            orderItemsBuffer.forEach(orderItemBuffer -> {
                Optional<Phone> optionalPhone = jdbcPhoneDao.get(orderItemBuffer.getPhoneId());
                if (optionalPhone.isPresent()) {
                    orderItems.add(new OrderItem(orderItemBuffer.getId(), optionalPhone.get(), order,
                            orderItemBuffer.getQuantity()));
                } else {
                    throw new NoElementWithSuchIdException(orderItemBuffer.getPhoneId());
                }
            });
            order.setOrderItems(orderItems);
            return order;
        }
    }

}
