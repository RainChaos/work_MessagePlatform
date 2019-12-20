package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.*;
import com.jiapeng.messageplatform.entity.*;
import com.jiapeng.messageplatform.enums.AscActionEnum;
import com.jiapeng.messageplatform.enums.AscTypeEnum;
import com.jiapeng.messageplatform.enums.MbMsgResEnum;
import com.jiapeng.messageplatform.enums.MbMsgRptEnum;
import com.jiapeng.messageplatform.model.MessageInfo;
import com.jiapeng.messageplatform.model.MsgTargetInfo;
import com.jiapeng.messageplatform.model.WxStudentInfoTemp;
import com.jiapeng.messageplatform.service.*;
import com.jiapeng.messageplatform.utils.PageResult;
import com.jiapeng.messageplatform.utils.VeDate;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webServices.GsmsResponse;
import webServices.MTReport;
import webServices.MTResponse;

import java.util.*;

/**
 * Created by HZL on 2019/8/5.
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageLogMapper messageLogMapper;
    @Autowired
    MessageDetailMapper messageDetailMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    WxMpService wxMpService;
    @Autowired
    SchoolService schoolService;
    @Autowired
    SchoolMapper schoolMapper;
    @Autowired
    StudentService studentService;
    @Autowired
    AscNoticeLogMapper ascNoticeLogMapper;
    @Autowired
    AscNoticeDetailMapper ascNoticeDetailMapper;
    @Autowired
    LeaveService leaveService;
    @Autowired
    MbMsgService mbMsgService;
    @Autowired
    SchoolWXMapper schoolWXMapper;
    @Autowired
    WxService wxService;
    @Autowired
    TeacherWxInfoService teacherWxInfoService;
    @Autowired
    ConfigMapper configMapper;
    @Autowired
    ConfigService configService;

    //电信短信接口
    @Value("${config.msginterfaceurl}")
    public String msgInterfaceUrl;

    //本项目部署的域名地址
    @Value("${config.wx.localdomainname}")
    public String localDomain;


    @Transactional
    @Override
    public void sendMsg(String teNo, String msgType, String content, Date sendDate, String state, List<String> clCodeList) throws Exception {
        List<Student> stuList = studentMapper.list(-1, 0, clCodeList, null, null,null);
        if (stuList.size() == 0) {
            throw new Exception("发送失败，班级下没有学生数据");
        }

        //1.插入到信息日志表
        MessageLog msgLog = new MessageLog();
        msgLog.setMsgType(msgType);
        msgLog.setMsgContent(content);
        msgLog.setTeNo(teNo);
        msgLog.setSendDate(sendDate);
        msgLog.setCreateDate(new Date());
        msgLog.setState("正在提交");
        msgLog.setBatchId(UUID.randomUUID().toString());
        messageLogMapper.insertSelective(msgLog);

        List<WxStudentInfoTemp> stuSimplList = new ArrayList<>();
        //2.循环班级下的所有学生插入到信息明细表
        stuList.forEach(student -> {
                    //保存学生家长列表的openid集合
                    List<MsgTargetInfo> msgTargetInfoList = new ArrayList<MsgTargetInfo>();
                    //获取学生家长列表
                    List<Guardian> guardianList = student.getGuardianList();
                    for (Guardian guardian : guardianList) {
                        String noticeNo = "";
                        if (msgType.equals("微信消息")) {
                            noticeNo = guardian.getWxNo();
                        } else {
                            noticeNo = guardian.getPhone();
                        }
                        if (StringUtils.isNotBlank(noticeNo)) {
                            MessageDetail detail = new MessageDetail();
                            detail.setLogId(msgLog.getId());
                            detail.setStuId(student.getStuId());
                            detail.setGuaName(guardian.getGuaName());
                            detail.setPhone(noticeNo);
                            messageDetailMapper.insertSelective(detail);

                            //创建DetialIdAndOpenId实体保存消息明细id以及openid
                            MsgTargetInfo detialSimpl = new MsgTargetInfo(detail.getId(), noticeNo);
                            msgTargetInfoList.add(detialSimpl);
                        }
                    }
                    //把信息传入到StudentSimpleInfo
                    WxStudentInfoTemp wxTemp = new WxStudentInfoTemp();
                    wxTemp.setSchoolName(student.getSchoolName());
                    wxTemp.setGradeName(student.getGradeName());
                    wxTemp.setClassName(student.getClassName());
                    wxTemp.setStudentName(student.getStuName());
                    wxTemp.setMsgTargetInfoList(msgTargetInfoList);
                    stuSimplList.add(wxTemp);
                }
        );

        //获取教师姓名
        Teacher teacher = teacherMapper.selectByPrimaryKey(teNo);
        String teName = teacher.getName();
        String scCode = teacher.getScCode();

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setScCode(scCode);//在此传入学校代码用于获取SchoolWX配置信息 by hzl 2019-9-6 16:10:41
        messageInfo.setMsgNo(msgLog.getId().toString());
        messageInfo.setBatchId(msgLog.getBatchId());
        messageInfo.setContent(content);
        messageInfo.setTeName(teName);
        messageInfo.setSendDate(VeDate.dateToStrLong(sendDate));
        if (stuSimplList.size() != 0) {
            messageInfo.setWxStudentInfoTempList(stuSimplList);
        }

        //微信模板消息接口调用
        if (msgType.equals("微信消息")) {
            try {
                WxMsgSendThread wxMsgSendThread = new WxMsgSendThread(messageInfo);
                wxMsgSendThread.start();

                MessageLog dbMsgLog = new MessageLog();
                dbMsgLog.setId(msgLog.getId());
                dbMsgLog.setState("提交成功");
                messageLogMapper.updateByPrimaryKeySelective(dbMsgLog);
            } catch (Exception e) {
                MessageLog dbMsgLog = new MessageLog();
                dbMsgLog.setId(msgLog.getId());
                dbMsgLog.setState("提交失败");
                dbMsgLog.setRemark(e.getMessage());
                messageLogMapper.updateByPrimaryKeySelective(dbMsgLog);
            }
        }
        //电信接口调用
        else {
            MbMsgSendThread mbMsgSendThread = new MbMsgSendThread(messageInfo);
            mbMsgSendThread.start();
  //        mbMsgSendThread.run();
        }
    }


    @Transactional
    @Override
    public void sendMsgByOne(String teNo, String msgType, String content, Date sendDate, String state, List<String> stuIdsList) throws Exception {
        List<Student> stuList = studentMapper.listByStuId(-1, 0, stuIdsList);
        if (stuList.size() == 0) {
            throw new Exception("发送失败，没有找到相应的接收人");
        }

        //1.插入到信息日志表
        MessageLog msgLog = new MessageLog();
        msgLog.setMsgType(msgType);
        msgLog.setMsgContent(content);
        msgLog.setTeNo(teNo);
        msgLog.setSendDate(sendDate);
        msgLog.setCreateDate(new Date());
        msgLog.setState("正在提交");
        msgLog.setBatchId(UUID.randomUUID().toString());
        messageLogMapper.insertSelective(msgLog);

        List<WxStudentInfoTemp> stuSimplList = new ArrayList<>();
        //2.循环班级下的所有学生插入到信息明细表
        stuList.forEach(student -> {
                    //保存学生家长列表的openid集合
                    List<MsgTargetInfo> msgTargetInfoList = new ArrayList<MsgTargetInfo>();
                    //获取学生家长列表
                    List<Guardian> guardianList = student.getGuardianList();
                    for (Guardian guardian : guardianList) {
                        String noticeNo = "";
                        if (msgType.equals("微信消息")) {
                            noticeNo = guardian.getWxNo();
                        } else {
                            noticeNo = guardian.getPhone();
                        }
                        if (StringUtils.isNotBlank(noticeNo)) {
                            MessageDetail detail = new MessageDetail();
                            detail.setLogId(msgLog.getId());
                            detail.setStuId(student.getStuId());
                            detail.setGuaName(guardian.getGuaName());
                            detail.setPhone(noticeNo);
                            messageDetailMapper.insertSelective(detail);

                            //创建DetialIdAndOpenId实体保存消息明细id以及openid
                            MsgTargetInfo detialSimpl = new MsgTargetInfo(detail.getId(), noticeNo);
                            msgTargetInfoList.add(detialSimpl);
                        }
                    }
                    //把信息传入到StudentSimpleInfo
                    WxStudentInfoTemp wxTemp = new WxStudentInfoTemp();
                    wxTemp.setSchoolName(student.getSchoolName());
                    wxTemp.setGradeName(student.getGradeName());
                    wxTemp.setClassName(student.getClassName());
                    wxTemp.setStudentName(student.getStuName());
                    wxTemp.setMsgTargetInfoList(msgTargetInfoList);
                    stuSimplList.add(wxTemp);
                }
        );

        //获取教师姓名
        Teacher teacher = teacherMapper.selectByPrimaryKey(teNo);
        String teName = teacher.getName();
        String scCode = teacher.getScCode();

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setScCode(scCode);//在此传入学校代码用于获取SchoolWX配置信息 by hzl 2019-9-6 16:10:41
        messageInfo.setMsgNo(msgLog.getId().toString());
        messageInfo.setBatchId(msgLog.getBatchId());
        messageInfo.setContent(content);
        messageInfo.setTeName(teName);
        messageInfo.setSendDate(VeDate.dateToStrLong(sendDate));
        if (stuSimplList.size() != 0) {
            messageInfo.setWxStudentInfoTempList(stuSimplList);
        }

        //微信模板消息接口调用
        if (msgType.equals("微信消息")) {
            try {
                WxMsgSendThread wxMsgSendThread = new WxMsgSendThread(messageInfo);
                wxMsgSendThread.start();

                MessageLog dbMsgLog = new MessageLog();
                dbMsgLog.setId(msgLog.getId());
                dbMsgLog.setState("提交成功");
                messageLogMapper.updateByPrimaryKeySelective(dbMsgLog);
            } catch (Exception e) {
                MessageLog dbMsgLog = new MessageLog();
                dbMsgLog.setId(msgLog.getId());
                dbMsgLog.setState("提交失败");
                dbMsgLog.setRemark(e.getMessage());
                messageLogMapper.updateByPrimaryKeySelective(dbMsgLog);
            }
        }
        //电信接口调用
        else {
            MbMsgSendThread mbMsgSendThread = new MbMsgSendThread(messageInfo);
            mbMsgSendThread.start();
   //         mbMsgSendThread.run();
        }
    }


    /**
     * 给微信接口发送数据线程
     * by hzl 2019-8-23
     */
    class WxMsgSendThread extends Thread {
        private MessageInfo messageInfo;

        public WxMsgSendThread(MessageInfo messageInfo) {
            this.messageInfo = messageInfo;
        }

        @Override
        public void run() {
            //模板id通过数据库获取
            SchoolWX schoolWX = schoolWXMapper.selectByPrimaryKey(messageInfo.getScCode());
            String templateId = schoolWX.getNormalTmp();

            //获取要推送的学生信息列表
            List<WxStudentInfoTemp> list = messageInfo.getWxStudentInfoTempList();

            list.forEach(wxStuInfoTemp -> {
                        //获取学生的家长列表的openid集合
                        List<MsgTargetInfo> msgTargetInfoList = wxStuInfoTemp.getMsgTargetInfoList();
                        //1.组织模板数据
                        //                {{first.DATA}}
                        //                学校：{{keyword1.DATA}}
                        //                通知人：{{keyword2.DATA}}
                        //                时间：{{keyword3.DATA}}
                        //                通知内容：{{keyword4.DATA}}
                        //                {{remark.DATA}}

                        String content = messageInfo.getContent();
                        boolean status = content.contains("img");
                        if (status) {
                            content = "该信息含有图片，请点击详情查看";
                        }

                        List<WxMpTemplateData> data = Arrays.asList(
                                new WxMpTemplateData("first", "您关注的学生有一条校园通知\n"),
                                new WxMpTemplateData("keyword1", wxStuInfoTemp.getSchoolName() + "\n所在班级：" + wxStuInfoTemp.getClassName() + "\n学生姓名：" + wxStuInfoTemp.getStudentName()),
                                new WxMpTemplateData("keyword2", messageInfo.getTeName()),
                                new WxMpTemplateData("keyword3", messageInfo.getSendDate() + "\n"),
                                new WxMpTemplateData("keyword4", content),
                                new WxMpTemplateData("remark", "")
                        );
                        for (MsgTargetInfo targetInfo : msgTargetInfoList) {
                            //2.推送消息
                            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                                    .toUser(targetInfo.getNo())//要推送的用户openid
                                    .data(data) //数据
                                    .templateId(templateId)//模版id
                                    .url(localDomain + "/page/messageDetails?id=" + messageInfo.getMsgNo())//点击模版消息要访问的网址*/
                                    .build();
                            try {
                                //3.发起推送
                                wxMpService = wxService.getWxMpService(messageInfo.getScCode());
                                String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);

                                sleep(500);

                                //把状态写入到数据库
                                MessageDetail msgDetail = new MessageDetail();
                                msgDetail.setId(targetInfo.getDetailId());
                                msgDetail.setState("成功");
                                messageDetailMapper.updateByPrimaryKeySelective(msgDetail);
                            } catch (Exception e) {
                                //把状态写入到数据库
                                MessageDetail msgDetail = new MessageDetail();
                                msgDetail.setId(targetInfo.getDetailId());
                                msgDetail.setState("失败");
                                msgDetail.setRemark(e.getMessage());
                                messageDetailMapper.updateByPrimaryKeySelective(msgDetail);
                            }
                        }//openIdList for循环结束
                    }
            );//.forEach结束
        }
    }

    /**
     * 给手机短信接口发送数据线程
     * by hzl 2019-8-28 11:37:16
     */
    class MbMsgSendThread extends Thread {
        private MessageInfo messageInfo;

        public MbMsgSendThread(MessageInfo messageInfo) {
            this.messageInfo = messageInfo;
        }

        @Override
        public void run() {
            try {
                //获取要推送的学生信息列表
                List<WxStudentInfoTemp> list = messageInfo.getWxStudentInfoTempList();

                //短信内容
                String msgContent = messageInfo.getContent();

                //用来存储所有家长手机号集合信息
                List<MsgTargetInfo> allTargetList = new ArrayList<>();
                list.forEach(wxStuInfoTemp -> {
                    //获取学生的家长列表的phoneNo集合
                    List<MsgTargetInfo> msgTargetInfoList = wxStuInfoTemp.getMsgTargetInfoList();
                    allTargetList.addAll(msgTargetInfoList);
                });


                String batchId = messageInfo.getBatchId();

                //获取提交结果
                GsmsResponse resp = mbMsgService.doPostSMS(batchId, msgContent, allTargetList, "norm");

                //根据结果写入到数据库
                if (resp.getResult() == 0) {
                    MessageLog messageLog = new MessageLog();
                    messageLog.setId(Integer.valueOf(messageInfo.getMsgNo()));
                    messageLog.setState("提交成功");
                    messageLogMapper.updateByPrimaryKeySelective(messageLog);
                } else {
                    throw new Exception(resp.getMessage());
                }
            } catch (Exception e) {
                MessageLog messageLog = new MessageLog();
                messageLog.setId(Integer.valueOf(messageInfo.getMsgNo()));
                messageLog.setState("提交失败");
                messageLog.setRemark(e.getMessage());
                messageLogMapper.updateByPrimaryKeySelective(messageLog);
            }
        }
    }

    @Override
    public void del(int id) {
        List<MessageDetail> list = messageDetailMapper.listByLogId(id);
        list.forEach(detail -> {
            messageDetailMapper.deleteByPrimaryKey(detail.getId());
        });
        messageLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageResult messagePageList(int pageIndex, int pageSize, List<String> teNoList, String msgType, String state) {
        Integer count = messageLogMapper.pageListCount(teNoList, "", state,"createDate DESC");
        int offset = (pageIndex == -1) ? pageIndex : (pageIndex - 1) * pageSize;


        List<MessageLog> list = messageLogMapper.pageList(offset, pageSize, "createDate DESC", teNoList, msgType, state);
        return new PageResult(count, list);
    }

//    @Override
//    public PageResult messageDetailPageList(int pageIndex, int pageSize, int logId, String state) {
//        Integer count = messageDetailMapper.pageListCount(logId, state);
//        int offset = (pageIndex - 1) * pageSize;
//        List<MessageDetail> list = messageDetailMapper.pageList(offset, pageSize, logId, state);
//        return new PageResult(count, list);
//    }


    @Override
    public PageResult messageDetailPageList(int pageSize, int pageIndex, int logId, String state) {
        Integer count = messageDetailMapper.pageListCount(logId, state);
        String id = String.valueOf(logId);
        List<Map<String, Object>> list = messageDetailMapper.list(pageIndex, pageSize, id, state, null, null, null, null);
        return new PageResult(count, list);
    }

    @Override
    public String getMessageContent(Integer id) {
        MessageLog messageLog = messageLogMapper.selectByPrimaryKey(id);
        if (null != messageLog)
            return messageLog.getMsgContent();
        return "";
    }

    @Override
    public void ascNoticeSendInterface(String scCode, String stuNo, String leaveId,
                                       String imgName, String deviceNo,
                                       Integer ascType, Integer actionType,
                                       String actionTime, String ascRemark) throws Exception {
        //1.查找学生
        Student student = studentService.loadByScCodeAndStuNo(scCode, stuNo);
        //region检查类型是否正确
        String ascTypeDesc = AscTypeEnum.getDescByCode(ascType);
        String actionTypeDesc = AscActionEnum.getDescByCode(actionType);
        Date actionDate = VeDate.strToDateLong(actionTime);
        String noticeWay = "微信消息";//默认微信
        Config noticeConfig = configService.findByScCodeAndCfCode(scCode, "NOTICEWAY");
        if (noticeConfig != null) {
            noticeWay = noticeConfig.getValue();
        }
        //endregion

        //region判断是否迟到（只有进入方向才进行判断）
        Boolean isLate = false;
        if (actionType.equals(1)) {
            //默认校门迟到时间配置
            Config config = configMapper.selectByScCodeAndCfCode(scCode, "SCHOOLLATETIME");
            //默认公寓晚归时间配置
            if (ascType.equals(2)) {
                config = configMapper.selectByScCodeAndCfCode(scCode, "DORLATETIME");
            }
            if (null != config) {
                try {
                    Date lateSetTime = VeDate.strToDateLong(config.getValue());
                    if (VeDate.isLate(lateSetTime, actionDate)) {
                        isLate = true;
                        actionType = 4;
                    }
                } catch (Exception e) {
                }
            }
        }
        //endregion

        //region把数据写入禁消息日志表：asc_norice_log
        AscNoticeLog asclog = new AscNoticeLog();
        asclog.setScCode(scCode);
        asclog.setStuId(student.getStuId());
        asclog.setAsctype(ascType);
        asclog.setAscdeviceno(deviceNo);
        asclog.setActiontype(actionType);
        asclog.setActiontime(actionDate);
        asclog.setAscremark(ascRemark);
        asclog.setImgName(imgName);
        asclog.setLeaveId(StringUtils.isNotBlank(leaveId) ? Integer.valueOf(leaveId) : null);
        asclog.setCreatetime(new Date());
        asclog.setBatchId(UUID.randomUUID().toString());
        asclog.setNoticeway(noticeWay);
        ascNoticeLogMapper.insertSelective(asclog);
        //endregion

        //region 用到的基础信息
        int ascLogId = asclog.getId();
        String batchId = asclog.getBatchId();
        List<MsgTargetInfo> targetList = new ArrayList<>();
        //基础信息
        String noticeNo = "";
        String stuName = student.getStuName();
        String className = student.getClassName();
        String remark = "";
        //endregion 用到的基础信息

        //region 判断是否迟到，然后添加教师通知信息加，以便通知教师
        //只有进入方向才进行判断
        if (actionType.equals(1) && isLate) {
            //查找学生教师通知信息添加
            List<TeacherWxInfo> teacherWxInfos = teacherWxInfoService.getListByStuId(student.getStuId());
            for (TeacherWxInfo tWxInfo : teacherWxInfos) {
                //根据noticeWay类型获取对应的通知号码
                if (noticeWay.equals("微信消息")) {
                    noticeNo = tWxInfo.getOpenId();
                }
                if (noticeWay.equals("手机短信")) {
                    noticeNo = tWxInfo.getPhoneNo();
                }
                if (StringUtils.isNotBlank(noticeNo)) {
                    //把数据写入到asc_botice_detail表
                    AscNoticeDetail detail = new AscNoticeDetail();
                    detail.setLogId(ascLogId);
                    detail.setGuaName("teNo@" + tWxInfo.getTeNo());
                    detail.setNoticeNo(noticeNo);
                    ascNoticeDetailMapper.insertSelective(detail);

                    int detailId = detail.getId();
                    targetList.add(new MsgTargetInfo(detailId, noticeNo, true));
                }
            }//for 结束
        }
        //endregion

        //region 添加学生家长通知信息
        List<Guardian> guaList = student.getGuardianList();
        for (Guardian gua : guaList) {
            //根据noticeWay类型获取对应的通知号码
            if (noticeWay.equals("微信消息")) {
                noticeNo = gua.getWxNo();
            }
            if (noticeWay.equals("手机短信")) {
                noticeNo = gua.getPhone();
            }

            if (StringUtils.isNotBlank(noticeNo)) {
                //把数据写入到asc_botice_detail表
                AscNoticeDetail detail = new AscNoticeDetail();
                detail.setLogId(ascLogId);
                detail.setGuaName(gua.getGuaName());
                detail.setNoticeNo(noticeNo);
                ascNoticeDetailMapper.insertSelective(detail);

                int detailId = detail.getId();
                targetList.add(new MsgTargetInfo(detailId, noticeNo));
            }
        }//for 结束
        //endregion

        //region 调用相关通知接口
        try {
            if (targetList.size() == 0) {
                throw new Exception("no find student guardian any noticeNo info");
            }
            //4.2消息通知接口
            ascNotice(scCode, noticeWay, stuName, className, batchId, targetList, ascTypeDesc, actionTypeDesc, actionTime, remark, isLate);

            //4.3把结果写入到数据库
            AscNoticeLog dbAsclog = new AscNoticeLog();
            dbAsclog.setId(ascLogId);
            dbAsclog.setNoticeresult("成功");
            ascNoticeLogMapper.updateByPrimaryKeySelective(dbAsclog);
        } catch (Exception e) {
            //把结果写入到数据库
            AscNoticeLog dbAsclog = new AscNoticeLog();
            dbAsclog.setId(ascLogId);
            dbAsclog.setNoticeresult("失败");
            dbAsclog.setFailreason(e.getMessage());
            ascNoticeLogMapper.updateByPrimaryKeySelective(dbAsclog);
            throw e;
        }
        //endregion
    }

    /***
     *  门禁消息通知（微信、手机短信）
     *  by hzl 2019-8-24 16:17:48
     * @param noticeWay
     * @param stuName
     * @param targetList
     * @param ascType
     * @param actionType
     * @param actionTime
     * @param remark
     * @throws Exception
     */

    public void ascNotice(String scCode, String noticeWay, String stuName, String className,
                          String batchId, List<MsgTargetInfo> targetList, String ascType,
                          String actionType, String actionTime, String remark, Boolean isLate) throws Exception {
        if (noticeWay.equals("微信消息")) {
            ascWxNotice(scCode, stuName, className, batchId, targetList, ascType, actionType, actionTime, remark, isLate);
        } else if (noticeWay.equals("手机短信")) {
            ascMbNotice(stuName, className, batchId, targetList, ascType, actionType, actionTime, remark, isLate);
        } else {
            throw new Exception("noticeWay类型为未找到,请检查类型格式是否正确:" + noticeWay);
        }
    }


    //    标题：校园安全提醒
//    {{first.DATA}}
//    学生姓名：{{keyword1.DATA}}
//    进出方向：{{keyword2.DATA}}
//    进出时间：{{keyword3.DATA}}
//    {{remark.DATA}}
//"	校园安全提醒"模板
    public String ascTempId1 = "DxuO-zcLr__n70jEfC11rIwTEIdlQptUAUadyfG6lLU";

    /**
     * 门禁消息微信通知
     * by hzl 2019-8-24 15:16:09
     *
     * @param stuName    学生姓名
     * @param targetList 微信openid集合
     * @param ascType    门禁类型：校园、公寓
     * @param actionType 动作类型：进入、离开
     * @param actionTime 动作时间
     * @param remark     备注（其他消息）
     * @param isLate     是否迟到类型
     * @throws Exception
     */
    public void ascWxNotice(String scCode, String stuName, String className, String batchId,
                            List<MsgTargetInfo> targetList, String ascType,
                            String actionType, String actionTime, String remark, Boolean isLate) throws Exception {

        //获取学校微信配置信息
        SchoolWX schoolWX = schoolWXMapper.selectByPrimaryKey(scCode);
        //获取门禁通知微信模板
        String templateId = schoolWX.getAscTmp();

        //1.组织模板数据
        //组织first信息
        String firstValue = " 您的孩子已经" + actionType + ascType + "!\n";
        //在学生姓名keyword1里面增加“门禁类型：”字段且换行
        String keyword1Value = stuName + "\n所在班级：" + className + "\n门禁类型：" + ascType;
        //家长通知文字模板
        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first", firstValue),
                new WxMpTemplateData("keyword1", keyword1Value),
                new WxMpTemplateData("keyword2", actionType),
                new WxMpTemplateData("keyword3", actionTime),
                new WxMpTemplateData("remark", remark)
        );

        //根据类型做相应的调整通知内容
        if (isLate) {
            firstValue = "您的学生迟到";
            if (ascType.equals("校园")) {
                firstValue = " 您的学生返校迟到!\n";
            } else if (ascType.equals("公寓")) {
                firstValue = " 您的学生回公寓晚归!\n";
            }
        }
        //教师通知文字模板
        List<WxMpTemplateData> dataLate = Arrays.asList(
                new WxMpTemplateData("first", firstValue),
                new WxMpTemplateData("keyword1", keyword1Value),
                new WxMpTemplateData("keyword2", actionType),
                new WxMpTemplateData("keyword3", actionTime),
                new WxMpTemplateData("remark", remark)
        );

        for (MsgTargetInfo target : targetList) {
            String openId = target.getNo();
            Integer detailId = target.getDetailId();
            //2.推送消息
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openId)//要推送的用户openid
                    .data(target.getTeacher() ? dataLate : data) //数据
                    .templateId(templateId)//模版id
                    .url(localDomain + "/page/ascNoticeInfo?id=" + batchId)//门禁上传的图片
                    .build();
            //3.发起推送
            try {
                wxMpService = wxService.getWxMpService(scCode);
                String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
                //4.把结果写入到数据
                AscNoticeDetail ascDetail = new AscNoticeDetail();
                ascDetail.setId(detailId);
                ascDetail.setState("成功");
                ascNoticeDetailMapper.updateByPrimaryKeySelective(ascDetail);
            } catch (Exception e) {
                //4.把结果写入到数据
                AscNoticeDetail ascDetail = new AscNoticeDetail();
                ascDetail.setId(detailId);
                ascDetail.setState("失败");
                ascDetail.setRemark(e.getMessage());
                ascNoticeDetailMapper.updateByPrimaryKeySelective(ascDetail);
            }
        }//for end
    }

    /**
     * 门禁消息短信通知
     * by hzl 2019-8-24 2019-8-24 16:05:01
     *
     * @param stuName    学生姓名
     * @param targetList 手机号集合
     * @param ascType    门禁类型：校园、公寓
     * @param actionType 动作类型：进入、离开
     * @param actionTime 动作时间
     * @param remark     备注（其他消息）
     * @throws Exception
     */
    public void ascMbNotice(String stuName, String className, String batchId,
                            List<MsgTargetInfo> targetList, String ascType, String actionType,
                            String actionTime, String remark, Boolean isLate) throws Exception {
        //1.组织信息内容
        String msgContent = " 您的孩子(班级：" + className + " 姓名：" + stuName + ")于 " + actionTime + " 已经" + actionType + ascType + "!\n" + remark;

        String lateDesc = "";
        if (isLate) {
            if (ascType.equals("校园")) {
                lateDesc = " 回到校园迟到!\n";
            } else if (ascType.equals("公寓")) {
                lateDesc = " 回到公寓晚归!\n";
            }
        }
        String msgContent_te = " 您的学生(班级：" + className + " 姓名：" + stuName + ")于 " + actionTime + lateDesc + remark;

        //在此对于"asc"类型的数据通过拼接的方式把短信内容由家长信息通过@@拼接教师信息，以便接口里面区别调用 by hzl 2019-9-16 16:39:40
        msgContent = msgContent + "@@" + msgContent_te;
        try {
            //2.请求电信接口
            GsmsResponse resp = mbMsgService.doPostSMS(batchId, msgContent, targetList, "asc");
            //根据结果写入到数据库
            if (resp.getResult() != 0) {
                throw new Exception(resp.getMessage());
            }
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    @Transactional
    public void mbMsgUpdateStateByRes(List<MTResponse> list) throws Exception {
        if (list != null && list.size() > 0) {
            MTResponse resp = null;
            String batchId = null;
            String customMsgId = null;
            String remark = null;
            int state;
            for (int i = 0, size = list.size(); i < size; i++) {
                resp = list.get(i);
                state = resp.getState();
                batchId = resp.getBatchID();
                customMsgId = resp.getCustomMsgID();
                remark = MbMsgResEnum.getDescByCode(state);
                String stateStr = (state == 0 || state == 1) ? remark : "失败";

                try {
                    //根据customMsgId的前缀去区分
                    String[] customMsgIdArr = customMsgId.split("-");
                    String prefixStr = customMsgIdArr[0];
                    String detailId = customMsgIdArr[1];
                    if ("norm".equals(prefixStr)) {
                        //正常通知消息
                        messageDetailMapper.updatStateById(detailId, stateStr, remark);
                    } else if ("asc".equals(prefixStr)) {
                        //门禁消息
                        ascNoticeDetailMapper.updatStateById(detailId, stateStr, remark);
                    } else {

                    }
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    @Transactional
    public void mbMsgUpdateStateByRpt(List<MTReport> list) throws Exception {
        if (list != null && list.size() > 0) {
            MTReport report = null;
            String batchId = null;
            String customMsgId = null;
            String remark = null;
            int state;
            for (int i = 0, size = list.size(); i < size; i++) {
                report = list.get(i);
                state = report.getState();
                batchId = report.getBatchID();
                customMsgId = report.getCustomMsgID();
                remark = MbMsgRptEnum.getDescByCode(state);
                String stateStr = (state == 0) ? remark : "失败";
                remark = (state == 0) ? null : remark;
                try {
                    //根据customMsgId的前缀去区分
                    String[] customMsgIdArr = customMsgId.split("-");
                    String prefixStr = customMsgIdArr[0];
                    String detailId = customMsgIdArr[1];
                    if ("norm".equals(prefixStr)) {
                        //正常通知消息
                        messageDetailMapper.updatStateById(detailId, stateStr, remark);
                    } else if ("asc".equals(prefixStr)) {
                        //门禁消息
                        ascNoticeDetailMapper.updatStateById(detailId, stateStr, remark);
                    } else {

                    }
                } catch (Exception e) {

                }
            }
        }
    }

}
