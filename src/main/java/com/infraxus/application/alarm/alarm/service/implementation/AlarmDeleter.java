package com.infraxus.application.alarm.alarm.service.implementation;

import com.infraxus.application.alarm.alarm.domain.Alarm;
import com.infraxus.application.alarm.alarm.domain.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmDeleter {

    private final AlarmRepository alarmRepository;

    public void delete(Alarm alarm){
        alarmRepository.delete(alarm);
    }

}
