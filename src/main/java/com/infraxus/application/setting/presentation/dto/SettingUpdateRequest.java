package com.infraxus.application.setting.presentation.dto;

import com.infraxus.application.setting.domain.Setting;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettingUpdateRequest {
    private Integer cpuLimit;
    private Integer memoryLimit;
    private Integer storageLimit;

    @Builder
    public SettingUpdateRequest(Integer cpuLimit, Integer memoryLimit, Integer storageLimit) {
        this.cpuLimit = cpuLimit;
        this.memoryLimit = memoryLimit;
        this.storageLimit = storageLimit;
    }

    public Setting toEntity() {
        return Setting.builder()
                .cpuLimit(this.cpuLimit)
                .memoryLimit(this.memoryLimit)
                .storageLimit(this.storageLimit)
                .build();
    }
}
