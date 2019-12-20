//package com.jiapeng.messageplatform.listener;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.jiapeng.messageplatform.entity.Student;
//import com.jiapeng.messageplatform.service.StudentService;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Controller;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * Created BY LC 学生同步监听
// */
////@Controller
//public class StudentListener {
//
//    @Autowired
//    private StudentService studentService;
//
//    @RabbitListener(
//            bindings = @QueueBinding(value = @Queue(value = "${queue.student.name}", durable = "true"),//定义队列，客户端定义
//                    exchange = @Exchange(value ="${exchange.student}", type = ExchangeTypes.FANOUT)//绑定交换机，交换机由服务端定义
//            )
//    )
//    public void studentListener(Channel channel, @Payload Message message) throws IOException {
//        String content = new String(message.getBody());//取出数据
//        System.out.println(content);
//        //通知服务端数据消费成功，不执行回调会导致堆积的消息会越来越多。消费者重启后会重复消费这些消息并重复执行业务逻辑。
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
//        //do something
//        student(content);
//    }
//
//    public void student(String content) {
//        JSONObject data = JSON.parseObject(content);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
//
//
//        //学号
//        String studentid = data.get("studentid") == null ? null : data.get("studentid").toString();
//        //学号为空后面不用执行了
//        if (studentid==null){
//            return;
//        }
//        //学校编码
//        String schoolcode  = data.get("sc_code") == null?"":data.get("sc_code").toString();
//        //姓名
//        String name = data.get("name") == null ? null : data.get("name").toString();
//        //班级
//        String unitid = data.get("unitid") == null ? null : data.get("unitid").toString();
//        //性别
//        String sex = data.get("sex") == null ? null : data.get("sex").toString();
//        if ("1".equals(sex)){
//            sex="0";
//        }
//        if ("2".equals(sex)){
//            sex="1";
//        }
//        //身份证
//        String idcard = data.get("idcard") == null ? null : data.get("idcard").toString();
//
//        //是否删除
//        String delete = data.get("delete") == null ? null : data.get("delete").toString();
//        //更新时间
//        studentService.studentHandle(schoolcode,studentid,name,sex,idcard,unitid,delete);
//
//    }
//}
