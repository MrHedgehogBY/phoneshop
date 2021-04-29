package com.es.core.model.phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(final Long key);

    void save(final Phone phone);

    List<Phone> findAll(final int offset, final int limit);

    List<Phone> findAll(final String search, final String sortField, final String order,
                        final int offset, final int limit);

    Long count(final String search, final String sortField, final String order,
               final int offset, final int limit);
}
