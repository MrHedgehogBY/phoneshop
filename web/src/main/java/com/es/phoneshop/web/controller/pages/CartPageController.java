package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.cart.PhoneArrayDTO;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @Value("${emptyCartMessage}")
    private String emptyCartMessage;

    @Value("${deleteFromCartMessage}")
    private String deleteFromCartMessage;

    @Value("${updateCartMessageError}")
    private String updateCartMessageError;

    @Value("${updateCartMessageSuccess}")
    private String updateCartMessageSuccess;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        model.addAttribute("cart", cartService.getCart(httpSession));
        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@Validated @ModelAttribute(name = "phoneArrayDTO") PhoneArrayDTO phoneArrayDTO,
                             Model model, BindingResult bindingResult) {
        phoneArrayDTOValidator.validate(phoneArrayDTO, bindingResult);
        Cart cart = cartService.getCart(httpSession);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model);
        }
        if (bindingResult.hasErrors()) {
            return failedValidation(cart, bindingResult, model);
        }
        HashMap<Long, Long> idQuantityMap = new HashMap<>();
        IntStream.of(0, phoneArrayDTO.getQuantity().length - 1).forEach(i -> {
            idQuantityMap.put(Long.parseLong(phoneArrayDTO.getPhoneId()[i]),
                    Long.parseLong(phoneArrayDTO.getQuantity()[i]));
        });
        List<Long> outOfStockId = new ArrayList<>();
        cartService.update(idQuantityMap, cart, outOfStockId);
        if (outOfStockId.isEmpty()) {
            model.addAttribute("message", updateCartMessageSuccess);
        } else {
            model.addAttribute("outOfStockId", outOfStockId);
            model.addAttribute("error", updateCartMessageError);
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
            model.addAttribute("message", deleteFromCartMessage);
            model.addAttribute("cart", cart);
        } else {
            throw new NoElementWithSuchIdException();
        }
        return "cart";
    }

    private String prepareModelForEmptyCart(Cart cart, Model model) {
        model.addAttribute("error", emptyCartMessage);
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
        model.addAttribute("error", updateCartMessageError);
        return "cart";
    }
}
