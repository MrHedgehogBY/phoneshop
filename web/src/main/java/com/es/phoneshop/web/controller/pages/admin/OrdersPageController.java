package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {

    @Autowired
    private Environment env;

    @Resource
    private OrderDao orderDao;

    @RequestMapping(method = RequestMethod.GET)
    public String showOrders(@RequestParam(required = false) Long page, Model model) {
        if (page == null) {
            page = 1L;
        }
        Long quantityOnPage = Long.parseLong(env.getProperty("value.quantityOnPage"));
        Long orderQuantity = orderDao.count();
        Long pagesQuantity = orderQuantity / quantityOnPage;
        Long lastPage = (orderQuantity % quantityOnPage != 0 ? pagesQuantity + 1 : pagesQuantity);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("orders", orderDao.findAll((int) ((page - 1) * quantityOnPage), quantityOnPage.intValue()));
        return "adminOrders";
    }
}
