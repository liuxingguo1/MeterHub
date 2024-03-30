package com.vking.duhv.meterhub.integration.mydog.service;

import com.vking.duhv.meterhub.common.Meter;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AnalyInfoEntity;
import org.springframework.stereotype.Component;

@Component
public interface AnalysInfo {

    AnalyInfoEntity analysInfo(Meter meter);

}
