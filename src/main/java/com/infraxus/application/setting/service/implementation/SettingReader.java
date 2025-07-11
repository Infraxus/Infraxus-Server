package com.infraxus.application.setting.service.implementation;

import com.infraxus.application.setting.domain.Setting;
import com.infraxus.application.setting.domain.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SettingReader {

    private final SettingRepository settingRepository;

    public List<Setting> findAll(){
        return settingRepository.findAll();
    }

    public Setting findById(UUID id){
        return settingRepository.findById(id)
                .orElse(null);
    }

}
