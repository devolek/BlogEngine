package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.universal.Response;

import java.util.Map;

public interface GlobalService {
    Map<String,Object> getSettings();
    Response setSettings(Map<String, Object> model);
}
