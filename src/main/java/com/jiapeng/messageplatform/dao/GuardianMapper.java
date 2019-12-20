package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.Guardian;
import com.jiapeng.messageplatform.entity.WxBandEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface GuardianMapper {
    int insert(Guardian record);

    int insertSelective(Guardian record);

    void updateByPrimaryKeySelective(Guardian record);

    @Delete("delete from guardian where id = #{id}")
    void delete(@Param("id") Integer id);

    @Delete("delete from guardian where stuId = #{stuId}")
    void deleteByStuNo(@Param("stuId") String stuId);

    List<WxBandEntity>  find(@Param("stuId") String stuId, @Param("wxNo") String wxNo);

    //

    @Select("select * from guardian g " +
            "left join student s on g.stuId = s.stuId " +
            "left join class cla on cla.cl_code = s.cl_code " +
            "where cla.sc_code = #{scCode} and s.stuNo = #{stuNo}  and s.stuName = #{stuName} and g.guaName = #{guaName}")
    Guardian findPhoneByKey(@Param("scCode") String scCode,@Param("stuNo") String stuNo,@Param("stuName") String stuName,@Param("guaName") String guaName);


    @Update("update guardian set wxNo='' where id=#{id} and wxNo=#{wxNo}")
    void updateByStuAndGuaname(@Param("wxNo") String wxNo, @Param("id") Integer id);


    @Delete("delete from guardian where id = #{id} and guaName =#{guaName} ")
    void deleteGuardian(@Param("id") Integer id, @Param("guaName") String guaName);

    @Select(" select * from guardian where stuId = #{stuId} order by wxNo desc")
    List<Guardian> listByStuId(String stuId);

    @Select(" select * from guardian where id = #{id}")
    Guardian getGuadianById(String id);

    @Select("select * from guardian where stuId = #{stuId} and guaName = #{guaName}")
    Guardian findByStuIdAndGuaName(@Param("stuId") String stuId, @Param("guaName") String guaName);

}