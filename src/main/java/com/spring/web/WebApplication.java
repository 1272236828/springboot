package com.spring.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        String rootPath = System.getProperty("user.dir");
        File uploadFilePath = new File(rootPath + "/static/");
        if (!uploadFilePath.exists()) {
            uploadFilePath.mkdirs();
        }
        SpringApplication.run(WebApplication.class, args);
    }

}
