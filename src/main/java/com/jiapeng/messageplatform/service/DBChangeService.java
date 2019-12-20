package com.jiapeng.messageplatform.service;

/**
 * Created by HZL on 2019/11/6.
 */
public interface DBChangeService {
    /**
     * 切换到学校数据库
     * by hzl 2019-11-6 17:16:51
     *
     * @param scCode
     * @return
     * @throws Exception
     */
    boolean changeDb(String scCode) throws Exception;

    /**
     * 切回主数据源
     * by hzl 2019-11-6 17:23:12
     */
    void backMainDb();

}
