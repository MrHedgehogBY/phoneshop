package com.es.core.filter;

import com.es.core.comparator.SortingComparator;
import com.es.core.model.phone.Phone;
import com.es.core.sortenum.SortField;
import com.es.core.sortenum.SortOrder;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class FilterServiceImpl implements FilterService {

    @Override
    public Stream<Phone> filterPhones(String sortField, String sortOrder, Stream<Phone> phoneStream) {
        if (sortField != null && !sortField.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
            phoneStream = phoneStream.sorted(new SortingComparator(SortField.valueOf(sortField), SortOrder.valueOf(sortOrder)));
        }
        return phoneStream;
    }
}
