package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.SchoolDBMapper;
import com.jiapeng.messageplatform.dbdynamic.DBContextHolder;
import com.jiapeng.messageplatform.dbdynamic.DynamicDataSource;
import com.jiapeng.messageplatform.entity.SchoolDB;
import com.jiapeng.messageplatform.service.DBChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by HZL on 2019/11/6.
 */
@Service
public class DBChangeServiceImpl implements DBChangeService {
    @Autowired
    SchoolDBMapper schoolDBMapper;
    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Override
    public boolean changeDb(String scCode) throws Exception {
        //默认切换到主数据源,进行整体资源的查找
        DBContextHolder.clearDataSource();
        SchoolDB dataSource = schoolDBMapper.selectByPrimaryKey(scCode);
        if (null != dataSource) {
            System.out.println("需要使用的的数据源已经找到,datasourceId是：" + dataSource.getScCode());
            //创建数据源连接&检查 若存在则不需重新创建
            dynamicDataSource.createDataSourceWithCheck(dataSource);
            //切换到该数据源
            DBContextHolder.setDataSource(dataSource.getScCode());

            return true;
        }
        return false;
    }

    @Override
    public void backMainDb() {
        //切回主数据源
        DBContextHolder.clearDataSource();
    }
}
