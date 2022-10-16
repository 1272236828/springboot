package com.spring.web.controller;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.User;
import com.spring.web.entity.UserToFile;
import com.spring.web.service.SQLUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PersonalController {
    @Autowired
    private SQLUserService sqlUserService;
    @RequestMapping("personal")
    public String personal(Model model, HttpServletRequest request,
                           HttpServletResponse response,
                           SQLUser sqlUser){

        List<UserToFile> list = new ArrayList<UserToFile>();
        UserToFile user = new UserToFile("username", "bbbbb",
                "1", "/a", (ArrayList<UserToFile>) list);
        UserToFile user1 = new UserToFile("username", "a",
                "2", "/", (ArrayList<UserToFile>) list);
        user.addChildren(user1);
        sqlUserService.updateFile(sqlUser, user, "111");
        return "personal";
    }

    @RequestMapping("/createDir")
    public String createDir(Model model, HttpServletRequest request) {

        return "personal";
    }
}
