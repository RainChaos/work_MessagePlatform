package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.Config;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Config record);

    int insertSelective(Config record);

    Config selectByPrimaryKey(Integer id);

    @Select("select  c.* " +
            "from config c " +
            "LEFT JOIN config_define cd ON  c.cfDeId=cd.id " +
            "LEFT JOIN school s on c.scCode=s.sc_code " +
            "where cd.code = #{cfCode} and s.sc_code = #{scCode}")
    Config selectByScCodeAndCfCode(@Param("scCode") String scCode, @Param("cfCode") String cfCode);

    int updateByPrimaryKeySelective(Config record);

    int updateByPrimaryKey(Config record);

    List<Config> list(@Param("scCode") String scCode, @Param("cfDeId") Integer cfDeId);
}