package com.es.core.service.cart;

import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;

import javax.servlet.http.HttpSession;

public interface CartService {

    Cart getCart(HttpSession httpSession);

    void deleteCart(HttpSession httpSession);

    void addPhone(Long phoneId, Long quantity, Cart cart) throws OutOfStockException, EmptyDatabaseArgumentException;

    void updatePhone(Long phoneId, Long quantity, Cart cart) throws EmptyDatabaseArgumentException, OutOfStockException;

    void removePhone(Long phoneId, Cart cart);

    void calculateCart(Cart cart);

    void checkCartItemsForOutOfStock(Cart cart) throws OutOfStockException;
}
