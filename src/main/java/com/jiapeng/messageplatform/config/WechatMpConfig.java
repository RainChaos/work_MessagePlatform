package com.jiapeng.messageplatform.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by HZL on 2019/8/13.
 */
@Component
public class WechatMpConfig {
    @Value("${config.wx.appid}")
    private String appId;
    @Value("${config.wx.appsecret}")
    private String appSecret;

    @Bean
    public WxMpService wxMpService() {
        //创建WxMpService实例并设置appid和sectret
        WxMpService wxMpService = new WxMpServiceImpl();
        //这里的设置方式是跟着这个sdk的文档写的
        wxMpService.setWxMpConfigStorage(wxConfigProvider());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxConfigProvider() {
        WxMpInMemoryConfigStorage wxConfigProvider = new WxMpInMemoryConfigStorage();
        wxConfigProvider.setAppId(appId);
        wxConfigProvider.setSecret(appSecret);
        return wxConfigProvider;
    }

}
