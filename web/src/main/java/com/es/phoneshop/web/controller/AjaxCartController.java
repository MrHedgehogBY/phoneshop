package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.PhoneDTO;
import com.es.core.validator.HandlingInfo;
import com.es.core.validator.ResponseHandlingInfo;
import com.es.core.validator.SuccessfulHandlingInfo;
import com.es.core.validator.ValidationHandlingInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {

    @Resource(name = "phoneDTOValidator")
    private Validator phoneDTOValidator;

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HandlingInfo> addPhone(@Validated @ModelAttribute(name = "phoneDTO") PhoneDTO phoneDTO,
                                                 BindingResult bindingResult) {
        phoneDTOValidator.validate(phoneDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            ValidationHandlingInfo errors = new ValidationHandlingInfo(bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Cart currentCart = cartService.getCart(httpSession);
            cartService.addPhone(Long.parseLong(phoneDTO.getId()), Long.parseLong(phoneDTO.getQuantity()), currentCart);
            SuccessfulHandlingInfo handlingInfo = new SuccessfulHandlingInfo(currentCart.getTotalQuantity().toString(),
                    currentCart.getTotalCost().toString());
            return ResponseEntity.ok().body(handlingInfo);
        } catch (OutOfStockException | EmptyDatabaseArgumentException e) {
            ResponseHandlingInfo errors = new ResponseHandlingInfo(e.getErrorMessage());
            return ResponseEntity.badRequest().body(errors);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

    }
}
