package com.es.core.model.order;

import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
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
    private static final String SQL_SAVE_ORDER = "insert into orders (subtotal, deliveryPrice, totalPrice, firstName," +
            " lastName, deliveryAddress, contactPhoneNo, additionalInformation, orderPlacingDate, status) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SAVE_ORDER_ITEM = "insert into orderItems (phoneId, orderId, quantity) " +
            "values (?, ?, ?)";
    private static final String SQL_FIND_ALL_ORDERS =
            "select * from orders order by orderPlacingDate desc offset %d limit %d";
    private static final String SQL_COUNT_ALL_ORDERS = "select count(*) from orders";
    private static final String SQL_UPDATE_ORDER_STATUS = "update orders set status = ? where id = ?";

    @Override
    public Optional<Order> get(Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_FOR_GET + key,
                new OrderBeanPropertyRowMapper()));
    }

    @Override
    public Long save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Object[] orderParams = new Object[]{order.getSubtotal(), order.getDeliveryPrice(), order.getTotalPrice(),
                order.getFirstName(), order.getLastName(), order.getDeliveryAddress(), order.getContactPhoneNo(),
                order.getAdditionalInformation(), order.getOrderPlacingDate(), order.getStatus().toString()};
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_ORDER);
            for (int i = 1; i <= orderParams.length; i++) {
                preparedStatement.setObject(i, orderParams[i - 1]);
            }
            return preparedStatement;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        List<OrderItem> orderItems = order.getOrderItems();
        for (int i = 0; i < orderItems.size(); i++) {
            jdbcTemplate.update(SQL_SAVE_ORDER_ITEM, orderItems.get(i).getPhone().getId(),
                    id, orderItems.get(i).getQuantity());
        }
        return id;
    }

    @Override
    public void updateStatus(Long key, OrderStatus status) {
        jdbcTemplate.update(SQL_UPDATE_ORDER_STATUS, status.toString(), key);
    }

    @Override
    public List<Order> findAll(int offset, int limit) {
        return jdbcTemplate.query(String.format(SQL_FIND_ALL_ORDERS, offset, limit), new OrderBeanPropertyRowMapper());
    }

    @Override
    public Long count() {
        return jdbcTemplate.queryForObject(SQL_COUNT_ALL_ORDERS, Long.class);
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
                    throw new NoElementWithSuchIdException(orderItemBuffer.getPhoneId().toString());
                }
            });
            order.setOrderItems(orderItems);
            return order;
        }
    }

}
