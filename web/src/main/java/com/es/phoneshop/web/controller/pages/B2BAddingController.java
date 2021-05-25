package com.es.phoneshop.web.controller.pages;

import com.es.core.model.b2b.B2bCartDTO;
import com.es.core.model.cart.Cart;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneArrayDTO;
import com.es.core.service.cart.CartService;
import com.es.core.service.phone.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/b2b")
public class B2BAddingController {

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @Resource
    private PhoneService phoneService;

    @Resource(name = "b2bCartDTOValidator")
    private Validator b2bCartDTOValidator;

    @RequestMapping(method = RequestMethod.GET)
    public String showB2B() {
        return "b2bPage";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addToCart(@Validated @ModelAttribute(name = "b2bCartDTO") B2bCartDTO b2bCartDTO,
                            Model model, BindingResult bindingResult) {
        b2bCartDTOValidator.validate(b2bCartDTO, bindingResult);
        List<Long> errorIdCode = new ArrayList<>();
        List<Long> errorIdQuantity = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errorIdCode = failedValidation(bindingResult, "code");
            errorIdQuantity = failedValidation(bindingResult, "quantity");
        }
        Cart cart = cartService.getCart(httpSession);
        List<Long> finalErrorIdCode = errorIdCode;
        List<Long> finalErrorIdQuantity = errorIdQuantity;
        List<Long> successfulInfo = new ArrayList<>();
        creatingResponseMessages(errorIdCode, errorIdQuantity, successfulInfo, b2bCartDTO, cart);
        model.addAttribute("successMessage", successfulInfo);
        model.addAttribute("errorsCode", finalErrorIdCode);
        model.addAttribute("errorsQuantity", finalErrorIdQuantity);
        return "b2bPage";
    }

    private List<Long> failedValidation(BindingResult bindingResult, String field) {
        List<FieldError> errors = bindingResult.getFieldErrors(field);
        return errors.stream()
                .map(item -> Long.parseLong(item.getCode()))
                .collect(Collectors.toList());
    }

    private void creatingResponseMessages(List<Long> ErrorCode, List<Long> ErrorQuantity, List<Long> success,
                                          B2bCartDTO b2bCartDTO, Cart cart) {
        String[] quantities = b2bCartDTO.getQuantity();
        String[] codes = b2bCartDTO.getCode();
        IntStream.range(0, quantities.length).forEach(i -> {
            if (!ErrorCode.contains((long) i) && !ErrorQuantity.contains((long) i)
                    && !quantities[i].isEmpty() && !codes[i].isEmpty()) {
                Long code = Long.parseLong(codes[i]);
                Long quantity = Long.parseLong(quantities[i]);
                boolean checker = cartService.checkQuantity(code, quantity);
                if (checker) {
                    Phone phone = phoneService.getPhone(code.toString());
                    cartService.addToCart(quantity, phone, cart);
                    success.add(phone.getId());
                } else {
                    ErrorQuantity.add((long) i);
                }
            }
        });
    }

}
