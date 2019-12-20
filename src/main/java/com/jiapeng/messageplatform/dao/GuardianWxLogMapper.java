package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.GuadianWxLog;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface GuardianWxLogMapper {

    int insert(GuadianWxLog record);

    List<GuadianWxLog> selectRecord(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize,
                                    @Param("scCode") String scCode, @Param("stuNo") String stuNo,@Param("stuName") String stuName,
                                    @Param("startDate") String startDate, @Param("endDate") String endDate);

    int selectRecordCount(@Param("scCode") String scCode, @Param("stuNo") String stuNo,@Param("stuName") String stuName,
                          @Param("startDate") String startDate, @Param("endDate") String endDate);


}