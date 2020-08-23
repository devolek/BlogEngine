package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.response.universal.InfoResponse;
import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.dto.response.universal.UniversalResponseFactory;
import com.devolek.blogengine.main.service.GlobalService;
import com.devolek.blogengine.main.service.dao.GlobalSettingsDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GlobalServiceImpl implements GlobalService {
    private final GlobalSettingsDao globalSettingsDao;

    @Value("${project.info.title}")
    private String title;
    @Value("${project.info.subtitle}")
    private String subtitle;
    @Value("${project.info.phone}")
    private String phone;
    @Value("${project.info.email}")
    private String email;
    @Value("${project.info.copyright}")
    private String copyright;
    @Value("${project.info.copyrightFrom}")
    private String copyrightFrom;

    public GlobalServiceImpl(GlobalSettingsDao globalSettingsDao) {
        this.globalSettingsDao = globalSettingsDao;
    }

    @Override
    public InfoResponse getInfo() {
        return new InfoResponse(title, subtitle, phone, email, copyright, copyrightFrom);
    }

    @Override
    public Map<String, Boolean> getSettings() {
        return globalSettingsDao.getSettings();
    }

    @Override
    public Response setSettings(Map<String, Object> model) {
        globalSettingsDao.setSettings(model);
        return UniversalResponseFactory.getTrueResponse();
    }
}
