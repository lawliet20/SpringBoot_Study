package com.wwj.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.wwj.model.User;
import com.wwj.service.RemoteService;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sherry on 2017/4/16.
 */
@RestController
public class RemoteController {
    @Resource
    private RemoteService remoteService;

    /**
     * 调用远程微服务接口（远程服务接口不带权限控制）
     * */
    @RequestMapping("/test/restTemplateReq")
    public void testRestRequest() throws Exception {
        List<User> list = remoteService.requestUserList();
        System.out.println(JSONUtils.toJSONString("####:"+ JSONUtil.toJsonStr(list)));
    }

    /**
     * 远程微服务调用此接口（此接口带权限控制）
     * */
    @RequestMapping("/test/restTemplateRes")
    public ResponseEntity<List<User>> testRestTemplateRes(){
        System.out.println("springBoot_service->testRestTemplateRes");
        List<User> list = remoteService.responseUserList();
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }

}
