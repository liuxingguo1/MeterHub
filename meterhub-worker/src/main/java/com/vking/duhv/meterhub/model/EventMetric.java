package com.vking.duhv.meterhub.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventMetric {

    private Long cid;
    private Long stationId;
    private LocalDateTime eventTime;
    private LocalDateTime obtainTime;
    private String measureType;
    private Long deviceId;
    private Long pointMonitorId;
    private Float val;

}
