package com.es.core.model.phone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class JdbcColorDao implements ColorDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Color> get(Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("select * from colors where id = " + key,
                new BeanPropertyRowMapper<>(Color.class)));
    }

    @Override
    public void save(final Color color) {
        jdbcTemplate.update("insert into colors (id, code) values (?, ?)",
                color.getId(), color.getCode());
    }
}
