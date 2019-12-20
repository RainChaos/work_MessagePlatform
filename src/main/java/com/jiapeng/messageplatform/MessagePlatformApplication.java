package com.jiapeng.messageplatform;

import com.jiapeng.messageplatform.service.SchoolService;
import com.jiapeng.messageplatform.utils.SessionUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@MapperScan(value = {"com.jiapeng.messageplatform.dao"})
@EnableTransactionManagement
public class MessagePlatformApplication {
	public static void main(String[] args)
	{
		SpringApplication.run(MessagePlatformApplication.class, args);
	}




}
