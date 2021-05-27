package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.b2b.B2bCartDTO;
import com.es.core.model.b2b.B2bCartItemDTO;
import com.es.core.model.cart.Cart;
import com.es.core.service.cart.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/b2b")
public class B2BAddingController {

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @RequestMapping(method = RequestMethod.GET)
    public String showB2B(Model model) {
        model.addAttribute("b2bCartDTO", new B2bCartDTO());
        return "b2bPage";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addToCart(@Valid @ModelAttribute(name = "b2bCartDTO") B2bCartDTO b2bCartDTO,
                            BindingResult bindingResult, Model model) {
        Cart cart = cartService.getCart(httpSession);
        List<Long> addedToCartProductIds = new ArrayList<>();
        IntStream.range(0, b2bCartDTO.getB2bCartItems().size()).forEach(i -> {
            B2bCartItemDTO currentB2bCartItemDTO = b2bCartDTO.getB2bCartItems().get(i);
            validateNullRequiredFields(bindingResult, currentB2bCartItemDTO, i);
            if (bindingResult.getFieldErrors("b2bCartItems[" + i + "].id").isEmpty()
                    && bindingResult.getFieldErrors("b2bCartItems[" + i + "].quantity").isEmpty()) {
                addItemToCart(currentB2bCartItemDTO, cart, bindingResult, addedToCartProductIds, i);
            }
        });
        model.addAttribute("addedToCartProductIds", addedToCartProductIds);
        model.addAttribute("b2bCartDTO", b2bCartDTO);
        return "b2bPage";
    }

    private void validateNullRequiredFields(BindingResult bindingResult, B2bCartItemDTO cartItemDTO, int index) {
        if (cartItemDTO.getId() != null && cartItemDTO.getQuantity() == null) {
            bindingResult.rejectValue("b2bCartItems[" + index + "].quantity",
                    "Null.b2bCartDTO.b2bCartItems.quantity");
        } else if (cartItemDTO.getId() == null && cartItemDTO.getQuantity() != null) {
            bindingResult.rejectValue("b2bCartItems[" + index + "].id",
                    "Null.b2bCartDTO.b2bCartItems.id");
        }
    }

    private void addItemToCart(B2bCartItemDTO currentB2bCartItemDTO, Cart cart,
                               BindingResult bindingResult, List<Long> addedToCartProductIds, int index) {
        Long id = currentB2bCartItemDTO.getId();
        Long quantity = currentB2bCartItemDTO.getQuantity();
        if (id != null && quantity != null) {
            try {
                cartService.addPhone(id, quantity, cart);
                addedToCartProductIds.add(id);
                currentB2bCartItemDTO.setId(null);
                currentB2bCartItemDTO.setQuantity(null);
            } catch (OutOfStockException e) {
                bindingResult.rejectValue("b2bCartItems[" + index + "].quantity",
                        "OutOfStock.b2bCartDTO.b2bCartItems.quantity");
            } catch (EmptyDatabaseArgumentException e) {
                bindingResult.rejectValue("b2bCartItems[" + index + "].id",
                        "NoElement.b2bCartDTO.b2bCartItems.id");
            }
        }
    }
}
