package com.es.core.service.cart;

import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.StockDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:/properties/errorsMessages.properties")
public class HttpSessionCartServiceImpl implements CartService {

    @Resource
    private Environment env;

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private StockDao jdbcStockDao;

    private static final String CART_SESSION_ATTRIBUTE = "sessionCart";

    @Override
    public Cart getCart(HttpSession httpSession) {
        Cart attribute = (Cart) httpSession.getAttribute(HttpSessionCartServiceImpl.CART_SESSION_ATTRIBUTE);
        if (attribute == null) {
            attribute = new Cart();
            httpSession.setAttribute(CART_SESSION_ATTRIBUTE, attribute);
        }
        return attribute;
    }

    public void deleteCart(HttpSession httpSession) {
        httpSession.removeAttribute(CART_SESSION_ATTRIBUTE);
    }

    @Override
    public void addPhone(Long phoneId, Long quantity, Cart cart) throws OutOfStockException, EmptyDatabaseArgumentException {
        Optional<Phone> optionalPhone;
        Optional<Stock> optionalStock;
        try {
            optionalPhone = jdbcPhoneDao.get(phoneId);
            optionalStock = jdbcStockDao.get(phoneId);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyDatabaseArgumentException(env.getProperty("error.emptyDatabaseArgument"));
        }
        if (optionalPhone.isPresent() && optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            Phone phone = optionalPhone.get();
            if (stock.getStock() >= quantity) {
                addQuantityToCartItem(quantity, phone, cart);
            } else {
                throw new OutOfStockException(env.getProperty("error.outOfStock"));
            }
        } else {
            throw new EmptyDatabaseArgumentException(env.getProperty("error.emptyDatabaseArgument"));
        }
    }

    @Override
    public void updatePhone(Long phoneId, Long quantity, Cart cart) throws EmptyDatabaseArgumentException, OutOfStockException {
        Optional<CartItem> optionalExistingSameCartItem;
        Optional<Stock> optionalStock;
        try {
            optionalExistingSameCartItem = findSameCartItem(phoneId, cart);
            optionalStock = jdbcStockDao.get(phoneId);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyDatabaseArgumentException(env.getProperty("error.emptyDatabaseArgument"));
        }
        if (optionalExistingSameCartItem.isPresent() && optionalStock.isPresent()) {
            CartItem existingSameItem = optionalExistingSameCartItem.get();
            Stock currentStock = optionalStock.get();
            if (currentStock.getStock() >= quantity) {
                existingSameItem.setQuantity(quantity);
                calculateCart(cart);
            } else {
                throw new OutOfStockException(env.getProperty("error.outOfStock"));
            }
        } else {
            throw new EmptyDatabaseArgumentException(env.getProperty("error.emptyDatabaseArgument"));
        }
    }

    @Override
    public void removePhone(Long phoneId, Cart cart) {
        Optional<CartItem> optionalCartItem = findSameCartItem(phoneId, cart);
        if (optionalCartItem.isPresent()) {
            cart.getCartItems().remove(optionalCartItem.get());
        } else {
            throw new NoElementWithSuchIdException(phoneId.toString());
        }
        calculateCart(cart);
    }

    @Override
    public void calculateCart(Cart cart) {
        long totalQuantity = cart.getCartItems().stream()
                .mapToLong(CartItem::getQuantity)
                .sum();
        cart.setTotalQuantity(totalQuantity);
        BigDecimal totalCost = cart.getCartItems().stream()
                .map(item -> item.getPhone().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalCost(totalCost);
    }

    @Override
    public void checkCartItemsForOutOfStock(Cart cart) throws OutOfStockException {
        List<Long> idsOutOfStock = new ArrayList<>();
        cart.getCartItems().forEach(cartItem -> {
            if (!checkQuantityForOutOfStock(cartItem.getPhone().getId(), cartItem.getQuantity())) {
                idsOutOfStock.add(cartItem.getPhone().getId());
            }
        });
        if (CollectionUtils.isNotEmpty(idsOutOfStock)) {
            idsOutOfStock.forEach(id -> removePhone(id, cart));
            throw new OutOfStockException();
        }
    }

    private boolean checkQuantityForOutOfStock(Long phoneId, Long quantity) {
        Optional<Stock> optionalStock = jdbcStockDao.get(phoneId);
        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            return stock.getStock() >= quantity;
        }
        return false;
    }

    private void addQuantityToCartItem(Long quantity, Phone phone, Cart cart) {
        Optional<CartItem> optionalCartItem = findSameCartItem(phone.getId(), cart);
        if (optionalCartItem.isPresent()) {
            CartItem existsItem = optionalCartItem.get();
            existsItem.setQuantity(existsItem.getQuantity() + quantity);
        } else {
            cart.getCartItems().add(new CartItem(phone, quantity));
        }
        calculateCart(cart);
    }

    private Optional<CartItem> findSameCartItem(Long phoneId, Cart cart) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getPhone().getId().equals(phoneId))
                .findFirst();
    }
}
