package com.spring.web.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spring.web.entity.SQLUser;
import com.spring.web.entity.UploadFile;
import com.spring.web.entity.User;
import com.spring.web.entity.UserToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.*;

@Repository
public class SQLUserRepositoryImpl implements SQLUserRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int test(SQLUser sqlUser) {
        String FileSQL = "CREATE TABLE IF NOT EXISTS UploadFiles(" +
                "FileID INT NOT NULL AUTO_INCREMENT," +
                "FileName VARCHAR(255) NOT NULL," +
                "FileSHA256 VARCHAR(255) NOT NULL," +
                "FilePath VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (FileID)" +
                ")";
        String UserSQL = "CREATE TABLE IF NOT EXISTS UserList(" +
                "UserID INT NOT NULL AUTO_INCREMENT," +
                "UserName VARCHAR(255) NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "adminAccount INT NOT NULL," +
                "PRIMARY KEY (UserID)" +
                ")";
        String MidSQL = "CREATE TABLE IF NOT EXISTS MidList(" +
                "ID INT NOT NULL AUTO_INCREMENT," +
                "UserName VARCHAR(255) NOT NULL," +
                "FileList VARCHAR(255)," +
                "PRIMARY KEY (ID))";
        jdbcTemplate.execute(UserSQL);
        jdbcTemplate.execute(FileSQL);
        jdbcTemplate.execute(MidSQL);
        return 0;
    }
    @Override
    public void addFileToSQL(SQLUser sqlUser, UploadFile uploadFile) {
        String sql = "INSERT INTO UploadFiles (FileName, FileSHA256, FilePath) values ('"
                + uploadFile.getFileName() + "','" + uploadFile.getFileSHA256() +
                "','" + uploadFile.getFilePath() + "')";
            jdbcTemplate.update(sql);
    }

    @Override
    public int checkFileExists(SQLUser sqlUser, UploadFile uploadFile) {
        String sql = "SELECT * FROM UploadFiles WHERE FileSHA256 = '" + uploadFile.getFileSHA256() + "'";
        List<Map<String, Object>> queryAnswer = jdbcTemplate.queryForList(sql);
        if (queryAnswer.isEmpty()) {
            return 0;
        }
        else {
            System.out.println(queryAnswer.get(0).keySet());
            for (String key: queryAnswer.get(0).keySet()) {
                if (key.equals("FileID")) {
                    return (int) queryAnswer.get(0).get(key);
                }
            }
        }
        return 0;
    }

    @Override
    public void updateFile(SQLUser sqlUser, UserToFile userToFile, String username) {
        String sql = "SELECT * FROM MidList WHERE UserName = '" + username + "'";
        List<Map<String, Object>> queryAnswer = jdbcTemplate.queryForList(sql);
        if(queryAnswer.isEmpty()){
            String sql1 = "insert into MidList(UserName, FileList) values ('" + username +"','" +
                    JSON.toJSON(userToFile) + "')";
            jdbcTemplate.update(sql1);
        } else {
            String sql2 = "UPDATE MidList SET FileList='" + JSON.toJSON(userToFile)
                    + "' WHERE UserName='" + username + "'";
            jdbcTemplate.update(sql2);
        }
    }

    @Override
    public UserToFile queryFileList(SQLUser sqlUser, String username) {
        String sql = "SELECT * FROM MidList WHERE UserName = '" + username + "'";
        List<Map<String, Object>> queryAnswer = jdbcTemplate.queryForList(sql);
        if(!queryAnswer.isEmpty()){
            System.out.println("1");
            for(String key: queryAnswer.get(0).keySet()) {
                if (key.equals("FileList")) {
                    UserToFile user = JSONObject.parseObject((String) queryAnswer.get(0).get(key), UserToFile.class);
                    return user;
                }
            }
        }
        return null;
    }
    @Override
    public int addUserToSQL(SQLUser sqlUser, User user) {
        String sql = "SELECT * FROM UserList WHERE UserName = '" + user.getUsername() + "'";
        List<Map<String, Object>> queryAnswer = jdbcTemplate.queryForList(sql);
        if(queryAnswer.isEmpty()){
            String sql1 ="insert into UserList(UserName, password, adminAccount) values ('"+ user.getUsername() + "','"
                    + user.getPassword() + "','" + user.isAdminAccount() + "')";
            jdbcTemplate.update(sql1);
            return 1;
        }
        return 0;
    }

    @Override
    public int checkUser(SQLUser sqlUser, User user) {
        String sql = "SELECT * FROM UserList WHERE UserName = '" + user.getUsername() +
                "' AND password='" + user.getPassword() + "'";
        List<Map<String, Object>> queryAnswer = jdbcTemplate.queryForList(sql);
        if(queryAnswer.isEmpty()){
            return 0;
        }return 1;
    }
}
