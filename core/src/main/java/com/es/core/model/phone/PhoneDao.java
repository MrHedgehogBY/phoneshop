package com.es.core.model.phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(final Long key);
    void save(final Phone phone);
    List<Phone> findAll(final int offset, final int limit);
}
