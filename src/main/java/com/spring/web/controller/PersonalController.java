package com.spring.web.controller;

import com.alibaba.fastjson.JSON;
import com.spring.web.entity.SQLUser;
import com.spring.web.entity.User;
import com.spring.web.entity.UserToFile;
import com.spring.web.service.SQLUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PersonalController {
    @Autowired
    private SQLUserService sqlUserService;
    @RequestMapping("/personal")
    public String personal(Model model, HttpServletRequest request,
                           HttpServletResponse response,
                           SQLUser sqlUser){
        String username = (String) request.getSession().getAttribute("username");
        UserToFile user = sqlUserService.queryFileList(sqlUser, username);
        String path = "/";
        if (request.getSession().getAttribute("path") == null) {
            request.getSession().setAttribute("path", path);
        }
        path = (String) request.getSession().getAttribute("path");
        model.addAttribute("path", path);
        if(!path.equals("/")) {
            for (String part: path.split("/")) {
                for(UserToFile userToFile: user.getChildren()) {
                    if (userToFile.getFileName().equals(part)) {
                        user = userToFile;
                        break;
                    }
                }
            }
        }
        System.out.println(JSON.toJSON(user));
        model.addAttribute("prePath", user.getParentPath());
        model.addAttribute("userList", user.children);
        request.getSession().setAttribute("user", user);
        return "/personal";
    }

    @RequestMapping("/createDir")
    public String createDir(HttpServletRequest request,
                            SQLUser sqlUser) {
        String username = (String) request.getSession().getAttribute("username");
        String path = (String) request.getSession().getAttribute("path");

        UserToFile userToFile = new UserToFile(username, "新建文件夹",
                "0", "/", new ArrayList<UserToFile>());
        UserToFile user = sqlUserService.queryFileList(sqlUser, username);
        UserToFile backup = user;
        if(!path.equals("/")) {
            for (String part: path.split("/")) {
                for(UserToFile userToFile1: user.getChildren()) {
                    if (userToFile1.getFileName().equals(part)) {
                        user = userToFile1;
                        break;
                    }
                }
            }
        }
        for(UserToFile userToFile1: user.children) {
            if(userToFile1.getFileName().equals(userToFile.getFileName())) {
                userToFile.setFileName(userToFile.getFileName() + "!");
                userToFile.setParentPath(path);
            }
        }
        System.out.println(path);
        user.addChildren(userToFile);
        sqlUserService.updateFile(sqlUser, backup, username);
        return "redirect:/personal";
    }

    @RequestMapping("/moveToNext")
    public String moveToNext(@RequestParam String fileName, @RequestParam String path,
                             Model model, HttpServletRequest request) {
        if (!path.equals("/")) {
            path = path + File.separator + fileName;
            request.getSession().setAttribute("path", path);
        } else {
            path = File.separator + fileName;
            request.getSession().setAttribute("path", path);
        }
        System.out.println((String) request.getSession().getAttribute("path"));
        request.getSession().setAttribute("perPath", path);
        return "redirect:/personal";
    }

    @RequestMapping("/moveToPre")
    public String moveToPre(@RequestParam String path,
                             Model model, HttpServletRequest request) {

        request.getSession().setAttribute("path", path);
        return "redirect:/personal";
    }

    @RequestMapping("/deleteDir")
    public String deleteDir(HttpServletRequest request, @RequestParam String filename, SQLUser sqlUser) {

        String path = (String) request.getSession().getAttribute("path");
        UserToFile user = sqlUserService.queryFileList(sqlUser, (String) request.getSession().getAttribute("username"));
        UserToFile backup = user;
        if(!path.equals("/")) {
            for (String part: path.split("/")) {
                for(UserToFile userToFile1: user.getChildren()) {
                    if (userToFile1.getFileName().equals(part)) {
                        user = userToFile1;
                        break;
                    }
                }
            }
        }
        for (UserToFile userToFile: user.getChildren()) {
            if(userToFile.getFileName().equals(filename)) {
                user.getChildren().remove(userToFile);
                break;
            }
        }
        sqlUserService.updateFile(sqlUser, backup, (String) request.getSession().getAttribute("username"));
        return "redirect:/personal";
    }

    @RequestMapping("/changeDir")
    public String changeDir(HttpServletRequest request, @RequestParam String file, @RequestParam String filename, SQLUser sqlUser) {
        UserToFile user = sqlUserService.queryFileList(sqlUser, (String) request.getSession().getAttribute("username"));
        if (!filename.isEmpty()) {
            for (UserToFile userToFile: user.getChildren()) {
                if(userToFile.getFileName().equals(filename)) {
                    userToFile.setFileName(file);
                }
            }
        }
        sqlUserService.updateFile(sqlUser, user, (String) request.getSession().getAttribute("username"));
        return "redirect:/personal";
    }
}
