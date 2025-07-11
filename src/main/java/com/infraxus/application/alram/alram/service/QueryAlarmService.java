package com.infraxus.application.alram.alram.service;

import com.infraxus.application.alram.alram.domain.Alarm;
import com.infraxus.application.alram.alram.domain.exception.AlarmNotFoundException;
import com.infraxus.application.alram.alram.domain.value.AlarmId;
import com.infraxus.application.alram.alram.service.implementation.AlarmReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryAlarmService {

    private final AlarmReader alarmReader;

    public List<Alarm> findAll(){
        return alarmReader.findAll();
    }

    public Alarm findById(AlarmId id){
        return alarmReader.findById(id);
    }

    public Alarm getAlarmById(AlarmId alarmId) {
        Alarm alarm = alarmReader.findById(alarmId);
        if (alarm == null) {
            throw new AlarmNotFoundException("Alarm with Server Id " + alarmId.getServerId() + " and Container Id " + alarmId.getContainerId()
                    + " not found.");
        }
        return alarm;
    }
}
