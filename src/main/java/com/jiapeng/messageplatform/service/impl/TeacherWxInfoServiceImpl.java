package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.StudentMapper;
import com.jiapeng.messageplatform.dao.TeacherWxInfoMapper;
import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.entity.TeacherWxInfo;
import com.jiapeng.messageplatform.service.TeacherWxInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HZL on 2019/9/9.
 */
@Service
public class TeacherWxInfoServiceImpl implements TeacherWxInfoService {
    @Autowired
    TeacherWxInfoMapper teacherWxInfoMapper;
    @Autowired
    StudentMapper studentMapper;

    @Transactional
    @Override
    public void wxBind(String clCodes, String teNo, String openId, String phoneNo) throws Exception {
        String[] clCodeArr = clCodes.split(",");
        for (String clCode : clCodeArr) {
            if (StringUtils.isNotBlank(clCode)) {
                TeacherWxInfo info = new TeacherWxInfo();
                info.setClCode(clCode);
                info.setTeNo(teNo);
                info.setOpenId(openId);
                info.setPhoneNo(phoneNo);
                info.setCreateDate(new Date());
                teacherWxInfoMapper.insertSelective(info);
            }
        }
    }

    @Override
    public void cancelWxBind(Integer id) {
        teacherWxInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<TeacherWxInfo> getListByStuId(String stuId) {
        List<TeacherWxInfo> list = new ArrayList<>();
        Student student = studentMapper.selectByPrimaryKey(stuId);
        if (null != student) {
            list = teacherWxInfoMapper.getListByClCode(student.getClCode());
        }
        return list;
    }
}
