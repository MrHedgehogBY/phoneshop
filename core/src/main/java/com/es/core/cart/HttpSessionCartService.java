package com.es.core.cart;

import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import com.es.core.exception.OutOfStockException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private StockDao jdbcStockDao;

    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";

    @Override
    public Cart getCart(HttpSession httpSession) {
        Object attribute = httpSession.getAttribute(HttpSessionCartService.CART_SESSION_ATTRIBUTE);
        Cart cart;
        if (attribute == null) {
            cart = new Cart();
            httpSession.setAttribute(CART_SESSION_ATTRIBUTE, cart);
        } else {
            cart = (Cart) attribute;
        }
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity, Cart cart) throws OutOfStockException, EmptyDatabaseArgumentException {
        Optional<Phone> optionalPhone = jdbcPhoneDao.get(phoneId);
        Optional<Stock> optionalStock = jdbcStockDao.get(phoneId);
        if (optionalPhone.isPresent() && optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            Phone phone = optionalPhone.get();
            if (stock.getStock() - stock.getReserved() >= quantity) {
                addToCart(quantity, stock, phone, cart);
            } else {
                throw new OutOfStockException("Product is out of stock");
            }
        } else {
            throw new EmptyDatabaseArgumentException("No product or stock in Database");
        }
    }

    @Override
    public void update(Map<Long, Long> items, Cart cart) {
        items.keySet().stream()
                .map(phoneId -> findSameCartItem(phoneId, cart))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(cartItem -> cartItem.setQuantity(items.get(cartItem.getPhone().getId())));
        calculateCart(cart);
    }

    @Override
    public void remove(Long phoneId, Cart cart) {
        Optional<CartItem> optionalCartItem = findSameCartItem(phoneId, cart);
        optionalCartItem.ifPresent(cartItem -> cart.getCartItems().remove(cartItem));
        calculateCart(cart);
    }

    @Override
    public void calculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getCartItems().stream()
                .map(CartItem::getQuantity)
                .mapToLong(item -> item)
                .sum()
        );
        cart.setTotalCost(cart.getCartItems().stream()
                .map(item -> item.getPhone().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private void addToCart(Long quantity, Stock stock, Phone phone, Cart cart) {
        jdbcStockDao.update(phone.getId(), stock.getStock() - quantity, stock.getReserved() + quantity);
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
