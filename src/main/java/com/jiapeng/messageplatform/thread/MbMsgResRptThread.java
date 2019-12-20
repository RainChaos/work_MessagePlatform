package com.jiapeng.messageplatform.thread;

import com.jiapeng.messageplatform.service.MbMsgService;
import com.jiapeng.messageplatform.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import webServices.MTReport;
import webServices.MTResponse;

import java.util.List;

/**
 * 用于定时获取电信接口消息
 * Created by HZL on 2019-8-31
 */
@Component
@EnableScheduling
public class MbMsgResRptThread {
    @Autowired
    MbMsgService mbMsgService;
    @Autowired
    MessageService messageService;

    private static final Logger logger = LoggerFactory.getLogger(MbMsgResRptThread.class);

    /**
     * 获取短信提交至运营商的提交响应
     * 提交运营商网关的响应报告，用于判断是否成功提交到运营商行业网关
     */
    @Scheduled(cron = "${config.mbmsg.res.cron}")
    public void getResponse() {
        logger.info("BEGIN......获取Response信息>>>>>>>>>>>>>>>>>>>>>");
        try {
            List<MTResponse> responseList = mbMsgService.getResponse(500);
            messageService.mbMsgUpdateStateByRes(responseList);
        } catch (Exception e) {
            logger.info("ERROR....：" + e.getMessage());
        } finally {
            logger.info("END.....获取Response信息>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    /**
     * 获取短信下发用户的状态报告
     * 提交手机的响应报告，用于，用于判断是否成功下发到用户手机
     */
    @Scheduled(cron = "${config.mbmsg.rpt.cron}")
    public void getReport() {
        logger.info("BEGIN......获取Report信息>>>>>>>>>>>>>>>>>>>>>");
        try {
            List<MTReport> responseList = mbMsgService.getReport(500);
            messageService.mbMsgUpdateStateByRpt(responseList);
        } catch (Exception e) {
            logger.info("ERROR....：" + e.getMessage());
        } finally {
            logger.info("END.....获取Report信息>>>>>>>>>>>>>>>>>>>>>");
        }
    }

}
