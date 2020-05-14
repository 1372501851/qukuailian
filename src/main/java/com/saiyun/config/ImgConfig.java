package com.saiyun.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ImgConfig extends WebMvcConfigurerAdapter {
    @Value("${upload.config.hard-disk}")
    private String hardDisk;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /**
         * 资源映射路径
         * addResourceHandler：访问映射路径
         * addResourceLocations：资源绝对路径
         */
        registry.addResourceHandler("/static/img/**").addResourceLocations("file:"+hardDisk);
    }
}
