package com.es.core.model.stock;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcStockDao implements StockDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_SELECT_FOR_GET = "select * from stocks where phoneId = ";
    private static final String SQL_UPDATE = "update stocks set stock = %d, reserved = %d where phoneId = %d";

    @Override
    public Optional<Stock> get(final Long key) {
        List<Stock> stockList = jdbcTemplate.query(SQL_SELECT_FOR_GET + key,
                new BeanPropertyRowMapper<>(Stock.class));
        if (CollectionUtils.isNotEmpty(stockList)) {
            return Optional.ofNullable(stockList.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void update(final Long key, final Long stock, final Long reserved) {
        jdbcTemplate.update(String.format(SQL_UPDATE, stock, reserved, key));
    }
}
