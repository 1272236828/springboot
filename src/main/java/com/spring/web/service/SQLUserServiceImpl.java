package com.spring.web.service;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.UploadFile;
import com.spring.web.entity.User;
import com.spring.web.entity.UserToFile;
import com.spring.web.repository.SQLUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SQLUserServiceImpl implements SQLUserService{
    @Autowired
    private SQLUserRepository sqlUserRepository;

    @Override
    public int test(SQLUser sqlUser) {
        return sqlUserRepository.test(sqlUser);
    }

    @Override
    public void addFileToSQL(SQLUser sqlUser, UploadFile uploadFile) {
        sqlUserRepository.addFileToSQL(sqlUser, uploadFile);
    }

    @Override
    public int checkFileExists(SQLUser sqlUser, UploadFile uploadFile) {
        return sqlUserRepository.checkFileExists(sqlUser, uploadFile);
    }

    public void updateFile(SQLUser sqlUser, UserToFile userToFile, String username){
        sqlUserRepository.updateFile(sqlUser, userToFile, username);
    }
    public int addUserToSQL(SQLUser sqlUser, User user){
        return sqlUserRepository.addUserToSQL(sqlUser,user);
    }
    public  int checkUser(SQLUser sqlUser, User user){
        return sqlUserRepository.checkUser(sqlUser, user);
    }

    public UserToFile queryFileList(SQLUser sqlUser, String username) {
        return sqlUserRepository.queryFileList(sqlUser, username);
    }
}
