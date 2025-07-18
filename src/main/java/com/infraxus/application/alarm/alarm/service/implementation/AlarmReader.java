package com.infraxus.application.alarm.alarm.service.implementation;

import com.infraxus.application.alarm.alarm.domain.Alarm;
import com.infraxus.application.alarm.alarm.domain.repository.AlarmRepository;
import com.infraxus.application.alarm.alarm.domain.value.AlarmId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmReader {

    private final AlarmRepository alarmRepository;

    public List<Alarm> findAll(){
        return alarmRepository.findAll();
    }

    public Alarm findById(AlarmId id){
        return alarmRepository.findById(id)
                .orElse(null);
    }

}
