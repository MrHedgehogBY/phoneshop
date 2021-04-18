package com.es.core.model.phone;

import java.util.Optional;

public interface ColorDao {
    Optional<Color> get(Long key);
    void save(final Color color);
}
