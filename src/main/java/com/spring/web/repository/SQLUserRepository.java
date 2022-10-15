package com.spring.web.repository;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.UploadFile;

import java.io.File;

public interface SQLUserRepository {
    public int test(SQLUser sqlUser);
    public void addFileToSQL(SQLUser sqlUser, UploadFile uploadFile);
    public int checkFileExists(SQLUser sqlUser, UploadFile uploadFile);
}
