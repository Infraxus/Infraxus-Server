package com.infraxus.application.setting.service.implementation;

import com.infraxus.application.setting.domain.Setting;
import com.infraxus.application.setting.domain.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingUpdater {

    private final SettingRepository settingRepository;

    public void update(Setting updatableSetting, Setting newSettingData){
        Setting updated = updatableSetting.toBuilder()
                .settingId(updatableSetting.getSettingId())
                .cpuLimit(newSettingData.getCpuLimit())
                .memoryLimit(newSettingData.getMemoryLimit())
                .gpuLimit(newSettingData.getGpuLimit())
                .storageLimit(newSettingData.getStorageLimit())
                .build();

        settingRepository.save(updated);
    }

}
