package com.infraxus.application.alarm.alarm.service.implementation;

import com.infraxus.application.alarm.alarm.domain.Alarm;
import com.infraxus.application.alarm.alarm.domain.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmCreator {

    private final AlarmRepository alarmRepository;

    public void save(Alarm alarm){
        alarmRepository.save(alarm);
    }

}
