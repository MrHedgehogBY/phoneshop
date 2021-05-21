package com.es.core.service.filter;

import com.es.core.sortenum.SortField;
import com.es.core.sortenum.SortOrder;
import org.springframework.stereotype.Service;

@Service
public class FilterServiceImpl implements FilterService {

    @Override
    public String checkFieldValue(String field) {
        if (field != null) {
            try {
                SortField sortField = SortField.valueOf(field);
                return sortField.name();
            } catch (IllegalArgumentException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String checkOrderValue(String order) {
        if (order != null) {
            try {
                SortOrder sortOrder = SortOrder.valueOf(order);
                return sortOrder.name();
            } catch (IllegalArgumentException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
