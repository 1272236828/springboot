package com.spring.web.repository;

import com.spring.web.entity.SQLUser;
import com.spring.web.entity.UploadFile;
import com.spring.web.entity.User;
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
        jdbcTemplate.execute(UserSQL);
        jdbcTemplate.execute(FileSQL);
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
    public void addUserToSQL(SQLUser sqlUser, User user){
        String sql = "";
    }

    @Override
    public  int checkUser(SQLUser sqlUser, User user){
        return 2;
    }
}
