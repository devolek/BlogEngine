package com.devolek.blogengine.main.service.dao;

import java.util.Map;

public interface GlobalSettingsDao {
    Map<String, Object> getSettings();

    void setSettings(Map<String, Object> model);
}
