package com.es.core.model.phone;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Optional;

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
    private static final String SQL_SELECT_FOR_FIND_ALL_SIMPLE = "select * from phones offset %d limit %d";
    private static final String SQL_SELECT_FOR_MAP_ROW = "select colorId from phone2color where phoneId = ";
    private static final String SQL_SELECT_COUNT_FIND_ALL_EXTENDED = "select count(*) from phones ";
    private static final String SQL_SELECT_FIND_ALL_EXTENDED = "select * from phones ";
    private static final String SQL_WHERE_SEARCH = "where id in (select phoneId from stocks) and " +
            "(select stock from stocks where phoneId = phones.id) > 0 and price is not null ";
    private static final String SQL_WHERE_SEARCH_FIELD = "and lower(model) like lower(?) ";
    private static final String SQL_ORDER_BY_FILTER = "group by phones.id order by %s %s ";
    private static final String SQL_OFFSET_LIMIT = "offset %d limit %d";
    private static final String SQL_ANY_CHAR_OR_EMPTY = "%";

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
        return jdbcTemplate.query(String.format(SQL_SELECT_FOR_FIND_ALL_SIMPLE, offset, limit),
                new PhoneBeanPropertyRowMapper());
    }

    public List<Phone> findAll(final String search, final String sortField, final String order,
                               final int offset, final int limit) {
        String request = SQL_SELECT_FIND_ALL_EXTENDED + SQL_WHERE_SEARCH;
        List<Object> objects = new ArrayList<>();
        List<Integer> types = new ArrayList<>();
        if (search != null) {
            request = request + SQL_WHERE_SEARCH_FIELD;
            objects.add(SQL_ANY_CHAR_OR_EMPTY + search + SQL_ANY_CHAR_OR_EMPTY);
            types.add(Types.VARCHAR);
        }
        if (sortField != null && order != null) {
            request = request + String.format(SQL_ORDER_BY_FILTER, sortField, order);
        }
        request = request + String.format(SQL_OFFSET_LIMIT, offset, limit);
        int[] typesArray = types.stream().mapToInt(i -> i).toArray();
        return jdbcTemplate.query(request, objects.toArray(), typesArray, new PhoneBeanPropertyRowMapper());
    }

    public Long count(final String search, final String sortField, final String order,
                      final int offset, final int limit) {
        String request = SQL_SELECT_COUNT_FIND_ALL_EXTENDED + SQL_WHERE_SEARCH;
        if (search != null) {
            request = request + SQL_WHERE_SEARCH_FIELD;
            return jdbcTemplate.queryForObject(request, new Object[]{SQL_ANY_CHAR_OR_EMPTY + search +
                    SQL_ANY_CHAR_OR_EMPTY}, new int[]{Types.VARCHAR}, Long.class);
        } else {
            return jdbcTemplate.queryForObject(request, Long.class);
        }
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
                for (Long colorId : colorIds) {
                    Optional<Color> optionalColor = jdbcColorDao.get(colorId);
                    optionalColor.ifPresent(colorSet::add);
                }
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
