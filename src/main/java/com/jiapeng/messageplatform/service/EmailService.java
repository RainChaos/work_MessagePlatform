package com.jiapeng.messageplatform.service;

/**
 * Created by HZL on 2019/5/23.
 */
public interface EmailService {
    void send(String receiveAddr,String title,String content) throws Exception;
}
