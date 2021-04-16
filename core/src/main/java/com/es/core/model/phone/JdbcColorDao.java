package com.es.core.model.phone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class JdbcColorDao implements ColorDao{
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Color> get(Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("select * from colors where id = " + key,
                new BeanPropertyRowMapper<>(Color.class)));
    }

    private final static class ColorRowMapper implements RowMapper<Color> {

        @Override
        public Color mapRow(ResultSet resultSet, int i) throws SQLException {
            Color color = new Color();
            color.setId(resultSet.getLong("id"));
            color.setCode(resultSet.getString("code"));
            return color;
        }
    }
}
