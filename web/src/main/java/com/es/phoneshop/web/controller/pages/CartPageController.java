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
import java.util.ArrayList;
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
        Cart cart = cartService.getCart(httpSession);
        if (cart.getTotalQuantity().intValue() == 0) {
            model.addAttribute("isEmpty", env.getProperty("emptyCartMessage"));
        }
        model.addAttribute("cart", cart);
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
        List<Long> errorsId = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errorsId = failedValidation(bindingResult);
        }
        HashMap<Long, Long> idQuantityMap = new HashMap<>();
        List<Long> finalErrorsId = errorsId;
        IntStream.range(0, phoneArrayDTO.getQuantity().length).forEach(i -> {
            Long id = Long.parseLong(phoneArrayDTO.getPhoneId()[i]);
            if (!finalErrorsId.contains(id)) {
                idQuantityMap.put(id, Long.parseLong(phoneArrayDTO.getQuantity()[i]));
            }
        });
        List<Phone> outOfStockPhones = cartService.checkOutOfStock(idQuantityMap, cart);
        if (outOfStockPhones.isEmpty() && errorsId.isEmpty()) {
            model.addAttribute("message", env.getProperty("updateCartMessageSuccess"));
            cartService.update(idQuantityMap, cart);
        } else {
            prepareModelForErrors(model, errorsId, outOfStockPhones);
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
            if (cart.getCartItems().isEmpty()) {
                return prepareModelForEmptyCart(cart, model);
            }
            model.addAttribute("message", env.getProperty("deleteFromCartMessage"));
            model.addAttribute("cart", cart);
        } else {
            throw new NoElementWithSuchIdException(id);
        }
        return "cart";
    }

    private String prepareModelForEmptyCart(Cart cart, Model model) {
        model.addAttribute("isEmpty", env.getProperty("emptyCartMessage"));
        model.addAttribute("cart", cart);
        return "cart";
    }

    private void prepareModelForErrors(Model model, List<Long> errorsId, List<Phone> outOfStockPhones) {
        model.addAttribute("errorsId", errorsId);
        model.addAttribute("outOfStockPhones", outOfStockPhones);
        model.addAttribute("error", env.getProperty("updateCartMessageError"));
    }

    private List<Long> failedValidation(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors("quantity");
        return errors.stream()
                .map(item -> Long.parseLong(item.getCode()))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(NoElementWithSuchIdException.class)
    public String handle(NoElementWithSuchIdException ex) {
        return "redirect:/404?message=" + env.getProperty("noSuchIdException") + ex.getId();
    }
}
