package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.filter.FilterService;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.search.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {

    @Resource
    private PhoneDao phoneDao;

    @Resource
    private CartService cartService;

    @Resource
    private SearchService searchServiceImpl;

    @Resource
    private FilterService filterServiceImpl;

    private static final Long QUANTITY_ON_PAGE = 10L;


    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) String field,
                                  @RequestParam(required = false) String order,
                                  @RequestParam(required = false) Long page, Model model) {
        List<Phone> phoneList = phoneDao.findAll(0, Integer.MAX_VALUE);
        Stream<Phone> phoneStream = searchServiceImpl.searchPhones(search, phoneList.stream());
        phoneStream = filterServiceImpl.filterPhones(field, order, phoneStream);
        phoneList = phoneStream.collect(Collectors.toList());
        Long lastPage = ((Integer) phoneList.size()).longValue() / QUANTITY_ON_PAGE;
        Long remainder = ((Integer) phoneList.size()).longValue() % QUANTITY_ON_PAGE;
        if (!remainder.equals(0L)) {
            lastPage++;
        }
        Stream<Phone> stream = phoneList.stream();
        if (page != null && page.equals(0L) && !page.equals(lastPage)) {
            stream = phoneList.stream().skip((page - 1L) * QUANTITY_ON_PAGE);
        }
        stream = stream.limit(QUANTITY_ON_PAGE);
        model.addAttribute("phones", stream.collect(Collectors.toList()));
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("pages", lastPage);
        return "productList";
    }
}
