package com.infraxus.application.alarm.alarm.service;

import com.infraxus.application.alarm.alarm.domain.Alarm;
import com.infraxus.application.alarm.alarm.domain.value.AlarmId;
import com.infraxus.application.alarm.alarm.presentation.dto.AlarmUpdateRequest;
import com.infraxus.application.alarm.alarm.service.implementation.AlarmReader;
import com.infraxus.application.alarm.alarm.service.implementation.AlarmUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandAlarmService {

    private final AlarmUpdater alarmUpdater;
    private final AlarmReader alarmReader;
    private final QueryAlarmService queryAlarmService;

    public void updateAlarm(AlarmId alarmId, AlarmUpdateRequest request){
        Alarm alarm = queryAlarmService.getAlarmById(alarmId);
        alarmUpdater.update(alarm, request.toEntity());
    }

}
