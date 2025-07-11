package com.infraxus.application.alram.alram.service.implementation;

import com.infraxus.application.alram.alram.domain.Alarm;
import com.infraxus.application.alram.alram.domain.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmUpdater {

    private final AlarmRepository alarmRepository;

    public void update(Alarm updatableAlarm, Alarm newAlarmData){
        updatableAlarm.toBuilder()
                .totalCriticalAlert(newAlarmData.getTotalCriticalAlert())
                .totalWarningAlert(newAlarmData.getTotalWarningAlert())
                .totalInfoAlert(newAlarmData.getTotalInfoAlert())
                .build();

        alarmRepository.save(updatableAlarm);
    }

}
