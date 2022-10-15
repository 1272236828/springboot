package com.spring.web.entity;

public class UploadFile {

    public int fileID;

    public String fileName;

    public String filePath;

    public String fileSHA256;

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSHA256(String fileSHA256) {
        this.fileSHA256 = fileSHA256;
    }

    public int getFileID() {
        return fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileSHA256() {
        return fileSHA256;
    }
}
