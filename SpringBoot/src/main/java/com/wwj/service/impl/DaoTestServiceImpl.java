package com.wwj.service.impl;

import com.wwj.dao.UserMapper;
import com.wwj.domain.User;
import com.wwj.service.DaoTestService;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//        PageHelper.startPage(1,2);
//        List<User> list2 = userMapper.selectUser2();
//        System.out.println("查询数据库用户2-->");
//        System.out.println(JSONUtils.toJSONString(list2));
        List<User> list = userMapper.selectUser();
        System.out.println("查询数据库用户-->");
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Transactional
    @Override
    public void testTransaction(){
        userMapper.deleteByPrimaryKey(3);
        throw new RuntimeException("运行时异常...");
    }
}
