package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.service.cart.CartService;
import com.es.core.service.phone.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/productDetails/{id}")
public class ProductDetailsPageController {

    @Autowired
    private Environment env;

    @Resource
    private PhoneService phoneService;

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductDetails(@PathVariable("id") String id, Model model) throws NoElementWithSuchIdException {
        Phone currentPhone = phoneService.getPhone(id);
        model.addAttribute("cart", cartService.getCart(httpSession));
        model.addAttribute("phone", currentPhone);
        return "productDetails";
    }

    @ExceptionHandler(NoElementWithSuchIdException.class)
    public String handle(NoElementWithSuchIdException ex) {
        return "redirect:/404?message=" + env.getProperty("noSuchIdException") + ex.getId();
    }
}
