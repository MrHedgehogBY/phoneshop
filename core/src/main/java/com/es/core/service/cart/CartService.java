package com.es.core.service.cart;

import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.phone.Phone;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface CartService {

    Cart getCart(HttpSession httpSession);

    void deleteCart(HttpSession httpSession);

    void addPhone(Long phoneId, Long quantity, Cart cart) throws OutOfStockException, EmptyDatabaseArgumentException;

    /**
     * @param items key: {@link com.es.core.model.phone.Phone#id}
     *              value: quantity
     */
    void update(Map<Long, Long> items, Cart cart);

    void remove(Long phoneId, Cart cart);

    void calculateCart(Cart cart);

    void checkCartItems(Cart cart) throws OutOfStockException;

    boolean checkQuantity(Long phoneId, Long quantity);

    List<Phone> checkOutOfStock(Map<Long, Long> items, Cart cart);
}
