package com.es.core.filter;

import com.es.core.model.phone.Phone;

import java.util.stream.Stream;

public interface FilterService {
    Stream<Phone> filterPhones(String sortField, String sortOrder, Stream<Phone> phoneStream);
}
