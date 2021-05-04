package com.es.core.cart;

import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.exception.OutOfStockException;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface CartService {

    Cart getCart(HttpSession httpSession);

    void addPhone(Long phoneId, Long quantity, Cart cart) throws OutOfStockException, EmptyDatabaseArgumentException;

    /**
     * @param items key: {@link com.es.core.model.phone.Phone#id}
     *              value: quantity
     */
    void update(Map<Long, Long> items, Cart cart, List<Long> outOfStockId);

    void remove(Long phoneId, Cart cart) throws NoElementWithSuchIdException;

    void calculateCart(Cart cart);
}
