package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.TeacherWxInfo;

import java.util.List;

/**
 * Created by HZL on 2019/9/9.
 */
public interface TeacherWxInfoService {
    //教师微信绑定  by hzl 2019-9-9 15:10:58
    void wxBind(String clCodes, String teNo, String openId, String phoneNo) throws Exception;

    //教师微信绑定  by hzl 2019-9-9 15:28:51
    void cancelWxBind(Integer id);

    //根据学生stuId获取对应教师绑定的通知号码信息 by hzl 2019-9-9 17:32:12
    List<TeacherWxInfo> getListByStuId(String stuId);
}
