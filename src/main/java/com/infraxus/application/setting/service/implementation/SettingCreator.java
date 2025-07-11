package com.infraxus.application.setting.service.implementation;

import com.infraxus.application.setting.domain.Setting;
import com.infraxus.application.setting.domain.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingCreator {

    private final SettingRepository settingRepository;

    public void save(Setting setting){
        settingRepository.save(setting);
    }

}
