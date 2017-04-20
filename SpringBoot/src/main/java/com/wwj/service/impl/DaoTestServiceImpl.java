package com.wwj.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.wwj.domain.User;
import com.wwj.dao.UserMapper;
import com.wwj.service.DaoTestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sherry on 2017/4/19.
 */
@Service("daoTestService")
public class DaoTestServiceImpl implements DaoTestService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void testSelect() {
        List<User> list = userMapper.selectUser();
        System.out.println("查询数据库用户-->");
        System.out.println(JSONUtils.toJSONString(list));
    }
}
