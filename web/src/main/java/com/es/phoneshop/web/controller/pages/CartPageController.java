package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.phone.PhoneDTO;
import com.es.core.service.cart.CartService;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneArrayDTO;
import com.es.core.service.phone.PhoneService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@PropertySource("classpath:/lang-en.properties")
@RequestMapping(value = "/cart")
public class CartPageController {

    @Resource
    private Environment env;

    @Resource
    private PhoneService phoneService;

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
        model.addAttribute("phoneArrayDTO", new PhoneArrayDTO(cart.getCartItems()));
        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@Valid @ModelAttribute(name = "phoneArrayDTO") PhoneArrayDTO phoneArrayDTO,
                             BindingResult bindingResult, Model model) {
        Cart cart = cartService.getCart(httpSession);
        List<Long> updatedPhoneIds = new ArrayList<>();
        IntStream.range(0, phoneArrayDTO.getPhoneDTOItems().size()).forEach(i -> {
            PhoneDTO currentPhoneDTO = phoneArrayDTO.getPhoneDTOItems().get(i);
            if (bindingResult.getFieldErrors("phoneDTOItems[" + i + "].id").isEmpty()
                    && bindingResult.getFieldErrors("phoneDTOItems[" + i + "].quantity").isEmpty()) {
                updateItemInCart(currentPhoneDTO, cart, bindingResult, i, updatedPhoneIds);
            }
        });
        model.addAttribute("cart", cart);
        model.addAttribute("updatedPhoneIds", updatedPhoneIds);
        model.addAttribute("successfulUpdateMessage", env.getProperty("successfulUpdateMessage"));
        model.addAttribute("phoneArrayDTO", phoneArrayDTO);
        return "cart";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String deleteFromCart(@PathVariable("id") Long id, Model model) throws NoElementWithSuchIdException {
        Phone currentPhone = phoneService.getPhone(id.toString());
        Cart cart = cartService.getCart(httpSession);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model);
        }
        cartService.removePhone(currentPhone.getId(), cart);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model);
        }
        model.addAttribute("message", env.getProperty("deleteFromCartMessage"));
        model.addAttribute("phoneArrayDTO", new PhoneArrayDTO(cart.getCartItems()));
        model.addAttribute("cart", cart);
        return "cart";
    }

    private void updateItemInCart(PhoneDTO phoneDTO, Cart cart, BindingResult bindingResult,
                                  int index, List<Long> updatedPhoneIds) {
        Long id = phoneDTO.getId();
        Long quantity = phoneDTO.getQuantity();
        if (id != null && quantity != null) {
            try {
                cartService.updatePhone(id, quantity, cart);
                updatedPhoneIds.add(id);
            } catch (OutOfStockException e) {
                bindingResult.rejectValue("phoneDTOItems[" + index + "].quantity",
                        "OutOfStock.phoneArrayDTO.phoneDTOItems.quantity");
            } catch (EmptyDatabaseArgumentException e) {
                bindingResult.rejectValue("phoneDTOItems[" + index + "].id",
                        "NoElement.phoneArrayDTO.phoneDTOItems.id");
            }
        }
    }

    private String prepareModelForEmptyCart(Cart cart, Model model) {
        model.addAttribute("isEmpty", env.getProperty("emptyCartMessage"));
        model.addAttribute("cart", cart);
        return "cart";
    }

    @ExceptionHandler(NoElementWithSuchIdException.class)
    public String handle(NoElementWithSuchIdException ex) {
        return "redirect:/404?message=" + env.getProperty("noSuchIdException") + ex.getId();
    }
}
