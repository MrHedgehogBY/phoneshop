package com.es.core.validator;

import com.es.core.model.phone.PhoneArrayDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.stream.IntStream;

@Service
public class PhoneArrayDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneArrayDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PhoneArrayDTO dto = (PhoneArrayDTO) o;
        IntStream.of(0, dto.getQuantity().length - 1).forEach(i -> {
            try {
                Long quantity = Long.parseLong(dto.getQuantity()[i]);
                if (quantity <= 0) {
                    errors.rejectValue("quantity", dto.getPhoneId()[i]);
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("quantity", dto.getPhoneId()[i]);
            }
        });
    }
}
