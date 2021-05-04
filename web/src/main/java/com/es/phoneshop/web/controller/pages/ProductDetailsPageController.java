package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping(value = "/productDetails/{id}")
public class ProductDetailsPageController {

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductDetails(@PathVariable("id") Long id, Model model) throws NoElementWithSuchIdException {
        Optional<Phone> currentPhone = jdbcPhoneDao.get(id);
        model.addAttribute("cart", cartService.getCart(httpSession));
        if (currentPhone.isPresent()) {
            model.addAttribute("phone", currentPhone.get());
        } else {
            throw new NoElementWithSuchIdException();
        }
        return "productDetails";
    }
}
