package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.ClassEntity;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ScoresMapper {

    @SelectProvider(type = ScoresProvider.class, method = "listSql")
    List<Map<String, Object>> list(@Param("tb_name") String tb_name,@Param("cl_code") List<String> cl_code,@Param("stuId") String stuId,@Param("term") String term, @Param("limit") int rows, @Param("page") int page);


    @SelectProvider(type = ScoresProvider.class, method = "countSql")
    int count(@Param("tb_name") String tb_name,@Param("cl_code") List<String> cl_code, @Param("stuId") String stuId, @Param("term") String term);


    @SelectProvider(type = ScoresProvider.class, method = "getRows")
     List<String> getRows(@Param("tb_name") String tb_name);

    @SelectProvider(type = ScoresProvider.class, method = "getRowIsExist")
    String getRowIsExist(@Param("tb_name") String tb_name,@Param("rows") String rows);
}
