package com.infraxus.application.monitoring.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MetricResponse {
    private double cpuUsage;
    private double memoryUsage;
    private long memoryTotal;
    private long memoryFree;
    private double networkRxBytes;
    private double networkTxBytes;
    private double diskReadBytes;
    private double diskWriteBytes;
}
