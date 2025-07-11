package com.infraxus.application.alram.alram.service;

import com.infraxus.application.alram.alram.domain.Alarm;
import com.infraxus.application.alram.alram.domain.value.AlarmId;
import com.infraxus.application.alram.alram.presentation.dto.AlarmCreateRequest;
import com.infraxus.application.alram.alram.presentation.dto.AlarmUpdateRequest;
import com.infraxus.application.alram.alram.service.implementation.AlarmCreator;
import com.infraxus.application.alram.alram.service.implementation.AlarmDeleter;
import com.infraxus.application.alram.alram.service.implementation.AlarmReader;
import com.infraxus.application.alram.alram.service.implementation.AlarmUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandAlarmService {

    private final AlarmCreator alarmCreator;
    private final AlarmUpdater alarmUpdater;
    private final AlarmDeleter alarmDeleter;
    private final AlarmReader alarmReader;
    private final QueryAlarmService queryAlarmService;

    public void createAlarm(AlarmCreateRequest request){
        alarmCreator.save(request.toEntity());
    }

    public void updateAlarm(AlarmId alarmId, AlarmUpdateRequest request){
        Alarm alarm = queryAlarmService.getAlarmById(alarmId);
        alarmUpdater.update(alarm, request.toEntity());
    }

    public void deleteAlarm(AlarmId alarmId){
        Alarm alarm = queryAlarmService.getAlarmById(alarmId);
        alarmDeleter.delete(alarm);
    }

}
