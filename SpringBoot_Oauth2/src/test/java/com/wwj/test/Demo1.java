package com.wwj.test;

import com.xiaoleilu.hutool.json.JSONUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:my-process.xml"})
public class Demo1 {
    private Logger logger = LoggerFactory.getLogger(Demo1.class);

    @Test
    public void demo1(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        logger.info(JSONUtil.toJsonStr(processEngine));
    }

    public static void main(String[] args) throws Exception, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost("http://localhost:8090/activiti-rest/service/login");

        StringEntity input = new StringEntity("{\"userId\":\"Kermit\",\"password\":\"Kermit\"}");
        input.setContentType("application/json");
        postRequest.setEntity(input);

        HttpResponse response = httpClient.execute(postRequest);

        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        httpClient.getConnectionManager().shutdown();
    }
}
