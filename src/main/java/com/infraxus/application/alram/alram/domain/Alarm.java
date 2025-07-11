package com.infraxus.application.alram.alram.domain;

import com.infraxus.application.alram.alram.domain.value.AlarmId;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("alarm")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {

    @PrimaryKey
    private AlarmId alarmId;

    private Integer totalCriticalAlert;

    private Integer totalWarningAlert;

    private Integer totalInfoAlert;

}
