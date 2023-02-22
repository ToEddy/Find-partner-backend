//package com.findPartner.common.config;
//
//import com.findPartner.common.Interceptor.LoginInterceptor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.Resource;
//
//
///**
// * @author eddy
// * @createTime 2023/2/11
// */
//@Configuration
//@EnableWebMvc
//@Slf4j
//public class SpringMVCConfig implements WebMvcConfigurer {
//    @Resource
//    private LoginInterceptor loginInterceptor;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 下面定义了拦截器，会导致 spring.resources.static-locations 配置失效
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/webapp/public/");
//        // 配置 knife4j 文档资源的访问路径
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        try {
//            // 设置ThreadLocal拦截器
//            registry.addInterceptor(loginInterceptor)
//                    .addPathPatterns("/**") //拦截所有的请求
//                    .excludePathPatterns("/login/**")
//                    .excludePathPatterns("/searchByName/**")
//                    .excludePathPatterns("/searchByTags/**")
//                    .excludePathPatterns("/recommend/**")
//                    .excludePathPatterns("/register/**");
//            WebMvcConfigurer.super.addInterceptors(registry);
//        } catch (Exception e) {
//            log.error("err", e);
//        }
//    }
//}

