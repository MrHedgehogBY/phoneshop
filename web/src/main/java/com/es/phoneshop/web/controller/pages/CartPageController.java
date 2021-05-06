package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneArrayDTO;
import com.es.core.model.phone.PhoneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@PropertySource("classpath:/lang-en.properties")
@RequestMapping(value = "/cart")
public class CartPageController {

    @Resource(name = "phoneArrayDTOValidator")
    private Validator phoneArrayDTOValidator;

    @Autowired
    private Environment env;

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        model.addAttribute("cart", cartService.getCart(httpSession));
        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@Validated @ModelAttribute(name = "phoneArrayDTO") PhoneArrayDTO phoneArrayDTO,
                             Model model, BindingResult bindingResult) {
        Cart cart = cartService.getCart(httpSession);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model);
        }
        phoneArrayDTOValidator.validate(phoneArrayDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return failedValidation(cart, bindingResult, model);
        }
        HashMap<Long, Long> idQuantityMap = new HashMap<>();
        IntStream.range(0, phoneArrayDTO.getQuantity().length).forEach(i -> {
            idQuantityMap.put(Long.parseLong(phoneArrayDTO.getPhoneId()[i]),
                    Long.parseLong(phoneArrayDTO.getQuantity()[i]));
        });
        List<Phone> outOfStockPhones = cartService.update(idQuantityMap, cart);
        if (outOfStockPhones.isEmpty()) {
            model.addAttribute("message", env.getProperty("updateCartMessageSuccess"));
        } else {
            model.addAttribute("outOfStockPhones", outOfStockPhones);
            model.addAttribute("error", env.getProperty("updateCartMessageError"));
        }
        model.addAttribute("cart", cart);
        return "cart";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String deleteFromCart(@PathVariable("id") Long id, Model model) throws NoElementWithSuchIdException {
        Optional<Phone> currentPhone = jdbcPhoneDao.get(id);
        Cart cart = cartService.getCart(httpSession);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model);
        }
        if (currentPhone.isPresent()) {
            cartService.remove(id, cart);
            model.addAttribute("message", env.getProperty("deleteFromCartMessage"));
            model.addAttribute("cart", cart);
        } else {
            throw new NoElementWithSuchIdException(id);
        }
        return "cart";
    }

    private String prepareModelForEmptyCart(Cart cart, Model model) {
        model.addAttribute("error", env.getProperty("emptyCartMessage"));
        model.addAttribute("cart", cart);
        return "cart";
    }

    private String failedValidation(Cart cart, BindingResult bindingResult, Model model) {
        List<FieldError> errors = bindingResult.getFieldErrors("quantity");
        List<Long> errorsId = errors.stream()
                .map(item -> Long.parseLong(item.getCode()))
                .collect(Collectors.toList());
        model.addAttribute("cart", cart);
        model.addAttribute("errorsId", errorsId);
        model.addAttribute("error", env.getProperty("updateCartMessageError"));
        return "cart";
    }

    @ExceptionHandler(NoElementWithSuchIdException.class)
    public String handle(NoElementWithSuchIdException ex) {
        return "redirect:/404?message=" + env.getProperty("noSuchIdException") + ex.getId();
    }
}
