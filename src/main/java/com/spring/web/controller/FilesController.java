package com.spring.web.controller;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.UploadFile;
import com.spring.web.entity.User;
import com.spring.web.entity.UserToFile;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class FilesController {
    @Autowired
    private SQLUserService sqlUserService;
    // 下载功能的页面
    @RequestMapping("/uploadFile")
    public String fileUploadController() {
        return "uploadFile";
    }

    // 实现下载功能
    @RequestMapping("/upload")
    public String upload(HttpServletRequest request,
                         SQLUser sqlUser,
                         @RequestParam("file") MultipartFile[] files)
            throws IllegalStateException, IOException {
        for (MultipartFile file: files) {
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

                    // 排除重名文件
                    uploadFile.setFilePath(path + File.separator + fileName);
                    if (filePath.exists()) {
                        filePath = new File(path + File.separator + "download" + fileName);
                        uploadFile.setFilePath(path + File.separator + "download" + fileName);
                        uploadFile.setFileName("download" + uploadFile.getFileName());
                    } else {
                        uploadFile.setFilePath(path + File.separator + fileName);
                    }
                    if (!filePath.getParentFile().exists()) {
                        filePath.getParentFile().mkdirs();
                    }

                    file.transferTo(filePath);
                    sqlUserService.addFileToSQL(sqlUser, uploadFile);
                }

                String username = (String) request.getSession().getAttribute("username");
                String path = (String) request.getSession().getAttribute("path");
                UserToFile user = (UserToFile) request.getSession().getAttribute("user");
                List<UserToFile> list = new ArrayList<UserToFile>();
                checkFlag = sqlUserService.checkFileExists(sqlUser, uploadFile);
                UserToFile userToFile = new UserToFile(username, fileName,
                        String.valueOf(checkFlag), path, (ArrayList<UserToFile>) list);

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

                user.addChildren(userToFile);
                request.getSession().setAttribute("user", backup);
                sqlUserService.updateFile(sqlUser, backup, username);
            }
        }
        return "redirect:/personal";
    }

    @RequestMapping("/showDownload")
    public String showDownLoad(HttpServletRequest request, Model module) {
        String path = request.getServletContext().getRealPath("/uploadFiles/");
        File fileDir = new File(path);
        File fileList[] = fileDir.listFiles();
        module.addAttribute("filesList", fileList);
        return "showFile";
    }

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(
            HttpServletRequest request,
            @RequestParam("fileID") String fileID,
            @RequestHeader("User-Agent") String userAgent,
            SQLUser sqlUser
    ) throws IOException {
        UploadFile file = sqlUserService.queryFilePath(sqlUser, fileID);
        String path = file.getFilePath();
        File downloadFile = new File(path);
        BodyBuilder builder = ResponseEntity.ok();
        // 设置传输文件长度
        builder.contentLength(downloadFile.length());
        // 使用二进制流传输
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = URLEncoder.encode(file.fileName, "UTF-8");
        // 如果是ie浏览器则 如果不是则
        if (userAgent.indexOf("MSIE") > 0) {
            builder.header("Content-Disposition", "attachment;filename=" + filename);
        } else {
            builder.header("Content-Disposition", "attachment; filename* = UTF-8''" + filename);
        }
        return builder.body(FileUtils.readFileToByteArray(downloadFile));
    }

    @RequestMapping("delete")
    public void delete(
            HttpServletRequest request,
            @RequestParam("filename") String filename
    ) throws IOException {

    }
}
