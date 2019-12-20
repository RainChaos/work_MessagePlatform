package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.model.MsgTargetInfo;
import webServices.AccountInfo;
import webServices.GsmsResponse;
import webServices.MTReport;
import webServices.MTResponse;

import java.util.List;

/**
 * 短信接口封装类
 * Created by HZL on 2019/8/31.
 */
public interface MbMsgService {
    // SMS POST 短信提交

    /**
     * 电信短信提交接口
     * by hzl 2019-8-29 09:52:32
     *
     * @param batchId
     * @param msgContent 短信内容
     * @param targetList 发送目标
     * @param contentType 消息内容类型 "norm"为普通消息、“asc”为门禁消息
     * @return
     */
    GsmsResponse doPostSMS(String batchId, String msgContent, List<MsgTargetInfo> targetList,String contentType);

    //Get Response提交响应获取
    List<MTResponse> getResponse(int pagesize);

    //Get Report报告状态获取
    List<MTReport> getReport(int pagesize);

    //查找 Response提交响应获取
    List<MTResponse> findResponse(String batchId, String mobile, int pageIndex, int flag);

    //查找 Report报告状态获取
    List<MTReport> findReport(String batchId, String mobile, int pageIndex, int flag);

    //获取短信接口账户信息
    AccountInfo getAccountInfo();

}
