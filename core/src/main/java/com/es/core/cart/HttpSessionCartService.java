package com.es.core.cart;

import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private StockDao jdbcStockDao;

    private static final String CART_SESSION_ATTRIBUTE = "sessionCart";

    @Override
    public Cart getCart(HttpSession httpSession) {
        Cart attribute = (Cart) httpSession.getAttribute(HttpSessionCartService.CART_SESSION_ATTRIBUTE);
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
        Optional<Phone> optionalPhone = jdbcPhoneDao.get(phoneId);
        Optional<Stock> optionalStock = jdbcStockDao.get(phoneId);
        if (optionalPhone.isPresent() && optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            Phone phone = optionalPhone.get();
            if (stock.getStock() >= quantity) {
                addToCart(quantity, phone, cart);
            } else {
                throw new OutOfStockException("Product is out of stock");
            }
        } else {
            throw new EmptyDatabaseArgumentException("No product or stock in Database");
        }
    }

    @Override
    public List<Phone> checkOutOfStock(Map<Long, Long> items, Cart cart) {
        List<Phone> outOfStockPhones = new ArrayList<>();
        items.keySet().stream()
                .map(phoneId -> findSameCartItem(phoneId, cart))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(cartItem -> {
                    Long phoneId = cartItem.getPhone().getId();
                    Long quantity = items.get(cartItem.getPhone().getId());
                    if (!checkQuantity(phoneId, quantity)) {
                        outOfStockPhones.add(cartItem.getPhone());
                    }
                });
        return outOfStockPhones;
    }

    @Override
    public void update(Map<Long, Long> items, Cart cart) {
        items.keySet().stream()
                .map(phoneId -> findSameCartItem(phoneId, cart))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(cartItem -> {
                    Long quantity = items.get(cartItem.getPhone().getId());
                    cartItem.setQuantity(quantity);
                });
        calculateCart(cart);
    }

    @Override
    public void remove(Long phoneId, Cart cart) {
        Optional<CartItem> optionalCartItem = findSameCartItem(phoneId, cart);
        if (optionalCartItem.isPresent()) {
            cart.getCartItems().remove(optionalCartItem.get());
        } else {
            throw new NoElementWithSuchIdException(phoneId);
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
    public void checkCartItems(Cart cart) throws OutOfStockException {
        List<Long> idsOutOfStock = new ArrayList<>();
        cart.getCartItems().forEach(cartItem -> {
            if (!checkQuantity(cartItem.getPhone().getId(), cartItem.getQuantity())) {
                idsOutOfStock.add(cartItem.getPhone().getId());
            }
        });
        if (CollectionUtils.isNotEmpty(idsOutOfStock)) {
            idsOutOfStock.forEach(id -> remove(id, cart));
            throw new OutOfStockException();
        }
    }

    @Override
    public boolean checkQuantity(Long phoneId, Long quantity) {
        Optional<Stock> optionalStock = jdbcStockDao.get(phoneId);
        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            if (stock.getStock() >= quantity) {
                return true;
            }
        }
        return false;
    }

    private void addToCart(Long quantity, Phone phone, Cart cart) {
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
