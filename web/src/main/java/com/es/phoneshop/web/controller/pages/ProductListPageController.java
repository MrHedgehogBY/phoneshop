package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.service.cart.CartService;
import com.es.core.service.filter.FilterService;
import com.es.core.service.phone.PhoneService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@PropertySource("classpath:/values.properties")
@RequestMapping(value = "/productList")
public class ProductListPageController {

    @Resource
    private PhoneService phoneService;

    @Resource
    private CartService cartService;

    @Resource
    private FilterService filterServiceImpl;

    @Resource
    private HttpSession httpSession;

    @Value("${value.quantityOnPage}")
    private Long quantityOnPage;


    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) String field,
                                  @RequestParam(required = false) String order,
                                  @RequestParam(required = false) Long page, Model model) {
        if (page == null) {
            page = 1L;
        }
        field = filterServiceImpl.checkFieldValue(field);
        order = filterServiceImpl.checkOrderValue(order);
        List<Phone> phoneList = phoneService.findAllPhones(search, field, order,
                ((Long) ((page - 1) * quantityOnPage)).intValue(), quantityOnPage.intValue());
        Long phoneQuantity = phoneService.countPhones(search, field, order, ((Long) ((page - 1) * quantityOnPage)).intValue(),
                quantityOnPage.intValue());
        Long lastPage;
        Long pagesQuantity = phoneQuantity / quantityOnPage;
        lastPage = (phoneQuantity % quantityOnPage != 0 ? pagesQuantity + 1 : pagesQuantity);
        model.addAttribute("phones", phoneList);
        model.addAttribute("cart", cartService.getCart(httpSession));
        model.addAttribute("pages", lastPage);
        model.addAttribute("phoneQuantity", phoneQuantity);
        return "productList";
    }
}
