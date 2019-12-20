package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.ClassTeacher;
import com.jiapeng.messageplatform.entity.SchoolIP;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by HZL on 2019/8/7.
 */
public interface SchoolIpMapper {

    @Insert("insert into school_ip(sc_code,ip_pass) " +
            "values(#{sc_code},#{ipAddr})")
    int insert(@Param("sc_code") String sc_code, @Param("ipAddr") String ipAddr);

    @Delete("delete school_ip where sc_code=#{sc_code} ")
    void del(String sc_code);


    @Select("select * from school_ip where sc_code=#{sc_code} and ip_pass like  CONCAT('%',#{ipAddr},'%') ")
    SchoolIP whiteListCheck(@Param("sc_code") String sc_code, @Param("ipAddr") String ipAddr);

    @Select("select * from school_ip where sc_code=#{sc_code}")
    List<SchoolIP> getIPListByScCode(@Param("sc_code") String sc_code);


}
