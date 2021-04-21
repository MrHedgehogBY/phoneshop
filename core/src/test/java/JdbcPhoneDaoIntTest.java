import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.Phone;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/applicationContext-core-test.xml")
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
public class JdbcPhoneDaoIntTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcPhoneDao jdbcPhoneDao;
    private static Phone phone;

    @BeforeClass
    public static void setupBeforeClass() {
        String brand = "Samsung";
        String model = "S3";
        phone = new Phone();
        phone.setBrand(brand);
        phone.setModel(model);
    }

    @Test
    public void saveTest() {
        deleteFromTables(jdbcTemplate, "phones");
        jdbcPhoneDao.save(phone);
        int rows = countRowsInTable(jdbcTemplate, "phones");
        assertEquals(rows, 1);
    }

    @Test
    public void getTest() {
        deleteFromTables(jdbcTemplate, "phones");
        Long id = 1L;
        phone.setId(id);
        jdbcPhoneDao.save(phone);
        Optional<Phone> phoneFromTable = jdbcPhoneDao.get(phone.getId());
        phoneFromTable.ifPresent(value -> assertEquals(value.getId(), phone.getId()));
    }

    @Test
    public void findAllTest() {
        deleteFromTables(jdbcTemplate, "phones");
        jdbcPhoneDao.save(phone);
        String newModel = "S4";
        phone.setModel(newModel);
        jdbcPhoneDao.save(phone);
        List<Phone> listPhone = jdbcPhoneDao.findAll(0, Integer.MAX_VALUE);
        assertEquals(listPhone.size(), 2);
    }

    @Sql("classpath:/scripts/deep-find-all.sql")
    @Test
    public void findAllDeepTest() {
        List<Phone> listPhone = jdbcPhoneDao.findAll(0, Integer.MAX_VALUE);
        assertEquals(listPhone.size(), 1);
        assertEquals(listPhone.get(0).getColors().size(), 1);
    }
}
