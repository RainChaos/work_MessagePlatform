package com.jiapeng.messageplatform.dao;

import org.apache.ibatis.annotations.Param;
/**
 * Created by Administrator on 2019/9/4.
 */
public interface UpdateDao  {
    //执行一个建表SQL语句
    public void executeSql(@Param("sql") String sql);
    /**
     *  判断数据库是否存在，是返回1，否返回0
     */

}
