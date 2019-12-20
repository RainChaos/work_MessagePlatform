package com.jiapeng.messageplatform.service;

import me.chanjar.weixin.mp.api.WxMpService;

/**
 * Created by HZL on 2019/8/13.
 */
public interface WxService {
    /**
     * 获取授权URL
     * @param redirectUrl
     * @return
     */
    String getAuthorUrl(String redirectUrl,String state);

    //根据获取scCode获取学校微信配置 by hzl 2019-9-6 21:06:28
    WxMpService getWxMpService(String scCode) throws Exception;
}
