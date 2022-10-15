package com.spring.web.controller;

import com.spring.web.entity.SQLUser;
import com.spring.web.service.SQLUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JDBCController {
    @Autowired
    private SQLUserService sqlUserService;

    @RequestMapping("/test")
    public int test(SQLUser sqlUser) {
        return sqlUserService.test(sqlUser);
    }
}
