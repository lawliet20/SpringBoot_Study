package com.wwj.test;

import com.wwj.BootMainApp;
import com.wwj.rest.HelloResource;
import com.wwj.service.UserService;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sherry on 2017/8/31.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootMainApp.class)
//配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HelloControllerTest {

    @Autowired
    private HelloResource helloResource;
    @Autowired
    private UserService userService;
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    private MockMvc weChatMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(helloResource, "daoTestService", userService);
        this.weChatMockMvc = MockMvcBuilders.standaloneSetup(helloResource)
//                .setCustomArgumentResolvers(pageableArgumentResolver)
//                .setControllerAdvice(exceptionTranslator)
//                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    public void helloTest() throws Exception {
        weChatMockMvc.perform(get("/hello/test/mybatis")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.lastName").value("Administrator"));
    }

}
