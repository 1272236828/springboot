package com.spring.web.controller;

import com.spring.web.entity.SQLUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
public class RegisterController {
    @RequestMapping("register")
    public String register() {
        return "register";
    }
    @RequestMapping("toRegister")
    public String skip(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       SQLUser sqlUser)
    {
        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){

        }
        else{
            return "register";
        }return "register";
    }
    public String registerController() {
        return "html/register";
    }
}
