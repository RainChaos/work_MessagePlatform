package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.model.MsgTargetInfo;
import com.jiapeng.messageplatform.service.MbMsgService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import webServices.*;

import java.util.List;

/**
 * Created by HZL on 2019/8/31.
 */
@Service
public class MbMsgServiceImpl implements MbMsgService {
    //短信接口账号
    @Value("${config.mbmsg.account}")
    public String account;
    //短信接口密码
    @Value("${config.mbmsg.password}")
    public String password;

    WebService service = new WebService();
    WebServiceSoap web = service.getWebServiceSoap();

    @Override
    public GsmsResponse doPostSMS(String batchId, String msgContent, List<MsgTargetInfo> targetList, String contentType) {
        GsmsResponse resp = null;
        MTPacks pack = new MTPacks();
        ArrayOfMessageData messageDatas = new ArrayOfMessageData();
        MessageData messageData = null;
        for (MsgTargetInfo targetInfo : targetList) {
            messageData = new MessageData();
            if (contentType.equals("asc")) {
                String[] msgContentArr = msgContent.split("@@");
                if (targetInfo.getTeacher()) {
                    messageData.setContent(msgContentArr[1]);
                } else {
                    messageData.setContent(msgContentArr[0]);
                }

            } else {
                messageData.setContent(msgContent); // 短信内容

            }
            String phone = targetInfo.getNo();
            messageData.setPhone(phone);      // 短信号码
            messageData.setCustomMsgID(contentType + "-" + targetInfo.getDetailId().toString()); // 每条短信,一个扩展码,使用uuid
            messageDatas.getMessageData().add(messageData);

        }

        pack.setMsgType(1);// 1短信发送 2彩信发送
        pack.setSendType(1);// 0群发 1 组发
        pack.setMsgs(messageDatas);
        pack.setDistinctFlag(false); // 是否过滤重复号码
        pack.setBatchID(batchId);

        //获取提交结果
        resp = web.post(account, password, pack);
        return resp;
    }

    @Override
    public List<MTResponse> getResponse(int pagesize) {
        ArrayOfMTResponse resps = web.getResponse(account, password, pagesize);
        return resps.getMTResponse();
    }

    @Override
    public List<MTReport> getReport(int pagesize) {
        ArrayOfMTReport reports = service.getWebServiceSoap().getReport(account, password, pagesize);
        return reports.getMTReport();
    }

    @Override
    public List<MTResponse> findResponse(String batchId, String mobile, int pageIndex, int flag) {
        ArrayOfMTResponse resps = web.findResponse(account, password, batchId, mobile, pageIndex, flag);
        return resps.getMTResponse();
    }

    @Override
    public List<MTReport> findReport(String batchId, String mobile, int pageIndex, int flag) {
        ArrayOfMTReport resps = web.findReport(account, password, batchId, mobile, pageIndex, flag);
        return resps.getMTReport();
    }

    @Override
    public AccountInfo getAccountInfo() {
        AccountInfo accountInfo = service.getWebServiceSoap().getAccountInfo(account, password);
        return accountInfo;
    }

}
