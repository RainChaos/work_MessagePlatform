package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.service.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by HZL on 2019/5/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailTest {

    @Autowired
    EmailService emailService;

    @Test
    public void sendEmail() {
        try {
            emailService.send("949767088@qq.com", "密码找回", "这是一个测试链接");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
