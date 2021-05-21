package com.es.core.model.color;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class JdbcColorDao implements ColorDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_SELECT_FOR_GET = "select * from colors where id = ";
    private static final String SQL_INSERT_FOR_SAVE = "insert into colors (id, code) values (?, ?)";

    @Override
    public Optional<Color> get(final Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_FOR_GET + key,
                new BeanPropertyRowMapper<>(Color.class)));
    }

    @Override
    public void save(final Color color) {
        jdbcTemplate.update(SQL_INSERT_FOR_SAVE,
                color.getId(), color.getCode());
    }
}
