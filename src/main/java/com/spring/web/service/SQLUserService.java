package com.spring.web.service;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.UploadFile;
import com.spring.web.entity.User;
import com.spring.web.entity.UserToFile;

public interface SQLUserService {
    public int test(SQLUser sqlUser);
    public void addFileToSQL(SQLUser sqlUser, UploadFile uploadFile);
    public int checkFileExists(SQLUser sqlUser, UploadFile uploadFile);
    public int addUserToSQL(SQLUser sqlUser, User user);
    public  int checkUser(SQLUser sqlUser, User user);
    public void updateFile(SQLUser sqlUser, UserToFile uploadFile, String username);

    public UserToFile queryFileList(SQLUser sqlUser, String username);
    public UploadFile queryFilePath(SQLUser sqlUser, String fileID);
}
