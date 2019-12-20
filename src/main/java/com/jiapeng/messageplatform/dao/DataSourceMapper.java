package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.DataSource;
import com.jiapeng.messageplatform.entity.SchoolDB;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @auther zhoupan
 * @date 2019/4/8 21:12
 * @info
 */
public interface DataSourceMapper {

    @Select("SELECT * FROM school_db")
    List<SchoolDB> get();

    @Select("SELECT * FROM school_db where scCode = #{scCode}")
    SchoolDB getByScCode(String scCode);

}
