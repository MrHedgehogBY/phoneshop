package com.es.core.model.phone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class JdbcPhoneDao implements PhoneDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private JdbcColorDao jdbcColorDao;

    public Optional<Phone> get(final Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("select * from phones where id = " + key,
                new CustomRowMapper<>(Phone.class)));
    }

    public void save(final Phone phone) {
        jdbcTemplate.update("insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, " +
                        "widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, " +
                        "backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
                        "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                phone.getId(), phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getDisplaySizeInches(),
                phone.getWeightGr(), phone.getLengthMm(), phone.getWidthMm(), phone.getHeightMm(),
                phone.getAnnounced(), phone.getDeviceType(), phone.getOs(), phone.getDisplayResolution(),
                phone.getPixelDensity(), phone.getDisplayTechnology(), phone.getBackCameraMegapixels(),
                phone.getFrontCameraMegapixels(), phone.getRamGb(), phone.getInternalStorageGb(),
                phone.getBatteryCapacityMah(), phone.getTalkTimeHours(), phone.getStandByTimeHours(),
                phone.getBluetooth(), phone.getPositioning(), phone.getImageUrl(), phone.getDescription());
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query("select * from phones offset " + offset + " limit " + limit,
                new CustomRowMapper<>(Phone.class));
    }

    private final class CustomRowMapper<T> extends BeanPropertyRowMapper<T> {

        public CustomRowMapper(Class<T> mappedClass) {
            this.initialize(mappedClass);
        }

        @Override
        public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Phone phone = (Phone) super.mapRow(rs, rowNumber);
            List<Long> colorIds = jdbcTemplate.query("select colorId from phone2color where phoneId = " +
                    phone.getId(), new IdRowMapper());
            if (!colorIds.isEmpty()) {
                Set<Color> colorSet = new HashSet<>();
                colorIds.forEach(id -> {
                    Optional<Color> optionalColor = jdbcColorDao.get(id);
                    optionalColor.ifPresent(colorSet::add);
                });
                phone.setColors(colorSet);
            }
            return (T) phone;
        }
    }

    private final static class IdRowMapper implements RowMapper<Long> {

        @Override
        public Long mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getLong("colorId");
        }
    }
}
