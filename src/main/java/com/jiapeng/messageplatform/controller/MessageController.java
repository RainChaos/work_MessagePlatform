package com.jiapeng.messageplatform.controller;

import com.graphbuilder.math.TermNode;
import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.entity.Teacher;
import com.jiapeng.messageplatform.service.MessageService;
import com.jiapeng.messageplatform.service.TeacherService;
import com.jiapeng.messageplatform.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by HZL on 2019/8/9.
 */
@RestController
@RequestMapping("/message")
public class MessageController{
    @Autowired
    MessageService messageService;
    @Autowired
    TeacherService teacherService;

    @PostMapping("/checkWord")
    public ReturnT<Object> checkWord(HttpServletRequest request,String content) throws Exception {
        SensitiveWord sw = new SensitiveWord("CensorWords.txt");
        sw.InitializationWork();
        return new ReturnT<>(200,content,"敏感词汇");
    }

    /**
     * 消息提交
     *
     * @param request
     * @param msgType   值为“手机短信”、“微信消息”两种
     * @param content   发送内容
     * @param sendDate  发送时间，如果勾选“定时发送”，则这里传的是定时发送选择的时间，否则默认传当前时间
     * @param clCodeStr 勾选要发送班级代码的集合，格式“x001,x002”
     * @return
     * @throws Exception
     */
    @LoginLimit(type = "teacher")
    @PostMapping("/send")
    public ReturnT<Object> sendMsg(HttpServletRequest request, String msgType, String content, String sendDate, String clCodeStr) throws Exception {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        List<String> clCodeList = new ArrayList<>();
        String[] clCodeArr = clCodeStr.split(",");
        Collections.addAll(clCodeList, clCodeArr);
        messageService.sendMsg(teNo, msgType, content, VeDate.strToDateLong(sendDate), null, clCodeList);
        return new ReturnT<>();
    }

    @LoginLimit(type = "teacher")
    @PostMapping("/msgSendByOne")
    public ReturnT<Object> msgSendByOne(HttpServletRequest request, String msgType, String content, String sendDate, String stuIdStr) throws Exception {
        String teNo = SessionUtil.getLoginTeacherNo(request);

        String[] stuIdS = stuIdStr.split(",");
        List<String> stuIdsList = new ArrayList<String>();

        for (int i=0; i<stuIdS.length; i++) {
            stuIdsList.add(stuIdS[i]);
        }

        messageService.sendMsgByOne(teNo, msgType, content, VeDate.strToDateLong(sendDate), null, stuIdsList);
        return new ReturnT<>();
    }

    /**
     * 信息删除
     *
     * @param id
     * @return
     */
    @PostMapping("/del")
    public ReturnT<Object> del(Integer id) {
        messageService.del(id);
        return new ReturnT<>();
    }


    /**
     * 获取发送消息列表（供管理端用）
     *
     * @param page
     * @param limit
     * @param msgType
     * @param state
     * @return
     */
    @PostMapping("/list")
    public ReturnT<Object> getList(HttpServletRequest request,Integer page, Integer limit, String msgType, String state) {

        String scCode = request.getSession().getAttribute("scCode").toString();

        List<String> teNoList = new ArrayList<>();
        if(scCode != null && scCode.length() != 0){
            //获取该学校的所有老师编号
            PageResult list = teacherService.getTeNoListByScCode(0,100000,scCode,null);
            List<Teacher> teacherList = list.getData();
            for (int i = 0; i < teacherList.size();i++){
                teNoList.add(teacherList.get(i).getTeNo());
            }
        }

        PageResult pageResult = messageService.messagePageList(page, limit, teNoList, msgType, state);
        return new ReturnT<>(pageResult);
    }



    /**
     * 获取消息明细列表（供管理端用）
     *
     * @param page
     * @param limit
     * @param logId
     * @param state
     * @return
     */
    @PostMapping("/detailList")
    public ReturnT<Object> getDetailListList(Integer page, Integer limit, Integer logId, String state) {
        PageResult pageResult = messageService.messageDetailPageList(limit, page, logId, state);
        return new ReturnT<>(pageResult);
    }

    /**
     * 获取消息详情内容
     *
     * @param id
     * @return
     */
    @GetMapping("/getMessageContent")
    public ReturnT<Object> getMessageContent(Integer id) {
        String msgContent = messageService.getMessageContent(id);
        return new ReturnT<>(msgContent);
    }
}
