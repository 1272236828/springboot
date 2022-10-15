package com.spring.web.repository;

import com.spring.web.entity.SQLUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SQLUserRepositoryImpl implements SQLUserRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int test(SQLUser sqlUser) {
        String sql = "CREATE TABLE IF NOT EXISTS UploadFiles(" +
                "FileID INT NOT NULL AUTO_INCREMENT," +
                "FileName VARCHAR(255) NOT NULL," +
                "FileSHA256 VARCHAR(255) NOT NULL," +
                "FilePath VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (FileID)" +
                ")";
        jdbcTemplate.execute(sql);
        return 0;
    }

}
