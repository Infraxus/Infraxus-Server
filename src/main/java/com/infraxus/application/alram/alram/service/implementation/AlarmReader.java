package com.infraxus.application.alram.alram.service.implementation;

import com.infraxus.application.alram.alram.domain.Alarm;
import com.infraxus.application.alram.alram.domain.repository.AlarmRepository;
import com.infraxus.application.alram.alram.domain.value.AlarmId;
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
