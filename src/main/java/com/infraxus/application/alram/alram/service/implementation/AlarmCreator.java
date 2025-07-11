package com.infraxus.application.alram.alram.service.implementation;

import com.infraxus.application.alram.alram.domain.Alarm;
import com.infraxus.application.alram.alram.domain.repository.AlarmRepository;
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
