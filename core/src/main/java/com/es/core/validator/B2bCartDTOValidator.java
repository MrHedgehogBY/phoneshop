package com.es.core.validator;

import com.es.core.model.b2b.B2bCartDTO;
import com.es.core.model.phone.PhoneDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.stream.IntStream;

@Service
public class B2bCartDTOValidator implements Validator {

    @Resource
    private PhoneDao phoneDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return B2bCartDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        B2bCartDTO b2bCartDTO = (B2bCartDTO) o;
        IntStream.range(0, b2bCartDTO.getQuantity().length).forEach(i -> {
            if (!b2bCartDTO.getCode()[i].isEmpty() && !b2bCartDTO.getQuantity()[i].isEmpty()) {
                checkCode(b2bCartDTO, i, errors);
                checkQuantity(b2bCartDTO, i, errors);
            }
        });
    }

    private void checkCode(B2bCartDTO b2bCartDTO, int i, Errors errors) {
        if (b2bCartDTO.getCode()[i] == null || b2bCartDTO.getCode()[i].isEmpty()) {
            errors.rejectValue("code", String.valueOf(i));
        } else {
            try {
                phoneDao.get(Long.parseLong(b2bCartDTO.getCode()[i]));
            } catch (NumberFormatException | EmptyResultDataAccessException e) {
                errors.rejectValue("code", String.valueOf(i));
            }
        }
    }

    private void checkQuantity(B2bCartDTO b2bCartDTO, int i, Errors errors) {
        if (b2bCartDTO.getQuantity()[i] == null || b2bCartDTO.getQuantity()[i].isEmpty()) {
            errors.rejectValue("quantity", String.valueOf(i));
        } else {
            try {
                Long.valueOf(b2bCartDTO.getQuantity()[i]);
                if (Long.parseLong(b2bCartDTO.getQuantity()[i]) <= 0) {
                    errors.rejectValue("quantity", String.valueOf(i));
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("quantity", String.valueOf(i));
            }
        }
    }
}
