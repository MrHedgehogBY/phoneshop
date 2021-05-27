package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDTO;
import com.es.core.service.cart.CartService;
import com.es.core.service.filter.FilterService;
import com.es.core.service.phone.PhoneService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private Integer quantityOnPage;


    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) String field,
                                  @RequestParam(required = false) String order,
                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                  @ModelAttribute(name = "phoneDTO") PhoneDTO phoneDTO, Model model) {
        field = filterServiceImpl.checkFieldValue(field);
        order = filterServiceImpl.checkOrderValue(order);
        List<Phone> phoneList = phoneService.findAllPhones(search, field, order,
                (page - 1) * quantityOnPage, quantityOnPage);
        Long phoneQuantity = phoneService.countPhones(search, field, order, (page - 1) * quantityOnPage,
                quantityOnPage);
        calculateAndAddLastPageNumberToModel(phoneQuantity, model);
        model.addAttribute("phones", phoneList);
        model.addAttribute("cart", cartService.getCart(httpSession));
        return "productList";
    }

    private void calculateAndAddLastPageNumberToModel(Long phoneQuantity, Model model) {
        Long pagesQuantity = phoneQuantity / quantityOnPage;
        Long LastPageNumber = (phoneQuantity % quantityOnPage != 0 ? pagesQuantity + 1 : pagesQuantity);
        model.addAttribute("pages", LastPageNumber);
        model.addAttribute("phoneQuantity", phoneQuantity);
    }
}
