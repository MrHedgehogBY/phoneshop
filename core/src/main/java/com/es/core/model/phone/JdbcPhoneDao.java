package com.es.core.model.phone;

import org.apache.commons.collections4.CollectionUtils;
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

    private static final String SQL_SELECT_FOR_GET = "select * from phones where id = ";
    private static final String SQL_INSERT_FOR_SAVE = "insert into phones (id, brand, model, price, " +
            "displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, " +
            "pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, " +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_FOR_FIND_ALL = "select * from phones offset ";
    private static final String SQL_LIMIT_FOR_FIND_ALL = " limit ";
    private static final String SQL_SELECT_FOR_MAP_ROW = "select colorId from phone2color where phoneId = ";

    public Optional<Phone> get(final Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_FOR_GET + key,
                new PhoneBeanPropertyRowMapper()));
    }

    public void save(final Phone phone) {
        jdbcTemplate.update(SQL_INSERT_FOR_SAVE,
                phone.getId(), phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getDisplaySizeInches(),
                phone.getWeightGr(), phone.getLengthMm(), phone.getWidthMm(), phone.getHeightMm(),
                phone.getAnnounced(), phone.getDeviceType(), phone.getOs(), phone.getDisplayResolution(),
                phone.getPixelDensity(), phone.getDisplayTechnology(), phone.getBackCameraMegapixels(),
                phone.getFrontCameraMegapixels(), phone.getRamGb(), phone.getInternalStorageGb(),
                phone.getBatteryCapacityMah(), phone.getTalkTimeHours(), phone.getStandByTimeHours(),
                phone.getBluetooth(), phone.getPositioning(), phone.getImageUrl(), phone.getDescription());
    }

    public List<Phone> findAll(final int offset, final int limit) {
        return jdbcTemplate.query(SQL_SELECT_FOR_FIND_ALL + offset + SQL_LIMIT_FOR_FIND_ALL + limit,
                new PhoneBeanPropertyRowMapper());
    }

    private final class PhoneBeanPropertyRowMapper extends BeanPropertyRowMapper<Phone> {

        public PhoneBeanPropertyRowMapper() {
            this.initialize(Phone.class);
        }

        @Override
        public Phone mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Phone phone = super.mapRow(rs, rowNumber);
            List<Long> colorIds = jdbcTemplate.query(SQL_SELECT_FOR_MAP_ROW + phone.getId(), new IdRowMapper());
            if (CollectionUtils.isNotEmpty(colorIds)) {
                Set<Color> colorSet = new HashSet<>();
                colorIds.stream().forEach(id -> {
                    Optional<Color> optionalColor = jdbcColorDao.get(id);
                    optionalColor.ifPresent(colorSet::add);
                });
                phone.setColors(colorSet);
            }
            return phone;
        }
    }

    private final static class IdRowMapper implements RowMapper<Long> {

        @Override
        public Long mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getLong("colorId");
        }
    }
}
