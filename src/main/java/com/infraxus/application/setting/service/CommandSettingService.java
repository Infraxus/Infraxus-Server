package com.infraxus.application.setting.service;

import com.infraxus.application.setting.domain.Setting;
import com.infraxus.application.setting.presentation.dto.SettingCreateRequest;
import com.infraxus.application.setting.presentation.dto.SettingUpdateRequest;
import com.infraxus.application.setting.service.implementation.SettingCreator;
import com.infraxus.application.setting.service.implementation.SettingReader;
import com.infraxus.application.setting.service.implementation.SettingUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandSettingService {

    private final SettingCreator settingCreator;
    private final SettingUpdater settingUpdater;
    private final SettingReader settingReader;
    private final QuerySettingService querySettingService;

    public void createSetting(SettingCreateRequest request){
        settingCreator.save(request.toEntity());
    }

    public void updateSetting(UUID settingId, SettingUpdateRequest request){
        Setting setting = querySettingService.getSettingById(settingId);
        settingUpdater.update(setting, request.toEntity());
    }

}

