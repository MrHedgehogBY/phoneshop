package com.es.phoneshop.web.controller.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/login")
public class LoginPageController {

    @Autowired
    private Environment env;

    @RequestMapping(method = RequestMethod.GET)
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", env.getProperty("incorrectUsernameOrPassword"));
        }
        return "login";
    }
}
