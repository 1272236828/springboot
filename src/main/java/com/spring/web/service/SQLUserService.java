package com.spring.web.service;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.UploadFile;

public interface SQLUserService {
    public int test(SQLUser sqlUser);
    public void addFileToSQL(SQLUser sqlUser, UploadFile uploadFile);
    public int checkFileExists(SQLUser sqlUser, UploadFile uploadFile);
}
