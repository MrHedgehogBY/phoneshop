package com.es.core.validator;

import com.es.core.model.order.OrderDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OrderDataDTOValidator implements Validator {

    private String regexNameOrSurname = "^[\\p{L} .'-]+$";
    private String regexAddress = "^[a-zA-Z0-9,\\s]+$";
    private String regexPhone = "^\\+\\d{12}$";

    @Override
    public boolean supports(Class<?> aClass) {
        return OrderDataDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "emptyField",
                "message.empty.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "emptyField",
                "message.empty.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "emptyField",
                "message.empty.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "emptyField",
                "message.empty.field");
        if (!errors.hasErrors()) {
            OrderDataDTO dto = (OrderDataDTO) o;
            if (!validateWithRegex(dto.getFirstName(), regexNameOrSurname)) {
                errors.rejectValue("firstName", "firstName");
            }
            if (!validateWithRegex(dto.getLastName(), regexNameOrSurname)) {
                errors.rejectValue("lastName", "lastName");
            }
            if (!validateWithRegex(dto.getAddress(), regexAddress)) {
                errors.rejectValue("address", "address");
            }
            if (!validateWithRegex(dto.getPhone(), regexPhone)) {
                errors.rejectValue("phone", "phone");
            }
        }
    }

    private boolean validateWithRegex(String parameter, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(parameter);
        return matcher.find();
    }
}
