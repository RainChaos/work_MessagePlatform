//package com.jiapeng.messageplatform.listener;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.jiapeng.messageplatform.service.TeacherService;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * created by lc 老师同步监听
// */
////@Component
//public class TeacherListener {
//
//    @Autowired
//    private TeacherService teacherService;
//
//    @RabbitListener(
//            bindings = @QueueBinding(value = @Queue(value = "${queue.teacher.name}", durable = "true"),//定义队列，客户端定义
//                    exchange = @Exchange(value = "${exchange.teacher}", type = ExchangeTypes.FANOUT)//绑定交换机，交换机由服务端定义
//            )
//    )
//    public void studentListener(Channel channel, @Payload Message message) throws IOException {
//        String content = new String(message.getBody());//取出数据
//        System.out.println(content);
//        //通知服务端数据消费成功，不执行回调会导致堆积的消息会越来越多。消费者重启后会重复消费这些消息并重复执行业务逻辑。
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
//        //do something
//        teacher(content);
//    }
//
//    public void teacher(String content) {
//        JSONObject data = JSON.parseObject(content);
//        //姓名
//        String name = data.get("name") == null ? null : data.get("name").toString();
//        //教师号
//        String teacherid = data.get("teacherid") == null ? "" : data.get("teacherid").toString();
//        //教师号为空后面不用执行了
//        if (teacherid == null) {
//            return;
//        }
//        //性别
//        String sex = data.get("sex") == null ? null : data.get("sex").toString();
//        if ("1".equals(sex)) {
//            sex = "0";
//        }
//        if ("2".equals(sex)) {
//            sex = "1";
//        }
//        //身份证
//        String idcard = data.get("idcard") == null ? null : data.get("idcard").toString();
//        //手机号
//        String phone = data.get("phone") == null ? null : data.get("phone").toString();
//        //密码
//        String password = data.get("password") == null ? null : data.get("password").toString();
//        //学校编码
//        String sc_code = data.get("sc_code") == null ? null : data.get("sc_code").toString();
//        //使用否
//        String used = data.get("used") == null ? null : data.get("used").toString();
//        //删除否
//        String delete = data.get("delete") == null ? null : data.get("delete").toString();
//        teacherService.teacherHandle(teacherid, password, name, sex, idcard, phone, sc_code, used, delete);
//    }
//}
