package com.es.core.search;

import com.es.core.comparator.SearchComparator;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SearchServiceImpl implements SearchService {

    @Resource
    private StockDao jdbcStockDao;

    @Override
    public Stream<Phone> searchPhones(String searchValue, Stream<Phone> phoneStream) {
        phoneStream = phoneStream.filter(item -> {
            Optional<Stock> stockInfoOptional = jdbcStockDao.get(item.getId());
            if (stockInfoOptional.isPresent()) {
                Stock stockInfo = stockInfoOptional.get();
                return stockInfo.getStock() - stockInfo.getReserved() > 0 && item.getPrice() != null;
            }
            return false;
        });
        if (searchValue != null && !searchValue.isEmpty()) {
            List<String> wordsList = Arrays.asList(searchValue.split(" "));
            phoneStream = phoneStream
                    .filter(item -> containsWord(item, wordsList))
                    .sorted(new SearchComparator(wordsList).reversed());
        }
        return phoneStream;
    }

    private boolean containsWord(Phone phone, List<String> wordsList) {
        return wordsList.stream().anyMatch(word -> phone.getModel().contains(word));
    }
}
