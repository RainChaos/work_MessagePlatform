package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.GuardianMapper;
import com.jiapeng.messageplatform.dao.GuardianWxLogMapper;
import com.jiapeng.messageplatform.dao.StudentMapper;
import com.jiapeng.messageplatform.entity.GuadianWxLog;
import com.jiapeng.messageplatform.entity.Guardian;
import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.entity.WxBandEntity;
import com.jiapeng.messageplatform.service.GuardianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by HZL on 2019/8/15.
 */
@Service
public class GuardianServiceImpl implements GuardianService {
    @Autowired
    GuardianMapper guardianMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    GuardianWxLogMapper guardianWxLogMapper;



    @Override
    public  List<WxBandEntity>  getByOpenId(String openId) {
        List<WxBandEntity>  guardian = guardianMapper.find(null, openId);
        return guardian;
    }

    @Transactional
    @Override
    public void cancelWxBand(String wxNo,Integer guaId) {
        //取消绑定后
        guardianMapper.updateByStuAndGuaname(wxNo,guaId);

        //查找该学生是否还有其他家长绑定便于更新学生微信绑定信息
        Guardian guardian = guardianMapper.getGuadianById(String.valueOf(guaId));
        List<Guardian> guadianList = guardianMapper.listByStuId(guardian.getStuId());
        Student student = new Student();
        student.setIsBind("0");
        student.setStuId(guardian.getStuId());
        if(guadianList.size()>0){
            for (Guardian guadian: guadianList) {
                if(guadian.getWxNo()!=null&&guadian.getWxNo().length()>0){
                    student.setIsBind("1");
                    break;
                }
            }
        }
        studentMapper.updateByPrimaryKeySelective(student);

        //将家长微信操作记录写入数据库
        Student stu = studentMapper.selectByPrimaryKey(guardian.getStuId());

        GuadianWxLog guadianWxLog = new GuadianWxLog();
        guadianWxLog.setGuaname(guardian.getGuaName());
        guadianWxLog.setOperate("解绑微信");
        guadianWxLog.setStuname(stu.getStuName());
        guadianWxLog.setStuno(stu.getStuNo());
        guadianWxLog.setStuid(guardian.getStuId());
        guadianWxLog.setSc_code(stu.getScCode());
        guadianWxLog.setWxno(wxNo);
        guardianWxLogMapper.insert(guadianWxLog);


    }

    @Override
    public String getPhoneNo(String scCode, String stuNo,String stuName, String guaName) {
        Guardian guardian = guardianMapper.findPhoneByKey(scCode,stuNo,stuName, guaName);
        if (null != guardian)
            return guardian.getPhone();
        return "";
    }


    @Transactional
    @Override
    public void wxBand(String scCode, String stuNo, String stuName,String guaName, String phone, String openId) throws Exception {
        //根据学校代码和学生编号,查找
        Student student = studentMapper.findByScCodeAndStuNo(scCode,stuNo,stuName);
        if (null == student)
            throw new Exception("根据你输入的信息未找到相应的学生！");
        String stuId = student.getStuId();
        List<Guardian> guardianList = guardianMapper.listByStuId(stuId);
        if (guardianList.size() == 0) {
            //添加家长记录
            Guardian guardian = new Guardian();
            guardian.setStuId(stuId);
            String phone2 = phone.replaceAll(" ","");
            guardian.setPhone(phone2);
            guardian.setWxNo(openId);
            guardian.setGuaName(guaName);
            guardianMapper.insert(guardian);
        } else {
            Boolean isExist = false;
            //修改家长记录
            for (Guardian guardian : guardianList) {
                if (guardian.getGuaName().equals(guaName)) {
                    guardian.setPhone(phone);
                    guardian.setWxNo(openId);
                    guardianMapper.updateByPrimaryKeySelective(guardian);
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {

                //查询该家长是否已经绑定学生ID为参数值的记录
                List<WxBandEntity> datalist = guardianMapper.find(stuId,openId);
                if(datalist.size()>0){
                    //说明该这个微信号已经绑定这个学生的
                    throw new Exception("不可重复绑定该学生！");
                };
                //添加家长记录
                Guardian guardian = new Guardian();
                guardian.setStuId(stuId);
                guardian.setPhone(phone);
                guardian.setWxNo(openId);
                guardian.setGuaName(guaName);
                guardianMapper.insert(guardian);
            }

        }

        student.setStuId(stuId);
        student.setIsBind("1");
        studentMapper.updateByPrimaryKeySelective(student);


        //将家长微信操作记录写入数据库
        GuadianWxLog guadianWxLog = new GuadianWxLog();
        guadianWxLog.setGuaname(guaName);
        guadianWxLog.setOperate("绑定微信");
        guadianWxLog.setStuname(stuName);
        guadianWxLog.setStuno(stuNo);
        guadianWxLog.setStuid(stuId);
        guadianWxLog.setSc_code(scCode);
        guadianWxLog.setWxno(openId);
        guardianWxLogMapper.insert(guadianWxLog);


    }


}
