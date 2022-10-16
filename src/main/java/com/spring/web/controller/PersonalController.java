package com.spring.web.controller;

import com.spring.web.entity.UserToFile;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PersonalController {
    @RequestMapping("personal")
    public String personal(Model model, HttpServletRequest request) {
        request.getSession().setAttribute("paths", "/myfile/list");
        return "personal";
    }

    @RequestMapping("/createDir")
    public String createDir(Model model, HttpServletRequest request) {

        String paths = (String) request.getSession().getAttribute("paths");
        for(String path: paths.split("/")) {
            System.out.println(path);
        }
        return "personal";
    }
}
