package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.utils.PageResult;
import webServices.MTReport;
import webServices.MTResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by HZL on 2019/8/5.
 */
public interface MessageService {

    void sendMsg(String teNo, String msgType, String content, Date sendDate, String state, List<String> clCodeList) throws Exception;

    //自定义发送
    void sendMsgByOne(String teNo, String msgType, String content, Date sendDate, String state, List<String> stuIdsList) throws Exception;

    void del(int id);

    PageResult messagePageList(int pageIndex, int pageSize,  List<String> teNoList, String msgType, String state);

    PageResult messageDetailPageList(int limit, int page, int logId, String state);



    String getMessageContent(Integer id);

    /**
     * 门禁消息通知接口 by hzl 2019-8-24 16:27:43
     * @param scCode 学校代码
     * @param stuNo 学生编号
     * @param leaveId 请假id
     * @param imgName 图片名称
     * @param deviceNo 闸机设备号
     * @param noticeWay 通知方式
     * @param ascType 门禁类型
     * @param actionType 动作类型
     * @param actionTime 动作时间
     * @param ascRemark 门禁备注
     * @throws Exception
     */
    void ascNoticeSendInterface(String scCode, String stuNo, String leaveId, String imgName, String deviceNo, Integer ascType, Integer actionType, String actionTime, String ascRemark) throws Exception;

    //根据电信短信Response接口返回消息写入到数据库 by hzl 2019-8-31 09:32:31
    void mbMsgUpdateStateByRes(List<MTResponse> list) throws Exception;

    //根据电信短信Report接口返回消息写入到数据库 by hzl 2019-8-31 09:32:31
    void mbMsgUpdateStateByRpt(List<MTReport> list) throws Exception;

}
