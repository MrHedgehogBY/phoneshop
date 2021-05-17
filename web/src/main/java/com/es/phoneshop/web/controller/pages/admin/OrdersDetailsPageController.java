package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.IncorrectUrlParameterException;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderStatus;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;


import javax.annotation.Resource;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/orders/{id}")
public class OrdersDetailsPageController {

    @Resource
    private OrderDao jdbcOrderDao;

    @Autowired
    private Environment env;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(@PathVariable("id") Long id, Model model) {
        Optional<Order> currentOrder = jdbcOrderDao.get(id);
        if (currentOrder.isPresent()) {
            model.addAttribute("order", currentOrder.get());
        } else {
            throw new NoElementWithSuchIdException(id);
        }
        return "adminOrderDetails";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String changeStatus(@RequestParam(required = false) String status, @PathVariable("id") Long id) {
        Optional<Order> currentOrder = jdbcOrderDao.get(id);
        if (currentOrder.isPresent()) {
            updateStatus(status, currentOrder.get());
        } else {
            throw new NoElementWithSuchIdException(id);
        }
        return "redirect:/admin/orders/" + id;
    }

    private void updateStatus(String status, Order order) {
        status = status.toUpperCase();
        if (EnumUtils.isValidEnum(OrderStatus.class, status)) {
            order.setStatus(OrderStatus.valueOf(status));
            jdbcOrderDao.updateStatus(order.getId(), order.getStatus());
        } else {
            throw new IncorrectUrlParameterException(status);
        }
    }

    @ExceptionHandler(NoElementWithSuchIdException.class)
    public String handleNoElement(NoElementWithSuchIdException ex) {
        return "redirect:/404?message=" + env.getProperty("noSuchIdException") + ex.getId();
    }

    @ExceptionHandler(IncorrectUrlParameterException.class)
    public String handleIncorrectParameter(IncorrectUrlParameterException ex) {
        return "redirect:/404?message=" + env.getProperty("incorrectParameter") + ex.getParameter();
    }
}
