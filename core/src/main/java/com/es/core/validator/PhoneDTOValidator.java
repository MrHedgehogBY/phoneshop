package com.es.core.validator;

import com.es.core.model.phone.PhoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
@PropertySource("classpath:/properties/validationMessages.properties")
public class PhoneDTOValidator implements Validator {

    @Autowired
    private Environment env;

    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity",
                env.getProperty("message.empty.quantity"));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id",
                env.getProperty("message.empty.id"));
        if (!errors.hasErrors()) {
            PhoneDTO dto = (PhoneDTO) o;
            try {
                Long quantity = Long.parseLong(dto.getQuantity());
                if (quantity <= 0) {
                    errors.rejectValue("quantity", env.getProperty("message.illegalQuantity"));
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("quantity", env.getProperty("message.incorrectValue"));
            }
        }
    }
}
