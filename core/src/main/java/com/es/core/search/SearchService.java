package com.es.core.search;

import com.es.core.model.phone.Phone;

import java.util.stream.Stream;

public interface SearchService {
    Stream<Phone> searchPhones(String searchingString, Stream<Phone> phoneStream);
}
