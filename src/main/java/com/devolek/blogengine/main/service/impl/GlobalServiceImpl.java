package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.dto.response.universal.UniversalResponseFactory;
import com.devolek.blogengine.main.service.GlobalService;
import com.devolek.blogengine.main.service.dao.GlobalSettingsDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@AllArgsConstructor
@Service
public class GlobalServiceImpl implements GlobalService {
    private final GlobalSettingsDao globalSettingsDao;

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
