package com.es.core.service.phone;

import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {

    @Resource
    private PhoneDao phoneDao;

    @Override
    public Phone getPhone(String id) {
        Optional<Phone> phone;
        try {
            phone = phoneDao.get(Long.valueOf(id));
        } catch (NumberFormatException | EmptyResultDataAccessException e) {
            throw new NoElementWithSuchIdException(id);
        }
        if (phone.isPresent()) {
            return phone.get();
        } else {
            throw new NoElementWithSuchIdException(id);
        }
    }

    @Override
    public void savePhone(Phone phone) {
        phoneDao.save(phone);
    }

    @Override
    public List<Phone> findAllPhones(int offset, int limit) {
        return phoneDao.findAll(offset, limit);
    }

    @Override
    public List<Phone> findAllPhones(String search, String sortField, String order, int offset, int limit) {
        return phoneDao.findAll(search, sortField, order, offset, limit);
    }

    @Override
    public Long countPhones(String search, String sortField, String order, int offset, int limit) {
        return phoneDao.count(search, sortField, order, offset, limit);
    }
}
