package com.jiapeng.messageplatform.service.impl;

/**
 * Created by Administrator on 2019/9/4.
 */

import com.jiapeng.messageplatform.dao.*;
import com.jiapeng.messageplatform.entity.Config;
import com.jiapeng.messageplatform.entity.ConfigDefine;
import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LLJ on 2019/7/8.
 */
@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private UpdateDao updateDao;
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ConfigDefineMapper configDefineMapper;


    private List<String> sqlList = new ArrayList<String>();

    @Override
    public void update() {
        for (int i = 0; i < sqlList.size(); i++) {
            try {
                updateDao.executeSql(sqlList.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initStuPage() {

        //获取菜单【数据为空则插入】
        List<ConfigDefine> defineList = configDefineMapper.list();
        if (defineList.size() == 0) {
            //插入config表
            String sql = "SET IDENTITY_INSERT [dbo].[config] ON \n" +
                    "INSERT [dbo].[config_define] ( [name], [code], [type], [selectValue], [remark]) VALUES ( N'门禁消息通知方式', N'NOTICEWAY', N'Select', N'微信消息,手机短信', NULL)\n" +
                    "INSERT [dbo].[config_define] ( [name], [code], [type], [selectValue], [remark]) VALUES ( N'校门迟到通知时间', N'SCHOOLLATETIME', N'Date', NULL, NULL)\n" +
                    "INSERT [dbo].[config_define] ( [name], [code], [type], [selectValue], [remark]) VALUES ( N'公寓晚归通知时间', N'DORLATETIME', N'Date', NULL, NULL)\n" +
                    "SET IDENTITY_INSERT [dbo].[config] OFF";
            updateDao.executeSql(sql);
        }

        //获取菜单【数据为空则插入】
        List<ModuleEntity> moduleEntity = moduleMapper.getAll();
        if (moduleEntity.size() == 0) {
            String sql = "" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'100', N'系统设置', NULL, N'&#xe61d', NULL, 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'100101', N'角色管理', N'100', N'&#xe61d', N'/page/roleList', 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'100102', N'管理员管理', N'100', N'&#xe61d', N'/page/adminList', 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'100103', N'菜单管理', N'100', N'&#xe61d', N'/page/menuList', 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'101', N'学校管理', NULL, N'&#xe61d', NULL, 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'101100', N'学校管理', N'101', N'&#xe61d', N'/page/schoolList', 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'101101', N'年级管理', N'101', N'&#xe61d', N'/page/gradeList', 1, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'101102', N'教师管理', N'101', N'&#xe61d', N'/page/teacherList', 3, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'101103', N'学生管理', N'101', N'&#xe61d', N'/page/studentList', 4, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'101105', N'学生头像采集', N'101', N'&#xe61d', N'/page/rlcj', 4, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'101104', N'班级管理', N'101', N'&#xe61d', N'/page/classList', 2, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'102', N'通知发布管理', NULL, N'&#xe61d', NULL, 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'102101', N'发信记录', N'102', N'&#xe61d', N'/page/messageList', 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'103', N'配置管理', NULL, N'&#xe61d', N'', 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'103100', N'配置设置', N'103', N'&#xe61d', N'/page/configList', 0, NULL)\n"+
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'103101', N'配置定义', N'103', N'&#xe61d', N'/page/configItemList ', 0, NULL)\n"+
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'104', N'门禁消息', NULL, N'&#xe61d', N'', 0, NULL)\n" +
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'104100', N'推送记录', N'104', N'&#xe61d', N'/page/ascList', 0, NULL)\n"+
                    "INSERT [dbo].[sys_module] ([id], [name], [parentid], [icon], [path], [ordernumber], [idname]) VALUES (N'104101', N'出入统计', N'104', N'&#xe61d', N'/page/ascTotal', 0, NULL)";
            updateDao.executeSql(sql);
        }


        //获取角色【数据为空则插入】
        List<Map<String, Object>> rolelist = roleMapper.getAll();
        if (rolelist.size() == 0) {
            String sql = "INSERT [dbo].[sys_role] ([id], [name]) VALUES (N'100', N'管理员')";
            updateDao.executeSql(sql);

        }


        //获取角色菜单
        List<Map<String, Object>> roleModulelist = roleMapper.getRoleModuleAll();
        if (roleModulelist.size() == 0) {
            String sql = "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'100')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'100101')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'100102')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'100103')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'101')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'101100')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'101101')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'101102')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'101103')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'101104')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'102')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'102101')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'103')\n" +
                    "INSERT [dbo].[sys_role_module] ([rid], [mid]) VALUES (N'100', N'103100')";
            updateDao.executeSql(sql);
        }


        //获取用户【数据为空则插入】
        List<Map<String, Object>> userlist = userMapper.list("", 10, 1);
        if (userlist.size() == 0) {
            String sql = "INSERT [dbo].[sys_user] ([name], [password], [dislog], [dislogreason], [dislogtime]) VALUES (N'admin', N'e10adc3949ba59abbe56e057f20f883e', 0, N'test', CAST(0x0000AAA000FAA210 AS DateTime))";
            updateDao.executeSql(sql);
        }

        //获取用户【数据为空则插入】
        List<Map<String, Object>> userrolelist = userMapper.getUserRolelist();
        if (userrolelist.size() == 0) {
            String sql = "INSERT [dbo].[sys_user_role] ([uid], [rid]) VALUES (1, N'100')";
            updateDao.executeSql(sql);
        }

    }

    {
        //asc_notice_detail
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'asc_notice_detail')" +
                "CREATE TABLE [dbo].[asc_notice_detail](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[logId] [int] NULL,\n" +
                "\t[guaName] [nvarchar](20) NULL,\n" +
                "\t[noticeNo] [nvarchar](32) NULL,\n" +
                "\t[state] [nvarchar](10) NULL,\n" +
                "\t[remark] [nvarchar](300) NULL,\n" +
                " CONSTRAINT [PK_asc_notice_detail] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        //asc_notice_log
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'asc_notice_log') " +
                "CREATE TABLE [dbo].[asc_notice_log](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[sc_code] [nvarchar](30) NULL,\n" +
                "\t[stuId] [nvarchar](100) NULL,\n" +
                "\t[noticeWay] [nvarchar](10) NULL,\n" +
                "\t[ascDeviceNo] [nvarchar](32) NULL,\n" +
                "\t[ascType] [int] NULL,\n" +
                "\t[actionType] [int] NULL,\n" +
                "\t[actionTime] [datetime] NULL,\n" +
                "\t[ascRemark] [nvarchar](200) NULL,\n" +
                "\t[noticeResult] [nvarchar](10) NULL,\n" +
                "\t[failReason] [nvarchar](200) NULL,\n" +
                "\t[createTime] [datetime] NULL,\n" +
                "\t[imgName] [nvarchar](100) NULL,\n" +
                "\t[leaveId] [int] NULL,\n" +
                "\t[batchId] [nvarchar](64) NOT NULL,\n" +
                " CONSTRAINT [PK_asc_notice_log] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        //class
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'class')" +
                "CREATE TABLE [dbo].[class](\n" +
                "\t[cl_code] [nvarchar](100) NOT NULL,\n" +
                "\t[cl_number] [nvarchar](100) NULL,\n" +
                "\t[name] [nvarchar](50) NULL,\n" +
                "\t[sc_code] [nvarchar](100) NULL,\n" +
                "\t[gr_code] [nvarchar](100) NULL,\n" +
                " CONSTRAINT [PK_class] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[cl_code] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],\n" +
                " CONSTRAINT [cl_code] UNIQUE NONCLUSTERED \n" +
                "(\n" +
                "\t[cl_code] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");
        sqlList.add(createColStr("class", "uuidstr", "nvarchar(100)", null));

        //class_teacher
        sqlList.add(" if not exists (SELECT   name   FROM   sysobjects   where   name= 'class_teacher') " +
                "CREATE TABLE [dbo].[class_teacher](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[cl_code] [nvarchar](100) NULL,\n" +
                "\t[te_no] [nvarchar](100) NULL,\n" +
                "\t[isheadmaster] [bit] NULL,\n" +
                " CONSTRAINT [PK_class_teacher] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],\n" +
                " CONSTRAINT [clCode_teNo] UNIQUE NONCLUSTERED \n" +
                "(\n" +
                "\t[cl_code] ASC,\n" +
                "\t[te_no] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        //grade
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'grade') " +
                "CREATE TABLE [dbo].[grade](\n" +
                "\t[gr_code] [nvarchar](100) NOT NULL,\n" +
                "\t[name] [nvarchar](100) NULL,\n" +
                "\t[ordernumber] [int] NULL,\n" +
                " CONSTRAINT [PK_grade_1] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[gr_code] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");
        sqlList.add(createColStr("grade", "sc_code", "nvarchar(100)", null));//年级表增加路径字段 by xie 2019-10-21 15:13:03
        sqlList.add(createColStr("grade", "uuidstr", "nvarchar(100)", null));

        sqlList.add("alter table grade add constraint scCodeAndName UNIQUE(sc_code,name)");

        //guardian
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'guardian') " +
                "CREATE TABLE [dbo].[guardian](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[stuId] [nvarchar](100) NOT NULL,\n" +
                "\t[guaName] [nvarchar](100) NULL,\n" +
                "\t[phone] [nvarchar](50) NULL,\n" +
                "\t[wxNo] [nvarchar](50) NULL,\n" +
                "\t[relation] [nvarchar](50) NULL,\n" +
                " CONSTRAINT [PK_guardian] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        //leave
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'leave') " +
                "CREATE TABLE [dbo].[leave](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[teNo] [nvarchar](30) NULL,\n" +
                "\t[stuNo] [nvarchar](30) NULL,\n" +
                "\t[startDate] [datetime] NOT NULL,\n" +
                "\t[endDate] [datetime] NOT NULL,\n" +
                "\t[reason] [nvarchar](200) NULL,\n" +
                "\t[createDate] [datetime] NULL,\n" +
                "\t[updateDate] [datetime] NULL,\n" +
                "\t[gateReadState] [bit] NULL,\n" +
                "\t[readFailReason] [nvarchar](200) NULL,\n" +
                " CONSTRAINT [PK_leave] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        //message_detail
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'message_detail')" +
                " CREATE TABLE [dbo].[message_detail](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[logId] [int] NULL,\n" +
                "\t[stuId] [nvarchar](100) NULL,\n" +
                "\t[guaName] [nvarchar](20) NULL,\n" +
                "\t[phone] [nvarchar](32) NULL,\n" +
                "\t[state] [nvarchar](10) NULL,\n" +
                "\t[remark] [nvarchar](300) NULL,\n" +
                " CONSTRAINT [PK_message_detail] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        //message_log
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'message_log') " +
                "CREATE TABLE [dbo].[message_log](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[te_no] [nvarchar](100) NULL,\n" +
                "\t[msgContent] [nvarchar](500) NULL,\n" +
                "\t[sendDate] [datetime] NULL,\n" +
                "\t[msgType] [nvarchar](20) NULL,\n" +
                "\t[createDate] [datetime] NULL,\n" +
                "\t[state] [nvarchar](50) NULL,\n" +
                "\t[remark] [nvarchar](500) NULL,\n" +
                "\t[batchId] [nvarchar](64) NOT NULL,\n" +
                " CONSTRAINT [PK_messageLog] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        //school
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'school')" +
                " CREATE TABLE [dbo].[school](\n" +
                "\t[sc_code] [nvarchar](100) NOT NULL,\n" +
                "\t[name] [nvarchar](100) NULL,\n" +
                "\t[token] [nvarchar](100) NULL,\n" +
                "\t[address] [nvarchar](50) NULL,\n" +
                "\t[contacts] [nvarchar](50) NULL,\n" +
                "\t[contactphone] [nvarchar](50) NULL,\n" +
                "\t[createtime] [datetime] NULL,\n" +
                "\t[status] [bit] NULL,\n" +
                "\t[statustime] [datetime] NULL,\n" +
                "\t[statusreason] [nvarchar](200) NULL,\n" +
                " CONSTRAINT [PK_school_1] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[sc_code] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");
        sqlList.add(createColStr("school", "menhukey", "nvarchar(300)", null));//学生表增加人脸模板2路径字段 by hzl 2019-10-21 15:13:03
        sqlList.add(createColStr("school", "uuidstr", "nvarchar(100)", null));

        //school_ip
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'school_ip')" +
                "CREATE TABLE [dbo].[school_ip](\n" +
                "\t[sc_code] [nvarchar](100) NULL,\n" +
                "\t[ip_pass] [nvarchar](1000) NULL\n" +
                ") ON [PRIMARY]");

        //student
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'student') " +
                "CREATE TABLE [dbo].[student](\n" +
                "\t[stuId] [nvarchar](100) NOT NULL,\n" +
                "\t[stuNo] [nvarchar](30) NULL,\n" +
                "\t[stuName] [nvarchar](100) NULL,\n" +
                "\t[sex] [nvarchar](1) NULL,\n" +
                "\t[idCode] [nvarchar](18) NULL,\n" +
                "\t[cl_code] [nvarchar](100) NOT NULL,\n" +
                "\t[createDate] [datetime] NULL,\n" +
                "\t[updateDate] [datetime] NULL,\n" +
                " CONSTRAINT [PK_student_1] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[stuId] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");


        //sys_module
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'sys_module') " +
                "CREATE TABLE [dbo].[sys_module](\n" +
                "\t[id] [nvarchar](20) NOT NULL,\n" +
                "\t[name] [nvarchar](20) NOT NULL,\n" +
                "\t[parentid] [nvarchar](20) NULL,\n" +
                "\t[icon] [nvarchar](20) NULL,\n" +
                "\t[path] [nvarchar](100) NULL,\n" +
                "\t[ordernumber] [int] NULL,\n" +
                "\t[idname] [nvarchar](50) NULL,\n" +
                " CONSTRAINT [PK_sys_module] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],\n" +
                "UNIQUE NONCLUSTERED \n" +
                "(\n" +
                "\t[parentid] ASC,\n" +
                "\t[name] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        //sys_role
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'sys_role') " +
                "CREATE TABLE [dbo].[sys_role](\n" +
                "\t[id] [nvarchar](20) NOT NULL,\n" +
                "\t[name] [nvarchar](50) NOT NULL,\n" +
                " CONSTRAINT [PK_sys_role] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

