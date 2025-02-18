package com.es.core.model.stock;

import java.util.Optional;

public interface StockDao {
    Optional<Stock> get(final Long key);

    void update(final Long key, final Long stock, final Long reserved);
}
