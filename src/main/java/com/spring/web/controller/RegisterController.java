package com.spring.web.controller;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.User;
import com.spring.web.entity.UserToFile;
import com.spring.web.service.SQLUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegisterController {
    @Autowired
    private SQLUserService sqlUserService;
    @RequestMapping("register")
    public String register() {

        return "register";
    }

    @RequestMapping("/toRegister")
    public String toRegister(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             SQLUser sqlUser) {
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setAdminAccount(0);
        if (sqlUserService.addUserToSQL(sqlUser, user) == 1) {
            ArrayList<UserToFile> list = new ArrayList<UserToFile>();
            UserToFile userToFile = new UserToFile(username, "/",
                    "0", "/", (ArrayList<UserToFile>) list);
            sqlUserService.updateFile(sqlUser, userToFile, username);
            return "login";
        }
    }
    return"register";
    }
}
