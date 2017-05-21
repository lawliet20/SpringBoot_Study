package com.wwj.controller;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author wwj
 * @version 1.0.0
 * @blog http://blog.didispace.com
 *
 */
@RestController
@RequestMapping("/mail")
public class MailController {
    @Resource
    JavaMailSender mailSender;

    @RequestMapping("/sendMail")
    public void index() {
        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("lawliet_w@126.com");
            message.setTo("1021679969@qq.com");
            message.setSubject("测试邮件主题");
            message.setText("测试邮件内容");
            this.mailSender.send(mimeMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}