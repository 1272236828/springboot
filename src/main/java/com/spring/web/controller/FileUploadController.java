package com.spring.web.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
public class FileUploadController {

    // 下载功能的页面
    @RequestMapping("/uploadFile")
    public String fileUploadController() {
        return "/html/uploadFile";
    }

    // 实现下载功能
    @RequestMapping("/upload")
    public String upload(HttpServletRequest request,
                         @RequestParam("file") MultipartFile file)
            throws IllegalStateException, IOException {
        if (!file.isEmpty()) {
            String path = request.getServletContext().getRealPath("/uploadFiles/");
            System.out.println(path);
            String fileName = file.getOriginalFilename();
            File filePath = new File(path + File.separator + fileName);
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }
            file.transferTo(filePath);
        }
        return "forward:/showDownload";
    }

    @RequestMapping("/showDownload")
    public String showDownLoad(HttpServletRequest request, Model module) {
        String path = request.getServletContext().getRealPath("/uploadFiles/");
        File fileDir = new File(path);
        File fileList[] = fileDir.listFiles();
        module.addAttribute("filesList", fileList);
        return "/html/showFile";
    }

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(
            HttpServletRequest request,
            @RequestParam("filename") String filename,
            @RequestHeader("User-Agent") String userAgent
    ) throws IOException {
        String path = request.getServletContext().getRealPath("/uploadFiles/");
        File downloadFile = new File(path + File.separator + filename);
        BodyBuilder builder = ResponseEntity.ok();
        builder.contentLength(downloadFile.length());
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        filename = URLEncoder.encode(filename, "UTF-8");
        if (userAgent.indexOf("MSIE") > 0) {
            builder.header("Content-Disposition", "attachment;filename=" + filename);
        } else {
            builder.header("Content-Disposition", "attachment; filename* = UTF-8''" + filename);
        }
        return builder.body(FileUtils.readFileToByteArray(downloadFile));
    }

}
