package com.es.phoneshop.web.controller;

import com.es.core.cart.CartService;
import com.es.core.cart.PhoneDataHolder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {

    @Resource
    @Qualifier("quantityValidator")
    private Validator quantityValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(quantityValidator);
    }

    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.POST)
    public String addPhone(@Validated @ModelAttribute(name = "phoneDataHolder") PhoneDataHolder phoneDataHolder,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }
        cartService.addPhone(phoneDataHolder.getId(), phoneDataHolder.getQuantity());
        return "productList";
    }
}
