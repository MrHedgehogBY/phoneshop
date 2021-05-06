package com.es.core.model.order;

import java.util.Optional;

public interface OrderDao {
    Optional<Order> get(final Long key);
    void save(final Order order);
}
