package com.wwj.config;

import com.wwj.model.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 通过FeignClient请求Popservice的公共服务类
 * 此工具类默认带FeignClientConfiguration中的TOKEN验证
 * Created by zhangjinye on 2017/5/20.
 */
@FeignClient(name = "popservice",
        configuration = FeignClientConfiguration.class
        , fallback =  PopServiceRest.PopServiceRestFallback.class)
public interface UserFeignClient {
    /**
     * 两个坑
     * 1、不支持GetMapping
     * 2、@PathVariable必须指定参数名
     */
    @RequestMapping(value = "/simple/{id}", method = RequestMethod.GET)
    public User findById(@PathVariable("id") Long id);

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User postUser(@RequestBody User user);

    //该请求不会成功, 只要参数是复杂对象，即使指定了是Get方法，feign依然会以POST方法进行发送请求
    //可以将对象参数，拆成单个参数如：string id，string username ...
    @RequestMapping(value = "/get-user", method = RequestMethod.GET)
    public User getUser(User user);

    //降级处理
    @Component
    public class PopServiceRestFallback implements UserFeignClient {

        @Override
        public User findById(Long id) {
            return null;
        }

        @Override
        public User postUser(User user) {
            return null;
        }

        @Override
        public User getUser(User user) {
            return null;
        }
    }
}