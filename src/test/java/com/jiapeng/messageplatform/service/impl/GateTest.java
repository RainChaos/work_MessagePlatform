package com.jiapeng.messageplatform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiapeng.messageplatform.controller.ApiController;
import com.jiapeng.messageplatform.gate.code.GateRequest;
import com.jiapeng.messageplatform.gate.code.GateResponse;
import com.jiapeng.messageplatform.gate.entity.Device;
import com.jiapeng.messageplatform.gate.entity.Personnel;
import com.jiapeng.messageplatform.utils.FileHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by HZL on 2019/12/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GateTest {
    @Autowired
    GateRequest gateRequest;
    @Autowired
    GateResponse gateResponse;

    private Logger logger = LoggerFactory.getLogger(GateTest.class);

    @Test
    public void testLog() {
        logger.trace("这是 info 级别");
        logger.debug("这是 debug 级别");
        logger.info("这是 info 级别");
        logger.warn("这是 warn 级别");
        logger.error("这是 error 级别");
    }

    @Test
    public void deviceInsert() {
        try {
            Device device = new Device();
            device.setDevice_AppID("mjcc70044f73242483");
            device.setDevice_No("da0130b56ce4a001");
            device.setDevice_Name("gljp考勤机");
            device.setDevice_Area("公司大门");
            device.setDevice_Pwd("888888");
            device.setDevice_Enable(0);
            device.setDevice_Volume(30);
            device.setDevice_PictureRatio(80);
            device.setDevice_LightBright(100);
            device.setDevice_LightOpenTime("18:00");
            device.setDevice_LightCloseTime("08:00");
            device.setDevice_ResatrtTime("00:00");
            device.setDevice_RecogThreshold(85);
            device.setDevice_WhiteThreshold(85);
            device.setDevice_RecogInterval(5);
            device.setDevice_DoorInterval(5);
            device.setDevice_DelayAlamValue(60);
            device.setDevice_LivingThreshold(0);
            device.setDevice_BroadcastText("{name}");
            device.setDevice_ShowText("{name}");
            device.setDevice_AdvertTime(5);
            device.setDevice_RecogSpace(0);
            device.setDevice_Type(1);
            device.setDevice_OutInType(0);
            device.setDevice_Blacklist(1);
            device.setDevice_LightMode(0);
            device.setDevice_RecogMode("{\"FRC\":true,\"PEC\":false,\"PEC_WHITE\":false,\"IC\":false,\"IC_FRC\":false}");
            device.setDevice_OpenBroadcast(0);
            device.setDevice_DoorMode(0);
            device.setDevice_OpenAdvert(0);
            device.setDevice_DoorWiegandType(1);
            device.setDevice_DoorWiegand(26);
            device.setDevice_ResatrtDay(0);
            device.setDevice_IsLiving(0);
            device.setDevice_RelayStatus(1);
            device.setDevice_AutoOpenDoor(1);
            device.setDevice_AutoRecord(0);
            device.setDevice_voiceSetting(0);
            device.setDevice_resultSetting(0);
            device.setDevice_Stranger(0);

            GateResponse response = gateRequest.insertDevice(device);
            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void personnelInsert() {
        try {
            Personnel personnel = new Personnel();
            personnel.setPersonnel_Name("王伟");
            personnel.setPersonnel_IDCard("123456789123456721");
            personnel.setPersonnel_No("0021");
            personnel.setPersonnel_GroupName("123");
            GateResponse response = gateRequest.insertPersonnel(personnel);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPersonnel() {
        try {
            Personnel personnel = gateRequest.getPersonnel("4B97DE8899E26E2B");
            System.out.println(personnel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPersonnelList() {
        try {
            List<Personnel> list = gateRequest.getPersonnelList("mj43dcee8e3cfae1e5");
            for (Personnel p : list) {
                System.out.println(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void deletePersonnel() {
        try {
            GateResponse response = gateRequest.deletePersonnel("0002");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void authPersonnel() {
        try {
            //      GateResponse response = gateRequest.authPersonnel("0001","da0130b56ce4a001");
            GateResponse response = gateRequest.authPersonnel("4B97DE8899E26E2B", "da0130b56ce4a001");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delAuthPersonnel() {
        try {
            //      GateResponse response = gateRequest.authPersonnel("0001","da0130b56ce4a001");
            GateResponse response = gateRequest.deleteAuthPersonnel("4B97DE8899E26E2B", "da0130b56ce4a001");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询人员授权
    @Test
    public void selectAuthPersonnel() {
        try {
            //      GateResponse response = gateRequest.authPersonnel("0001","da0130b56ce4a001");
            List<String> list = new ArrayList<>();
            list.add("0001");
            list.add("4B97DE8899E26E2B");
            GateResponse response = gateRequest.getAuthPersonnel(null, "da0130b56ce4a001");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void listTest() {
        try {
            List<Personnel> list = new ArrayList<>();
            Personnel p1 = new Personnel();
            p1.setPersonnel_GroupName("22");
            list.add(p1);
            Personnel p2 = new Personnel();
            p2.setPersonnel_GroupName("666");
            list.add(p2);
            System.out.println(JSON.toJSONString(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void imgUploadPersonnel() {
        try {
            InputStream inputStream = ApiController.class.getResourceAsStream("/static/image/header/facetemplate0.jpg");
            String base64Str = URLEncoder.encode(FileHelper.getBase64FromInputStream(inputStream), "UTF-8");
            GateResponse response = gateRequest.imgUploadPersonnel("0001", base64Str);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testP() {
        try {
            Personnel per;
            List<Personnel> list = new ArrayList<>();

            for (int i = 0; i < 300; i++) {
                per = new Personnel();
                per.setPersonnel_Name(String.valueOf(i));
                list.add(per);
            }
            Collections.synchronizedList(list);
            testThread thread = new testThread(list);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class testThread extends Thread {
        private List<Personnel> list;

        public testThread(List<Personnel> list) {
            this.list = list;
        }

        @Override
        public void run() {
            list.parallelStream().forEach(p -> {
                System.out.println(p.getPersonnel_Name());
            });
            System.out.println("sssss:" + list.size());
        }
    }
}
