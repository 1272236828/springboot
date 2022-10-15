package com.spring.web.controller;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.UploadFile;
import com.spring.web.service.SQLUserService;
import com.spring.web.util.SHA256Utils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

@Controller
public class FileUploadController {
    @Autowired
    private SQLUserService sqlUserService;
    // 下载功能的页面
    @RequestMapping("/uploadFile")
    public String fileUploadController() {
        return "/html/uploadFile";
    }

    // 实现下载功能
    @RequestMapping("/upload")
    public String upload(HttpServletRequest request,
                         SQLUser sqlUser,
                         @RequestParam("file") MultipartFile file)
            throws IllegalStateException, IOException {
        // 如果文件不为空则向下处理
        if (!file.isEmpty()) {
            // 临时文件夹位置
            String tempPath = request.getServletContext().getRealPath("/tempUploadFiles/");
            // 获取文件名字
            String fileName = file.getOriginalFilename();
            // 获取临时文件夹地址
            File filePath = new File(tempPath + File.separator + fileName);
            //如果临时文件夹不存在则创建一个新的文件夹
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }

            // 将上传文件存入临时文件夹
            file.transferTo(filePath);

            // 构造上传文件对象
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFileName(fileName);
            // 先记录临时文件路径
            uploadFile.setFilePath(tempPath + File.separator + fileName);
            uploadFile.setFileSHA256(SHA256Utils.gatSHA256(filePath));

            //获取判断文件是否重复，如果重复返回相应文件的id如果不重复返回0
            int checkFlag = sqlUserService.checkFileExists(sqlUser, uploadFile);

            // 尝试将已经生成的file文件重新转化成MultiPartFile
            // 获取输入流
            InputStream inputStream = new FileInputStream(filePath);
            file = new MockMultipartFile(filePath.getName(), inputStream);

            // 删除临时文件
            FileUtils.deleteQuietly(filePath);

            // 如果没有出现过文件
            if (checkFlag == 0) {
                // 存入实际文件夹
                String path = request.getServletContext().getRealPath("/uploadFiles/");
                filePath = new File(path + File.separator + fileName);

                uploadFile.setFilePath(path + File.separator + fileName);
                if (!filePath.getParentFile().exists()) {
                    filePath.getParentFile().mkdirs();
                }

                file.transferTo(filePath);
                System.out.println(filePath);
                sqlUserService.addFileToSQL(sqlUser, uploadFile);
            }
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
        // 设置传输文件长度
        builder.contentLength(downloadFile.length());
        // 使用二进制流传输
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        filename = URLEncoder.encode(filename, "UTF-8");
        // 如果是ie浏览器则 如果不是则
        if (userAgent.indexOf("MSIE") > 0) {
            builder.header("Content-Disposition", "attachment;filename=" + filename);
        } else {
            builder.header("Content-Disposition", "attachment; filename* = UTF-8''" + filename);
        }
        return builder.body(FileUtils.readFileToByteArray(downloadFile));
    }

}
