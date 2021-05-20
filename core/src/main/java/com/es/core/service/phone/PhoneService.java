package com.es.core.service.phone;

import com.es.core.model.phone.Phone;

import java.util.List;

public interface PhoneService {
    Phone getPhone(String id);

    void savePhone(Phone phone);

    List<Phone> findAllPhones(int offset, int limit);

    List<Phone> findAllPhones(String search, String sortField, String order, int offset, int limit);

    Long countPhones(String search, String sortField, String order, int offset, int limit);
}
