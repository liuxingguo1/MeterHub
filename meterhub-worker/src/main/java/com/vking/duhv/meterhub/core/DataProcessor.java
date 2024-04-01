package com.vking.duhv.meterhub.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vking.duhv.meterhub.common.Meter;

public interface DataProcessor {

    void doProcess(Meter meter) throws JsonProcessingException;

}
