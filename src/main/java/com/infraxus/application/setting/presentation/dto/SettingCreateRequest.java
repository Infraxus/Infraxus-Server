package com.infraxus.application.setting.presentation.dto;

import com.infraxus.application.setting.domain.Setting;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class SettingCreateRequest {
    private Integer cpuLimit;
    private Integer memoryLimit;
    private Integer storageLimit;

    @Builder
    public SettingCreateRequest(Integer cpuLimit, Integer memoryLimit, Integer storageLimit) {
        this.cpuLimit = cpuLimit;
        this.memoryLimit = memoryLimit;
        this.storageLimit = storageLimit;
    }

    public Setting toEntity() {
        return Setting.builder()
                .settingId(UUID.randomUUID())
                .cpuLimit(this.cpuLimit)
                .memoryLimit(this.memoryLimit)
                .storageLimit(this.storageLimit)
                .build();
    }
}
