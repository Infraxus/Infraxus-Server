package com.infraxus.application.setting.service;

import com.infraxus.application.setting.domain.Setting;
import com.infraxus.application.setting.exception.SettingNotFoundException;
import com.infraxus.application.setting.service.implementation.SettingReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuerySettingService {

    private final SettingReader settingReader;

    public List<Setting> findAll(){
        return settingReader.findAll();
    }

    public Setting findById(UUID id){
        return settingReader.findById(id);
    }

    public Setting getSettingById(UUID settingId) {
        Setting setting = settingReader.findById(settingId);
        if (setting == null) {
            throw new SettingNotFoundException("Setting with ID " + settingId + " not found.");
        }
        return setting;
    }
}
