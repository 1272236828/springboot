package com.spring.web.controller;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.User;
import com.spring.web.service.SQLUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@Controller
public class LoginController {
    @Autowired
    private SQLUserService sqlUserService;
    @RequestMapping("/login")
    public String login() {

        return "login";
    }
    @RequestMapping("/toLogin")
    public String toLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          SQLUser sqlUser,
                          HttpServletRequest request,
                          Model model){
        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setAdminAccount(0);
            if(sqlUserService.checkUser(sqlUser, user) == 1){
                request.getSession().setAttribute("username", username);
                return "personal";
            }
        }
        model.addAttribute("status", "登录失败，请重试！");
        return "/login";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.removeAttribute("username");
        return "login";
    }
}
