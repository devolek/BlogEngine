package com.devolek.blogengine.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
//                .addResourceLocations("file://" + uploadPath + "/upload/"); //для деплоя на серв
                .addResourceLocations("file:" + uploadPath + "/upload/"); //для локального хранилища
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
