package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.entity.Guardian;
import com.jiapeng.messageplatform.entity.WxBandEntity;
import com.jiapeng.messageplatform.service.GuadianWxLogService;
import com.jiapeng.messageplatform.service.GuardianService;
import com.jiapeng.messageplatform.utils.PageResult;
import com.jiapeng.messageplatform.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by HZL on 2019/8/15.
 */
@RestController
@RequestMapping("/guardian")
public class GuardianController {
    @Autowired
    GuardianService guardianService;

    @Autowired
    GuadianWxLogService guadianWxLogService;

    /**
     * 微信取消绑定按钮
     * @param openId
     * @return
     */
//    @PostMapping("/cancelWxBand")
//    public ReturnT<Object> cancelWxBand(String openId, Integer guaId) {
//        guardianService.cancelWxBand(openId, guaId);
//        return new ReturnT<>("取消成功");
//    }


    /**
     * 根据微信号获取用户信息
     *
     * @param openId
     * @return
     */
    @PostMapping("/getGuardian")
    public ReturnT<Object> getWxBand(String openId) {
        List<WxBandEntity> guardian = guardianService.getByOpenId(openId);
        return new ReturnT<>(guardian);
    }

    /**
     * 获取家长手机号码
     * @param scCode  学校代码
     * @param stuNo 学生编号
     * @param stuName 学生姓名
     * @param guaName 家长姓名
     * @return
     */
    @PostMapping("/getGuardianPhone")
    public ReturnT<Object> getGuardianPhone( String scCode,String stuNo,String stuName,String guaName) {
        String phoneNo = guardianService.getPhoneNo(scCode,stuNo, stuName, guaName);
        return new ReturnT<>(phoneNo);
    }

    /**
     * 家长微信绑定学生信息
     *
     * @param stuName
     * @param stuNo
     * @param guaName
     * @param phone
     * @param openId
     * @return
     * @throws Exception
     */
    @PostMapping("/wxBand")
    public ReturnT<Object> wxBand(String scCode, String stuNo, String stuName, String guaName, String phone, String openId) throws Exception {
        guardianService.wxBand(scCode,stuNo,stuName, guaName, phone, openId);
        return new ReturnT<>("绑定成功");
    }

    @PostMapping("/operate")
    public ReturnT<Object> operate(int page, int limit, String scCode, String stuNo,String stuName,  String startDate, String endDate) throws Exception {
        PageResult pageResult = guadianWxLogService.getRecord(page,limit,scCode,stuNo,stuName,startDate,endDate);
        return new ReturnT<>(pageResult);
    }
}
