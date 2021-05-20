package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping(value = "/productDetails/{id}")
public class ProductDetailsPageController {

    @Autowired
    private Environment env;

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductDetails(@PathVariable("id") String id, Model model) throws NoElementWithSuchIdException {
        Optional<Phone> currentPhone;
        try {
            currentPhone = jdbcPhoneDao.get(Long.valueOf(id));
            model.addAttribute("cart", cartService.getCart(httpSession));
            currentPhone.ifPresent(phone -> model.addAttribute("phone", phone));
        } catch (NumberFormatException | EmptyResultDataAccessException e) {
            throw new NoElementWithSuchIdException(id);
        }
        return "productDetails";
    }

    @ExceptionHandler(NoElementWithSuchIdException.class)
    public String handle(NoElementWithSuchIdException ex) {
        return "redirect:/404?message=" + env.getProperty("noSuchIdException") + ex.getId();
    }
}
