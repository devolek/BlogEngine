package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.response.universal.OkResponse;
import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.service.GlobalService;
import com.devolek.blogengine.main.service.dao.GlobalSettingsDao;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GlobalServiceImpl implements GlobalService {
    private final GlobalSettingsDao globalSettingsDao;

    public GlobalServiceImpl(GlobalSettingsDao globalSettingsDao) {
        this.globalSettingsDao = globalSettingsDao;
    }

    @Override
    public Map<String, Boolean> getSettings() {
        return globalSettingsDao.getSettings();
    }

    @Override
    public Response setSettings(Map<String, Object> model) {
        globalSettingsDao.setSettings(model);
        return new OkResponse();
    }
}
