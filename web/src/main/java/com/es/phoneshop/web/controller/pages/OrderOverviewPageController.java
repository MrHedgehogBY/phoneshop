package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.order.Order;
import com.es.core.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {

    @Autowired
    private Environment env;

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getOrderOverview(@PathVariable("id") String id, Model model) {
        Order order = orderService.getOrder(id);
        model.addAttribute("order", order);
        return "orderOverview";
    }

    @ExceptionHandler(NoElementWithSuchIdException.class)
    public String handle(NoElementWithSuchIdException ex) {
        return "redirect:/404?message=" + env.getProperty("noSuchIdException") + ex.getId();
    }
}
