package com.jiapeng.messageplatform.dbdynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by HZL on 2019/11/6.
 */
@Service
public class DBContextHolder {
    private final static Logger log = LoggerFactory.getLogger(DBContextHolder.class);
    // 对当前线程的操作-线程安全的
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    // 调用此方法，切换数据源
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
//        clearDataSource();
        log.info("已切换到数据源:{}", dataSource);
    }

    // 获取数据源
    public static String getDataSource() {
        return contextHolder.get();
    }

    // 删除数据源
    public static void clearDataSource() {
        contextHolder.remove();
        log.info("已切换到主数据源");
    }

//    private static String contextHolder;
//
//    // 调用此方法，切换数据源
//    public static void setDataSource(String dataSource) {
//        contextHolder = dataSource;
//        log.info("已切换到数据源:{}", dataSource);
//    }
//
//    // 获取数据源
//    public static String getDataSource() {
//        return contextHolder;
//    }
//
//    // 删除数据源
//    public static void clearDataSource() {
//        contextHolder = null;
//        log.info("已切换到主数据源");
//    }
}
