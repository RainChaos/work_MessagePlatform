package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.SchoolWXMapper;
import com.jiapeng.messageplatform.entity.SchoolWX;
import com.jiapeng.messageplatform.service.WxService;
import com.jiapeng.messageplatform.utils.Constants;
import com.jiapeng.messageplatform.utils.VeDate;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by HZL on 2019/8/13.
 */
@Service
public class WxServiceImpl implements WxService {
    @Autowired
    SchoolWXMapper schoolWXMapper;

    @Value("${config.wx.appid}")
    private String appId;
    @Value("${config.wx.appsecret}")
    private String appSecret;
    @Value("${config.wx.localdomainname}")
    private String domainName;

    @Override
    public String getAuthorUrl(String redirectUrl, String state) {
        String authorizeUrl = Constants.GET_AUTHORIZE_URL;
        authorizeUrl = authorizeUrl.replace("[appid]", appId);
        authorizeUrl = authorizeUrl.replace("[redirect_uri]", redirectUrl);
        authorizeUrl = authorizeUrl.replace("[!state]", state);
        return authorizeUrl;
    }

    @Override
    public WxMpService getWxMpService(String scCode) throws Exception {
        SchoolWX schoolWX = schoolWXMapper.selectByPrimaryKey(scCode);
        if (null == schoolWX) {
            throw new Exception("微信配置未配置");
        }
        String token = schoolWX.getToken();
        String appId = schoolWX.getAppId();
        String appSecret = schoolWX.getAppSecret();
        Date expireTime = schoolWX.getExpiresTime();
        Date nowDate = new Date();

        WxMpInMemoryConfigStorage wxConfigProvider = new WxMpInMemoryConfigStorage();
        wxConfigProvider.setAppId(appId);
        wxConfigProvider.setSecret(appSecret);

        WxMpService wxMpService = new WxMpServiceImpl();

        //获取token并保存到数据库
        if (StringUtils.isBlank(token) || nowDate.compareTo(expireTime) == 1) {

            expireTime = VeDate.getAddHour(nowDate, 2);

            wxMpService.setWxMpConfigStorage(wxConfigProvider);
            token = wxMpService.getAccessToken();

            schoolWX.setToken(token);
            schoolWX.setExpiresTime(expireTime);
            schoolWXMapper.updateByPrimaryKeySelective(schoolWX);
        } else {
            wxConfigProvider.setToken(token);
            wxConfigProvider.setExpiresTime(expireTime.getTime());
            wxMpService.setWxMpConfigStorage(wxConfigProvider);
        }
        return wxMpService;
    }
}
