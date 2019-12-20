package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.SchoolDB;
import com.jiapeng.messageplatform.entity.SchoolEntity;
import com.jiapeng.messageplatform.entity.SchoolIP;
import com.jiapeng.messageplatform.entity.SchoolWX;
import com.jiapeng.messageplatform.utils.DataGridJson;
import me.chanjar.weixin.mp.api.WxMpService;

import java.util.List;
import java.util.Map;

public interface SchoolService {


    SchoolEntity getByScCode(String scCode);

    void add(SchoolEntity entity);

    void addSchollDB(SchoolDB entity);

    void update(SchoolEntity entity);

    void setMenhuKey(SchoolEntity entity);

    void del(String sc_code) throws Exception;

    DataGridJson list(String scCode, String key, int rows, int page);

    DataGridJson listSchoolDB(int rows, int page);



    void setStatus(SchoolEntity entity);

    void updateToken(String sc_code);

    List<Map<String, Object>> listGradeTree(String sc_code, String teNo, String requestType);


    //by xie
    void whiteListCheck(String scCode, String ipAddr) throws Exception;

    void setIP(String scCode, String ipAddr);

    List<SchoolIP> getIPListByScCode(String scCode);

    //添加学校微信配置信息 by hzl 2019-9-6 15:31:28
    void addSchoolWx(SchoolWX schoolWX) throws Exception;

    //修改学校微信配置信息 by hzl 2019-9-6 15:31:28
    void updateSchoolWx(SchoolWX schoolWX) throws Exception;

    //获取学校微信配置信息 by hzl 2019-9-6 15:31:28
    SchoolWX loadSchoolWX(String scCode) throws Exception;

    //学校信息处理
    void schoolHandle(String sc_code, String name, String delete);

    //region 学校数据源管理

    //修改学校数据库配置信息 by hzl 2019-11-6 17:22:31
    void updateSchoolDB(SchoolDB schoolDB) throws Exception;

    //获取学校数据库配置信息 by hzl 2019-11-6 17:25:11
    SchoolDB loadSchoolDB(String scCode) throws Exception;

    //endregion
}

