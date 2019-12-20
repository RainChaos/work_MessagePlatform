package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.GuardianMapper;
import com.jiapeng.messageplatform.dao.StudentMapper;
import com.jiapeng.messageplatform.dao.TeacherMapper;
import com.jiapeng.messageplatform.entity.Guardian;
import com.jiapeng.messageplatform.entity.Leave;
import com.jiapeng.messageplatform.entity.MessageLog;
import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.model.MsgTargetInfo;
import com.jiapeng.messageplatform.model.UnitTree;
import com.jiapeng.messageplatform.service.LeaveService;
import com.jiapeng.messageplatform.service.MbMsgService;
import com.jiapeng.messageplatform.service.StudentService;
import com.jiapeng.messageplatform.utils.VeDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import webServices.AccountInfo;
import webServices.GsmsResponse;
import webServices.MTReport;
import webServices.MTResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by HZL on 2019/8/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MbMsgTest {
    @Autowired
    MbMsgService mbMsgService;

    @Test
    public void getAccount() {
        try {
            AccountInfo accountInfo = mbMsgService.getAccountInfo();
            System.out.println("AccountInfo:" + accountInfo);
            System.out.println("name:" + accountInfo.getName());
            System.out.println("account:" + accountInfo.getAccount());
            System.out.println("identify:" + accountInfo.getIdentify());
            System.out.println("reserve:" + accountInfo.getReserve());
            System.out.println("balance:" + accountInfo.getBalance());
            System.out.println("userBrief:" + accountInfo.getUserbrief());
            System.out.println("bizNames:" + accountInfo.getBizNames().getString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void doPost() {
        try {
            List<MsgTargetInfo> targetList = new ArrayList<>();
            MsgTargetInfo info = new MsgTargetInfo(1, "18507739912");
            targetList.add(info);
            String uuid = UUID.randomUUID().toString();
            System.out.println("UUID: " + uuid);
            GsmsResponse resp = mbMsgService.doPostSMS(uuid, "测试短信", targetList,"norm");
            System.out.println("result:" + resp.getResult());
            System.out.println("message:" + resp.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getResponse() {
        try {
            List<MTResponse> resps = mbMsgService.getResponse(200);
            if (resps != null && resps.size() > 0) {
                for (int i = 0, size = resps.size(); i < size; i++) {
                    MTResponse resp = resps.get(i);
                    System.out.println("MTResponse-" + i + "-:" + resp);
                    System.out.println("batchId:" + resp.getBatchID());
                    System.out.println("customMsgId:" + resp.getCustomMsgID());
                    System.out.println("id:" + resp.getId());
                    System.out.println("msgId:" + resp.getMsgID());
                    System.out.println("phone:" + resp.getPhone());
                    System.out.println("state:" + resp.getState());
                    System.out.println("originResult:" + resp.getOriginResult());
                    System.out.println("reserve:" + resp.getReserve());
                    System.out.println("number:" + resp.getNumber());
                    System.out.println("total:" + resp.getTotal());
                    System.out.println("submitRespTime:" + resp.getSubmitRespTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getReport() {
        try {
            List<MTReport> resps = mbMsgService.getReport(200);
            if (resps != null && resps.size() > 0) {
                for (int i = 0, size = resps.size(); i < size; i++) {
                    MTReport resp = resps.get(i);
                    System.out.println("MTResponse-" + i + "-:" + resp);
                    System.out.println("batchId:" + resp.getBatchID());
                    System.out.println("customMsgId:" + resp.getCustomMsgID());
                    System.out.println("id:" + resp.getId());
                    System.out.println("msgId:" + resp.getMsgID());
                    System.out.println("phone:" + resp.getPhone());
                    System.out.println("state:" + resp.getState());
                    System.out.println("originResult:" + resp.getOriginResult());
                    System.out.println("reserve:" + resp.getReserve());
                    System.out.println("number:" + resp.getNumber());
                    System.out.println("total:" + resp.getTotal());
                    System.out.println("submitTime:" + resp.getSubmitTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findResponse() {
        try {
            List<MTResponse> resps = mbMsgService.findResponse("9e99ac47-b4f5-4f4d-aade-9b0f715d5f50", null, 15, 1);
            if (resps != null && resps.size() > 0) {
                for (int i = 0, size = resps.size(); i < size; i++) {
                    MTResponse resp = resps.get(i);
                    System.out.println("MTResponse-" + i + "-:" + resp);
                    System.out.println("batchId:" + resp.getBatchID());
                    System.out.println("customMsgId:" + resp.getCustomMsgID());
                    System.out.println("id:" + resp.getId());
                    System.out.println("msgId:" + resp.getMsgID());
                    System.out.println("phone:" + resp.getPhone());
                    System.out.println("state:" + resp.getState());
                    System.out.println("originResult:" + resp.getOriginResult());
                    System.out.println("reserve:" + resp.getReserve());
                    System.out.println("number:" + resp.getNumber());
                    System.out.println("total:" + resp.getTotal());
                    System.out.println("submitRespTime:" + resp.getSubmitRespTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findReport() {
        try {
            List<MTReport> resps = mbMsgService.findReport("4560ff4a-cb52-4d9a-94df-8a994cc2f4710", null, 15, 1);
            if (resps != null && resps.size() > 0) {
                for (int i = 0, size = resps.size(); i < size; i++) {
                    MTReport resp = resps.get(i);
                    System.out.println("MTResponse-" + i + "-:" + resp);
                    System.out.println("batchId:" + resp.getBatchID());
                    System.out.println("customMsgId:" + resp.getCustomMsgID());
                    System.out.println("id:" + resp.getId());
                    System.out.println("msgId:" + resp.getMsgID());
                    System.out.println("phone:" + resp.getPhone());
                    System.out.println("state:" + resp.getState());
                    System.out.println("originResult:" + resp.getOriginResult());
                    System.out.println("reserve:" + resp.getReserve());
                    System.out.println("number:" + resp.getNumber());
                    System.out.println("total:" + resp.getTotal());
                    System.out.println("submitTime:" + resp.getSubmitTime());
                }
            }
        } catch (Exception e) {

        }
    }
}
