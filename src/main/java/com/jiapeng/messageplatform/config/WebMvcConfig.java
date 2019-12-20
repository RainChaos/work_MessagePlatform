package com.jiapeng.messageplatform.config;

import com.jiapeng.messageplatform.interceptor.DBChangeInterceptor;
import com.jiapeng.messageplatform.interceptor.ErrorRedirectInterceptor;
import com.jiapeng.messageplatform.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Web相关配置
 *
 * @author zifangsky
 * @date 2018/7/9
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //设置允许跨域 by hzl 2019-10-28 11:42:28
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); // 允许任何头
        corsConfiguration.addAllowedMethod("*"); // 允许任何方法（post、get等）
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 对接口配置跨域设置
        return new CorsFilter(source);
    }

    /**
     * 视图控制器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index");
    }


    @Value("${spring.mvc.static-path-pattern}")
    private String staticResourceUrl;

    //需要在这里注入，下面.addInterceptor()才不会null by hzl 2019-11-8 15:35:38
    @Bean
    public HandlerInterceptor getDBChangeInterceptor() {
        return new DBChangeInterceptor();
    }


    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //给教师端加上登录拦截方法 by hzl 2019-8-22 16:22:33
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new ErrorRedirectInterceptor()).addPathPatterns("/error/**");
//        数据库切换方法 by hzl 2019-11-8 15:01:48
        registry.addInterceptor(getDBChangeInterceptor()).addPathPatterns("/**").excludePathPatterns("/login");
    }
    @Value("${system.config.downurl}")
    private String downUrl;
    @Value("${system.config.downpath}")
    private String downPath;

    @Value("${config.student.img.url}")
    private String stuImgUrl;
    @Value("${config.student.img.path}")
    private String stuImgPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String staticMapping = downUrl + "/**";
        String localDirectory = "file:" + downPath;
        registry.addResourceHandler(staticMapping).addResourceLocations(localDirectory);
        registry.addResourceHandler(stuImgUrl + "/**").addResourceLocations("file:" + stuImgPath);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

}
