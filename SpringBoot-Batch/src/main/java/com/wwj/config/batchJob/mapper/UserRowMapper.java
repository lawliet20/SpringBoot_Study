package com.wwj.config.batchJob.mapper;

import com.wwj.config.batchJob.domain.User;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    private static final Logger log = LoggerFactory.getLogger(UserRowMapper.class);

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setAge(rs.getString("age"));
        user.setBalance(rs.getString("balance"));
        log.info("user数据读入转型:{}", JSONUtil.toJsonPrettyStr(user));

        return user;
    }

}
