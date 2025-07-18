package com.infraxus.application.alarm.alarm.service.implementation;

import com.infraxus.application.alarm.alarm.domain.Alarm;
import com.infraxus.application.alarm.alarm.domain.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmUpdater {

    private final AlarmRepository alarmRepository;

    public void update(Alarm updatableAlarm, Alarm newAlarmData){
        Alarm updated = updatableAlarm.toBuilder()
                .alarmId(updatableAlarm.getAlarmId())
                .totalCriticalAlert(newAlarmData.getTotalCriticalAlert())
                .totalWarningAlert(newAlarmData.getTotalWarningAlert())
                .totalInfoAlert(newAlarmData.getTotalInfoAlert())
                .build();

        alarmRepository.save(updated);
    }

}
