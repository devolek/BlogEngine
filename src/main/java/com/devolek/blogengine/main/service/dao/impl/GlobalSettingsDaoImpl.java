package com.devolek.blogengine.main.service.dao.impl;

import com.devolek.blogengine.main.model.GlobalSetting;
import com.devolek.blogengine.main.repo.GlobalSettingRepository;
import com.devolek.blogengine.main.service.dao.GlobalSettingsDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GlobalSettingsDaoImpl implements GlobalSettingsDao {

    private final GlobalSettingRepository globalSettingRepository;

    public GlobalSettingsDaoImpl(GlobalSettingRepository globalSettingRepository) {
        this.globalSettingRepository = globalSettingRepository;
    }

    @Override
    public Map<String, Boolean> getSettings() {
        HashMap<String, Boolean> model = new HashMap<>();
        Iterable<GlobalSetting> iterable = globalSettingRepository.findAll();
        for (GlobalSetting globalSetting : iterable) {
            model.put(globalSetting.getCode(), globalSetting.getValue().equals("YES"));
        }
        return model;
    }

    @Override
    public void setSettings(Map<String, Object> model) {
        for (String code : model.keySet()) {
            Optional<GlobalSetting> setting = globalSettingRepository.findByCode(code);
            if (setting.isPresent()) {
                GlobalSetting globalSetting = setting.get();
                globalSetting.setValue(model.get(code).equals(true) ? "YES" : "NO");
                globalSettingRepository.save(globalSetting);
            }
        }
    }
}
