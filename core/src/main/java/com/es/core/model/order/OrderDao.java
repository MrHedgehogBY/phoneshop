package com.es.core.model.order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Optional<Order> get(final Long key);

    Long save(final Order order);

    List<Order> findAll(int offset, int limit);

    Long count();

    void updateStatus(Long key, OrderStatus status);
}
