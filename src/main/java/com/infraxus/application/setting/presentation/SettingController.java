package com.infraxus.application.setting.presentation;

import com.infraxus.application.setting.domain.Setting;
import com.infraxus.application.setting.presentation.dto.SettingCreateRequest;
import com.infraxus.application.setting.presentation.dto.SettingUpdateRequest;
import com.infraxus.application.setting.service.CommandSettingService;
import com.infraxus.application.setting.service.QuerySettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class SettingController {

    private final CommandSettingService commandSettingService;
    private final QuerySettingService querySettingService;

//    @PostMapping
//    public ResponseEntity<Void> createSetting(@RequestBody SettingCreateRequest request) {
//        commandSettingService.createSetting(request);
//        return ResponseEntity.ok().build();
//    }

    @PutMapping("/{settingId}")
    public ResponseEntity<Void> updateSetting(@PathVariable UUID settingId, @RequestBody SettingUpdateRequest request) {
        commandSettingService.updateSetting(settingId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Setting>> getAllSettings() {
        return ResponseEntity.ok(querySettingService.findAll());
    }

//    @GetMapping("/{settingId}")
//    public ResponseEntity<Setting> getSettingById(@PathVariable UUID settingId) {
//        return ResponseEntity.ok(querySettingService.findById(settingId));
//    }
}
