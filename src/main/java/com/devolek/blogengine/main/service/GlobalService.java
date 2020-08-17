package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.response.universal.Response;

import java.util.Map;

public interface GlobalService {
    Map<String,Boolean> getSettings();
    Response setSettings(Map<String, Object> model);
}
