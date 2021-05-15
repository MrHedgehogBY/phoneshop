import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderDataDTO;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import com.es.core.order.OrderService;
import com.es.core.order.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private StockDao jdbcStockDao;

    @Mock
    private OrderDao jdbcOrderDao;

    @InjectMocks
    private OrderService orderServiceImpl = new OrderServiceImpl();

    private Cart cart;
    private Phone phone = new Phone();
    private Stock stock = new Stock();
    private CartItem cartItem;
    private OrderDataDTO orderDataDTO;
    private Long id = 1L;
    private Long deliveryPrice = 5L;

    @Before
    public void setup() {
        cart = new Cart();
        phone.setId(1L);
        phone.setPrice(BigDecimal.valueOf(100L));
        stock.setPhone(phone);
        stock.setStock(10);
        stock.setReserved(1);
        cartItem = new CartItem(phone, 2L);
        cart.getCartItems().add(cartItem);
        cart.setTotalQuantity(2L);
        cart.setTotalCost(BigDecimal.valueOf(200L));
        orderDataDTO = new OrderDataDTO();
    }

    @Test
    public void testPlaceOrder() {
        when(jdbcStockDao.get(anyLong())).thenReturn(Optional.of(stock));
        orderServiceImpl.placeOrder(cart, orderDataDTO, deliveryPrice);
    }

    @Test(expected = NoElementWithSuchIdException.class)
    public void testPlaceOrderException() {
        when(jdbcStockDao.get(anyLong())).thenReturn(Optional.empty());
        orderServiceImpl.placeOrder(cart, orderDataDTO, deliveryPrice);
    }

}
