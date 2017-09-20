package com.wwj.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.wwj.dao.UserMapper;
import com.wwj.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sherry on 2017/4/19.
 */
@Service("daoTestService")
@Transactional
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    public List<User> getAllUser() {
//        PageHelper.startPage(1,2);
        List<User> list2 = userMapper.selectUser2();
        logger.debug("查询数据库用户2{}",JSONUtils.toJSONString(list2));
        List<User> list = userMapper.selectUser();
        logger.debug("查询数据库用户2{}",JSONUtils.toJSONString(list2));
        return list2;

    }

    public void delWithException(){
        userMapper.deleteByPrimaryKey(3);
        throw new RuntimeException("运行时异常...");
    }
}
