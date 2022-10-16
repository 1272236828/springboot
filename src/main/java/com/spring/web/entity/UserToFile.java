package com.spring.web.entity;


import java.io.File;
import java.util.ArrayList;

public class UserToFile {
    public String username;
    public String fileName;
    public String fileID;
    public String parentPath;
    public ArrayList<UserToFile> children;

    public UserToFile(String username, String fileName, String fileID,
                      String parentPath, ArrayList<UserToFile> children) {
        setUsername(username);
        setFileName(fileName);
        setFileID(fileID);
        setParentPath(parentPath);
        setChildren(children);
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public ArrayList<UserToFile> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<UserToFile> children) {
        this.children = children;
    }

    public void addChildren(UserToFile file) {
        if (this.parentPath.equals("/")) {
            file.setParentPath(this.parentPath);
        } else {
            file.setParentPath(this.parentPath + File.separator + this.fileName);
        }
        this.children.add(file);
    }
}
