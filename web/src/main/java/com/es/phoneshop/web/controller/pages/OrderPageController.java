package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.OrderDataDTO;
import com.es.core.service.cart.CartService;
import com.es.core.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {

    @Autowired
    private Environment env;

    @Resource
    private OrderService orderService;

    @Resource
    private HttpSession httpSession;

    @Resource
    private CartService cartService;

    @Resource(name = "orderDataDTOValidator")
    private Validator orderDataDTOValidator;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model) {
        Cart cart = cartService.getCart(httpSession);
        prepareModelToShowCart(cart, model);
        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@Validated @ModelAttribute(name = "orderDataDTO") OrderDataDTO orderDataDTO,
                             Model model, BindingResult bindingResult) throws OutOfStockException {
        Cart cart = cartService.getCart(httpSession);
        cartService.checkCartItems(cart);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model);
        }
        orderDataDTOValidator.validate(orderDataDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return prepareModelForValidationErrors(cart, model, bindingResult);
        }
        Long id = orderService.placeOrder(cart, orderDataDTO, Long.parseLong(env.getProperty("delivery.price")));
        cartService.deleteCart(httpSession);
        return "redirect:/orderOverview/" + id;
    }

    private String prepareModelForEmptyCart(Cart cart, Model model) {
        model.addAttribute("error", env.getProperty("emptyCartMessage"));
        prepareModelToShowCart(cart, model);
        return "order";
    }

    private String prepareModelForValidationErrors(Cart cart, Model model, BindingResult bindingResult) {
        model.addAttribute("errors", bindingResult);
        prepareModelToShowCart(cart, model);
        return "order";
    }

    private void prepareModelToShowCart(Cart cart, Model model) {
        model.addAttribute("cart", cart);
        model.addAttribute("deliveryPrice", Long.parseLong(env.getProperty("delivery.price")));
        model.addAttribute("totalPrice",
                cart.getTotalCost().add(BigDecimal.valueOf(Long.parseLong(env.getProperty("delivery.price")))));
    }

    @ExceptionHandler(OutOfStockException.class)
    public String handleOutOfStock() {
        return "redirect:/404?message=" + env.getProperty("outOfStock");
    }

    @ExceptionHandler(NoElementWithSuchIdException.class)
    public String handleNoElementWithSuchId(NoElementWithSuchIdException e) {
        return "redirect:/404?message=" + env.getProperty("noSuchIdException") + e.getId();
    }
}
