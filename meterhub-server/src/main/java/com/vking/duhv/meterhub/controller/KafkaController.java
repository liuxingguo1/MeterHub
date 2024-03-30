package com.vking.duhv.meterhub.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vking.duhv.meterhub.common.SignalMetric;
import com.vking.duhv.meterhub.mq.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class KafkaController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaSender kafkaSender;

    @GetMapping("/signalMetric")
    public String signalMetric(@RequestParam("id") Long id) {
        SignalMetric sm = new SignalMetric();
        sm.setMeasureType("S");
        sm.setEventTime(LocalDateTime.now());
        sm.setObtainTime(LocalDateTime.now());
        sm.setCid(1L);
        sm.setStationId(2L);
        sm.setDeviceId(3L);
        sm.setPointMonitorId(id);
        sm.setVal(34.1F);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(sm);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaSender.send("duhv_signal_metric", UUID.randomUUID().toString(), json);
        return "ok";
    }


    @GetMapping("/pressureTest")
    public String pressureTest(@RequestParam("id") Long id, @RequestParam("times") Long times) {
        SignalMetric sm = new SignalMetric();
        sm.setMeasureType("S");
        sm.setEventTime(LocalDateTime.now());
        sm.setObtainTime(LocalDateTime.now());
        sm.setCid(1L);
        sm.setStationId(2L);
        sm.setDeviceId(3L);
        sm.setPointMonitorId(id);
        sm.setVal(34.1F);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(sm);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        while (times-- > 0) {

            kafkaSender.send("duhv_signal_metric", UUID.randomUUID().toString(), json);
        }
        return "ok";
    }


}