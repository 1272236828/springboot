package com.spring.web.entity;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public class UserToFile {
    public String username;
    public String fileName;
    public String fileID;
    public String parentPath;
    public List<UserToFile> children;

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

    public List<UserToFile> getChildren() {
        return children;
    }

    public void setChildren(List<UserToFile> children) {
        this.children = children;
    }

    public void addChildren(List<UserToFile> file, String paths, UserToFile list) {

    }
}
