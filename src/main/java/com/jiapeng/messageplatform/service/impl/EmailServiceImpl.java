package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.service.EmailService;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by HZL on 2019/5/23.
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${config.email.hostname}")
    private String hostName;
    @Value("${config.email.username}")
    private String emailUserName;
    @Value("${config.email.addr}")
    private String emailAddr;
    @Value("${config.email.code}")
    private String emailCode;

    @Override
    public void send(String receiveAddr, String title, String content) throws Exception {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(hostName);
        email.setCharset("utf-8");//设置发送的字符类型
        email.addTo(receiveAddr);//设置收件人
        email.setFrom(emailAddr, emailUserName);//发送人的邮箱为自己的，用户名可以随便填
        email.setAuthentication(emailAddr, emailCode);//设置发送人到的邮箱和用户名和授权码(授权码是自己设置的)
        email.setSubject(title);//设置发送主题
        email.setMsg(content);//设置发送内容
        email.send();//进行发送
    }
}
