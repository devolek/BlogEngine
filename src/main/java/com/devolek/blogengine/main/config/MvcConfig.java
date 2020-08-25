package com.devolek.blogengine.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;
    @Value("${db.pathToRootJks}")
    private String pathToRootJks;
    @Value("${db.rootJksPassword}")
    private String rootJksPassword;

    @PostConstruct
    public void init() {
        System.setProperty("javax.net.ssl.trustStore", pathToRootJks);
        System.setProperty("javax.net.ssl.trustStorePassword", rootJksPassword);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
//                .addResourceLocations("file://" + uploadPath + "/upload/"); //для деплоя на серв
                .addResourceLocations("file:" + uploadPath + "/upload/"); //для локального хранилища
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
