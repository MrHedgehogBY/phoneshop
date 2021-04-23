package com.es.core.comparator;

import com.es.core.model.phone.Phone;

import java.util.Comparator;
import java.util.List;

public class SearchComparator implements Comparator<Phone> {

    private List<String> wordsList;

    public SearchComparator(List<String> wordsList) {
        this.wordsList = wordsList;
    }

    @Override
    public int compare(Phone phone1, Phone phone2) {
        long wordsInPhone1 = wordsList.stream()
                .filter(word -> phone1.getModel().contains(word))
                .count();
        long wordsInPhone2 = wordsList.stream()
                .filter(word -> phone2.getModel().contains(word))
                .count();
        Double wordsPercentageProduct1 = wordsInPhone1 / (double) phone1.getDescription().split(" ").length;
        Double wordsPercentageProduct2 = wordsInPhone2 / (double) phone2.getDescription().split(" ").length;
        return wordsPercentageProduct1.compareTo(wordsPercentageProduct2);
    }
}
