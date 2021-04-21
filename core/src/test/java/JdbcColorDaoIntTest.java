import com.es.core.model.phone.Color;
import com.es.core.model.phone.JdbcColorDao;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/applicationContext-core-test.xml")
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class
})
public class JdbcColorDaoIntTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcColorDao jdbcColorDao;
    private static Color color;

    @BeforeClass
    public static void setupBeforeClass() {
        String code = "Black";
        color = new Color();
        color.setCode(code);
    }

    @Before
    public void setup() {
        deleteFromTables(jdbcTemplate, "colors");
    }

    @Test
    public void saveTest() {
        jdbcColorDao.save(color);
        int rows = countRowsInTable(jdbcTemplate, "colors");
        assertEquals(rows, 1);
    }

    @Test
    public void getTest() {
        Long id = 1L;
        color.setId(id);
        jdbcColorDao.save(color);
        Optional<Color> phoneFromTable = jdbcColorDao.get(color.getId());
        phoneFromTable.ifPresent(value -> assertEquals(value.getId(), color.getId()));
    }
}