//sys_role_module
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'sys_role_module') " +
                "CREATE TABLE [dbo].[sys_role_module](\n" +
                "\t[rid] [nvarchar](20) NULL,\n" +
                "\t[mid] [nvarchar](20) NULL\n" +
                ") ON [PRIMARY]");

        //sys_user
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'sys_user') " +
                "CREATE TABLE [dbo].[sys_user](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[name] [nvarchar](20) NULL,\n" +
                "\t[password] [nvarchar](50) NULL,\n" +
                "\t[dislog] [bit] NULL,\n" +
                "\t[dislogreason] [nvarchar](50) NULL,\n" +
                "\t[dislogtime] [datetime] NULL,\n" +
                "\t[sc_code] [nvarchar](100) NULL,\n" +
                " CONSTRAINT [PK_sys_user] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],\n" +
                " CONSTRAINT [name] UNIQUE NONCLUSTERED \n" +
                "(\n" +
                "\t[name] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");
        sqlList.add(createColStr("sys_user", "scCode", "nvarchar(50)", null));//老师表添加字段




//sys_user_role
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'sys_user_role') " +
                "CREATE TABLE [dbo].[sys_user_role](\n" +
                "\t[uid] [int] NULL,\n" +
                "\t[rid] [nvarchar](20) NULL\n" +
                ") ON [PRIMARY]");

        //teacher
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'teacher')" +
                "CREATE TABLE [dbo].[teacher](\n" +
                "\t[te_no] [nvarchar](100) NOT NULL,\n" +
                "\t[loginName] [nvarchar](100) NULL,\n" +
                "\t[password] [nvarchar](100) NULL,\n" +
                "\t[name] [nvarchar](100) NULL,\n" +
                "\t[sex] [nvarchar](1) NULL,\n" +
                "\t[idCode] [nvarchar](200) NULL,\n" +
                "\t[phoneNo] [nvarchar](20) NULL,\n" +
                "\t[email] [nvarchar](100) NULL,\n" +
                "\t[sc_code] [nvarchar](100) NULL,\n" +
                "\t[isAdmin] [nvarchar](1) NULL,\n" +
                "\t[enable] [nvarchar](1) NULL,\n" +
                "\t[statusDate] [datetime] NULL,\n" +
                "\t[statusReason] [nvarchar](200) NULL,\n" +
                " CONSTRAINT [PK_teacher_1] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[te_no] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],\n" +
                " CONSTRAINT [loginName] UNIQUE NONCLUSTERED \n" +
                "(\n" +
                "\t[loginName] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");
        sqlList.add(createColStr("teacher", "isWxLogin", "nvarchar(100)", null));
        sqlList.add(createColStr("teacher", "isDelete", "nvarchar(1)", null));//老师表添加字段

        //school_wx表 by hzl 2019-9-7 09:18:20
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'school_wx')" +
                "CREATE TABLE [dbo].[school_wx](\n" +
                "\t[scCode] [nvarchar](100) NOT NULL,\n" +
                "\t[appId] [nvarchar](100) NULL,\n" +
                "\t[appSecret] [nvarchar](100) NULL,\n" +
                "\t[normalTmp] [nvarchar](100) NULL,\n" +
                "\t[ascTmp] [nvarchar](100) NULL,\n" +
                "\t[expiresTime] [datetime] NULL,\n" +
                "\t[token] [nvarchar](300) NULL,\n" +
                " CONSTRAINT [PK_school_wx] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[scCode] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        //教师班级通知号码绑定表 teacher_wx_info by hzl 2019-9-9 16:43:52
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'teacher_wx_info')" +
                "CREATE TABLE [dbo].[teacher_wx_info](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[clCode] [nvarchar](100) NULL,\n" +
                "\t[teNo] [nvarchar](100) NULL,\n" +
                "\t[phoneNo] [nvarchar](20) NULL,\n" +
                "\t[openId] [nvarchar](100) NULL,\n" +
                "\t[createDate] [datetime] NULL,\n" +
                "\t[updateDate] [datetime] NULL,\n" +
                " CONSTRAINT [PK_class_teacher_wx_bind] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]\n" +
                "ALTER TABLE [dbo].[teacher_wx_info]  WITH CHECK ADD  CONSTRAINT [FK_teacher_wx_info_class] FOREIGN KEY([clCode])\n" +
                "REFERENCES [dbo].[class] ([cl_code])\n" +
                "ALTER TABLE [dbo].[teacher_wx_info] CHECK CONSTRAINT [FK_teacher_wx_info_class]\n" +
                "ALTER TABLE [dbo].[teacher_wx_info]  WITH CHECK ADD  CONSTRAINT [FK_teacher_wx_info_teacher] FOREIGN KEY([teNo])\n" +
                "REFERENCES [dbo].[teacher] ([te_no])\n" +
                "ALTER TABLE [dbo].[teacher_wx_info] CHECK CONSTRAINT [FK_teacher_wx_info_teacher]\n");


        //config表 by hzl 2019-9-10 15:33:21
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'config')" +
                "CREATE TABLE [dbo].[config](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[cfDeId] [int] NULL,\n" +
                "\t[scCode] [nvarchar](100) NULL,\n" +
                "\t[value] [nvarchar](500) NULL,\n" +
                " CONSTRAINT [PK_config] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]\n" +
                "\n" +
                "ALTER TABLE [dbo].[config]  WITH CHECK ADD  CONSTRAINT [FK_config_config_define] FOREIGN KEY([cfDeId])\n" +
                "REFERENCES [dbo].[config_define] ([id])\n" +
                "\n" +
                "ALTER TABLE [dbo].[config] CHECK CONSTRAINT [FK_config_config_define]\n" +
                "\n" +
                "ALTER TABLE [dbo].[config]  WITH CHECK ADD  CONSTRAINT [FK_config_school] FOREIGN KEY([scCode])\n" +
                "REFERENCES [dbo].[school] ([sc_code])\n" +
                "\n" +
                "ALTER TABLE [dbo].[config] CHECK CONSTRAINT [FK_config_school]");

        //config_define by hzl 2019-9-10 15:33:21
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'config_define')" +
                "CREATE TABLE [dbo].[config_define](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[name] [nvarchar](30) NULL,\n" +
                "\t[code] [nvarchar](100) NULL,\n" +
                "\t[type] [nvarchar](30) NULL,\n" +
                "\t[selectValue] [nvarchar](200) NULL,\n" +
                "\t[remark] [nvarchar](100) NULL,\n" +
                " CONSTRAINT [PK_config_define] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");



        //config_define by xjp 2019-10-12 14:13:21
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'guadian_wx_log')" +
                "CREATE TABLE [dbo].[guadian_wx_log](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[sc_code] [nvarchar](100) NULL,\n" +
                "\t[stuid] [nvarchar](100) NULL,\n" +
                "\t[stuno] [nvarchar](100) NULL,\n" +
                "\t[stuname] [nvarchar](100) NULL,\n" +
                "\t[guaname] [nvarchar](100) NULL,\n" +
                "\t[wxno] [nvarchar](100) NULL,\n" +
                "\t[operate] [nvarchar](100) NULL,\n" +
                "\t[operatetime] [datetime] NULL,\n" +
                " CONSTRAINT [PK_guadian_wx_log] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");

        sqlList.add(createColStr("student", "faceImage1", "nvarchar(300)", null));//学生表增加人脸模板1路径字段 by hzl 2019-10-21 15:13:03
        sqlList.add(createColStr("student", "isBind", "nvarchar(1)", null));//学生表增加人脸模板1路径字段 by hzl 2019-10-21 15:13:03
        sqlList.add(createColStr("student", "faceImage2", "nvarchar(300)", null));//学生表增加人脸模板2路径字段 by hzl 2019-10-21 15:13:03
        sqlList.add(createColStr("student", "faceImage3", "nvarchar(300)", null));//学生表增加人脸模板3路径字段 by hzl 2019-10-21 15:13:03

        //学校数据库配置表 by hzl 2019-11-18 09:19:58
        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name= 'school_db')" +
                "CREATE TABLE [dbo].[school_db](\n" +
                "\t[scCode] [nvarchar](100) NOT NULL,\n" +
                "\t[url] [nvarchar](200) NULL,\n" +
                "\t[userName] [nvarchar](20) NULL,\n" +
                "\t[password] [nvarchar](20) NULL,\n" +
                "\t[code] [nvarchar](20) NULL,\n" +
                "\t[dbType] [nvarchar](20) NULL,\n" +
                " CONSTRAINT [PK_school_db] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[scCode] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");
    }

    /**
     * 创建列的sql
     *
     * @param tableName    表名
     * @param colName      列明
     * @param type         字段类型
     * @param defaultValue 默认值
     * @return
     */
    private String createColStr(String tableName, String colName, String type, String defaultValue) {
        String sql = "IF not EXISTS(SELECT 1 FROM SYSOBJECTS T1  \n" +
                "  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  \n" +
                " WHERE T1.NAME='" + tableName + "' AND T2.NAME='" + colName + "'  \n" +
                ")  \n" +
                "begin\n" +
                "ALTER TABLE " + tableName + " ADD " + colName + " " + type + "\n";
        if (defaultValue != null) {
            sql += "ALTER TABLE " + tableName + " ADD CONSTRAINT DF_" + tableName + "_" + colName + " DEFAULT " + defaultValue + " FOR " + colName + "\n";
        }
        sql += "end\n";
        return sql;
    }

    /**
     * 创建索引
     *
     * @param tableName
     * @param colName
     * @param indexName
     * @return
     */
    private String createIndexSql(String tableName, String colName, String indexName) {
        String sql = "if exists (SELECT   name   FROM   sysobjects   where   name= '" + tableName + "')\n" +
                "begin\n" +
                "IF EXISTS (SELECT * FROM SYSINDEXES WHERE NAME='" + indexName + "')\n" +
                "DROP INDEX " + tableName + "." + indexName + "\n" +
                "CREATE NONCLUSTERED INDEX " + indexName + "\n" +
                "ON " + tableName + "([" + colName + "]asc)  \n" +
                "WITH  (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                "end\n";
        return sql;
    }

}
