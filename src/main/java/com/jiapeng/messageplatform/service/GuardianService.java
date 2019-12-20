package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.Guardian;
import com.jiapeng.messageplatform.entity.WxBandEntity;

import java.util.List;

/**
 * Created by HZL on 2019/8/15.
 */
public interface GuardianService {
    //根据openid查找家长记录
    List<WxBandEntity> getByOpenId(String openId);

    //取消微信绑定
    void cancelWxBand(String wxNo, Integer guaId);

    //获取家长手机号码
    String getPhoneNo(String scCode,String stuNo,String stuName,String guaName);

    //家长微信绑定
    void wxBand(String scCode,String stuNo, String stuName, String guaName, String phone, String openId) throws Exception;

}
