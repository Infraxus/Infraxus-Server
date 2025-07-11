package com.infraxus.application.alram.alram.service.implementation;

import com.infraxus.application.alram.alram.domain.Alarm;
import com.infraxus.application.alram.alram.domain.repository.AlarmRepository;
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
