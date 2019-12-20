package com.jiapeng.messageplatform.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiapeng.messageplatform.entity.GradeEntity;
import com.jiapeng.messageplatform.service.ClassService;
import com.jiapeng.messageplatform.service.GradeService;
import com.jiapeng.messageplatform.service.SchoolService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


//@Component
public class OrganizeListener {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private ClassService classService;
    @Autowired
    private SchoolService schoolService;

    @RabbitListener(
        bindings = @QueueBinding(value = @Queue(value = "${queue.organize.name}", durable = "true"),//定义队列，客户端定义
                exchange = @Exchange(value ="${exchange.organize}", type = ExchangeTypes.FANOUT)//绑定交换机，交换机由服务端定义
        )
    )
        public void organizeListener(Channel channel, @Payload Message message) throws Exception {
            String content = new String(message.getBody());//取出数据
        System.out.println(content);
        //通知服务端数据消费成功，不执行回调会导致堆积的消息会越来越多。消费者重启后会重复消费这些消息并重复执行业务逻辑。
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        //do something
        organize(content);
    }

    /**
     * 单位结构更新处理
     * @param content
     * @throws Exception
     */
    public void organize(String content) throws Exception {
        JSONObject data = JSON.parseObject(content);
        String type = data.get("type") == null ? "" : data.get("type").toString();
        String code = data.get("unitid") == null ? "" : data.get("unitid").toString();
        String name = data.get("name") == null ? "" : data.get("name").toString();
        String sc_code = data.get("sc_code") == null ? "" : data.get("sc_code").toString();
        String gr_code = data.get("punitid") == null ? "" : data.get("punitid").toString();
        String delete = data.get("delete") == null ? "" : data.get("delete").toString();

        switch (type){
            case "1":
                //类型为学校
                System.out.println("修改学校信息");
                schoolService.schoolHandle(code, name, delete);
                break;
            case "5":
                //类型为年级
                System.out.println("修改年级信息");
                gradeService.gradeHandle(sc_code,code, name, delete);
                break;
            case "6":
                //类型为班级
                System.out.println("修改班级信息");
                classService.classHandle(sc_code, gr_code, code, name, delete);
                break;
            default:
                break;
        }
    }

}
