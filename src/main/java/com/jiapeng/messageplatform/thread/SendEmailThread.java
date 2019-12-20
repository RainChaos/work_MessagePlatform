package com.jiapeng.messageplatform.thread;

import com.jiapeng.messageplatform.service.EmailService;

import javax.annotation.Resource;

/**
 * Created by HZL on 2019/5/28.
 */

public class SendEmailThread extends Thread {
    @Resource
    EmailService emailService;

    String receiveAddr;
    String title;
    String content;

    public SendEmailThread(String receiveAddr, String title, String content) {
        this.receiveAddr = receiveAddr;
        this.title = title;
        this.content = content;
    }

    @Override
    public void run() {
        try {
            emailService.send(receiveAddr, title, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
