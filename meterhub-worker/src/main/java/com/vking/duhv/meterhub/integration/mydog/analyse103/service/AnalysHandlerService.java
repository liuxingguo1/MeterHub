package com.vking.duhv.meterhub.integration.mydog.analyse103.service;


import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.ASDUBaseEntity;
import com.vking.duhv.meterhub.integration.mydog.analyse103.entity.AsduMessageInfo;

/**
 * @author lucan.liu
 * @date 2023-12-15 11:04
 */
public interface AnalysHandlerService {
    ASDUBaseEntity analysMessage(AsduMessageInfo asduMessageInfo);
}
