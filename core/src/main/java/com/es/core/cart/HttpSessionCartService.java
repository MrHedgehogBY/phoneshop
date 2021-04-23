package com.es.core.cart;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {

    @Resource
    private Cart cart;

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private StockDao jdbcStockDao;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        Optional<Phone> optionalPhone = jdbcPhoneDao.get(phoneId);
        Optional<Stock> optionalStock = jdbcStockDao.get(phoneId);
        if (optionalPhone.isPresent() && optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            Phone phone = optionalPhone.get();
            if (stock.getStock() - stock.getReserved() >= quantity) {
                addToCart(quantity, stock, phone);
            }
        }
    }

    @Override
    public void update(Map<Long, Long> items) {
        for (Long phoneId : items.keySet()) {
            Optional<CartItem> optionalCartItem = findSameCartItem(phoneId);
            optionalCartItem.ifPresent(cartItem -> cartItem.setQuantity(items.get(phoneId)));
        }
    }

    @Override
    public void remove(Long phoneId) {
        Optional<CartItem> optionalCartItem = findSameCartItem(phoneId);
        optionalCartItem.ifPresent(cartItem -> cart.getCartItems().remove(cartItem));
    }

    private void addToCart(Long quantity, Stock stock, Phone phone) {
        jdbcStockDao.update(phone.getId(), stock.getStock() - quantity, stock.getReserved() + quantity);
        Optional<CartItem> optionalCartItem = findSameCartItem(phone.getId());
        if (optionalCartItem.isPresent()) {
            CartItem existsItem = optionalCartItem.get();
            existsItem.setQuantity(existsItem.getQuantity() + quantity);
        } else {
            cart.getCartItems().add(new CartItem(phone, quantity));
        }
        cart.setTotalQuantity(cart.getTotalQuantity() + quantity);
        cart.setTotalCost(cart.getTotalCost().add(phone.getPrice().multiply(BigDecimal.valueOf(quantity))));
    }

    private Optional<CartItem> findSameCartItem(Long phoneId) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getPhone().getId().equals(phoneId))
                .findFirst();
    }
}
