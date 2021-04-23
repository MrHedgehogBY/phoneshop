package com.es.core.comparator;

import com.es.core.model.phone.Phone;
import com.es.core.sortenum.SortField;
import com.es.core.sortenum.SortOrder;

import java.util.Comparator;

public class SortingComparator implements Comparator<Phone> {

    private SortField sortField;
    private SortOrder sortOrder;

    public SortingComparator(SortField sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Phone phone1, Phone phone2) {
        int value = 0;
        switch (sortField) {
            case brand: {
                value = phone1.getBrand().compareTo(phone2.getBrand());
                break;
            }
            case model: {
                value = phone1.getModel().compareTo(phone2.getModel());
                break;
            }
            case price: {
                value = phone1.getPrice().compareTo(phone2.getPrice());
                break;
            }
            case displaySize: {
                value = phone1.getDisplaySizeInches().compareTo(phone2.getDisplaySizeInches());
                break;
            }
            default: {
                break;
            }
        }
        return sortOrder == SortOrder.asc ? value : -1 * value;
    }
}
