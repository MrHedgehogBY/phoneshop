import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/applicationContext-core-test.xml")
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class
})
public class JdbcOrderDaoIntTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderDao jdbcOrderDao;

    @Autowired
    private PhoneDao jdbcPhoneDao;

    private static Long id = 1L;
    private static Long quantity = 1L;
    private static Order order;
    private static OrderItem orderItem;
    private static Phone phone;

    @BeforeClass
    public static void setupBeforeClass() {
        String brand = "Samsung";
        String model = "S3";
        phone = new Phone();
        phone.setBrand(brand);
        phone.setModel(model);
        phone.setId(id);
        order = new Order();
        order.setDate(new Timestamp(System.currentTimeMillis()));
        order.setStatus(OrderStatus.NEW);
        orderItem = new OrderItem(id, phone, order, quantity);
    }

    @Test
    public void saveTest() {
        deleteFromTables(jdbcTemplate, "orders");
        jdbcOrderDao.save(order);
        int rows = countRowsInTable(jdbcTemplate, "orders");
        assertEquals(rows, 1);
    }

    @Test
    public void getTest() {
        deleteFromTables(jdbcTemplate, "orders");
        order.setId(id);
        jdbcOrderDao.save(order);
        Optional<Order> orderFromTable = jdbcOrderDao.get(order.getId());
        orderFromTable.ifPresent(value -> assertEquals(value.getId(), order.getId()));
    }

    @Test
    public void getDeepTest() {
        deleteFromTables(jdbcTemplate, "orders");
        order.setId(id);
        order.getOrderItems().add(orderItem);
        jdbcPhoneDao.save(phone);
        jdbcOrderDao.save(order);
        Optional<Order> orderFromTable = jdbcOrderDao.get(order.getId());
        orderFromTable.ifPresent(value -> assertEquals(value.getOrderItems().get(0).getPhone().getId(), phone.getId()));
    }

    @Test
    public void updateStatusTest() {
        deleteFromTables(jdbcTemplate, "orders");
        order.setId(id);
        jdbcOrderDao.save(order);
        jdbcOrderDao.updateStatus(order.getId(), OrderStatus.DELIVERED);
        OrderStatus status = null;
        Optional<Order> changedOrder = jdbcOrderDao.get(order.getId());
        if (changedOrder.isPresent()) {
            status = changedOrder.get().getStatus();
        }
        assertEquals(OrderStatus.DELIVERED, status);
    }
}
