package com.vking.duhv.meterhub.model;

import java.time.LocalDateTime;

public class PointMonitorMetric {

    private LocalDateTime eventTime;

    private String type;

    private LocalDateTime time;

    private Long cid;

    private Long stationId;

    private Long deviceId;

    private Long pointMonitorId;

    private Float val;

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getPointMonitorId() {
        return pointMonitorId;
    }

    public void setPointMonitorId(Long pointMonitorId) {
        this.pointMonitorId = pointMonitorId;
    }

    public Float getVal() {
        return val;
    }

    public void setVal(Float val) {
        this.val = val;
    }
}
