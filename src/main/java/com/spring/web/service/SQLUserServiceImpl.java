package com.spring.web.service;

import com.spring.web.entity.SQLUser;
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
}
