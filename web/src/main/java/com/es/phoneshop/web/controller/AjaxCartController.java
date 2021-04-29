package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.cart.PhoneDTO;
import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.OutOfStockException;
import com.es.core.validator.Errors;
import com.es.core.validator.ResponseErrors;
import com.es.core.validator.ValidationErrors;
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
    private Validator quantityValidator;

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Errors> addPhone(@Validated @ModelAttribute(name = "phoneDTO") PhoneDTO phoneDTO,
                                           BindingResult bindingResult) {
        quantityValidator.validate(phoneDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            ValidationErrors errors = new ValidationErrors(bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Cart currentCart = cartService.getCart(httpSession);
            cartService.addPhone(phoneDTO.getId(), phoneDTO.getQuantity(), currentCart);
            return ResponseEntity.ok().build();
        } catch (OutOfStockException | EmptyDatabaseArgumentException e) {
            ResponseErrors errors = new ResponseErrors(e.getMessage());
            return ResponseEntity.badRequest().body(errors);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

    }
}
