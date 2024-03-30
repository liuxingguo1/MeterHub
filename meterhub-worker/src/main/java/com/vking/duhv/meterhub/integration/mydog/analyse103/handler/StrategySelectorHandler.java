package com.vking.duhv.meterhub.integration.mydog.analyse103.handler;

import com.vking.duhv.meterhub.integration.mydog.service.AnalysInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StrategySelectorHandler {
    @Autowired
    private Map<String, AnalysInfo> selectorMap;

    /**
     * 根据类型选择对应的策略
     * @param type 协议类型
     * @return 抽象策略处理器
     */
    public AnalysInfo select(String type) {
        return selectorMap.get(type);
    }

}
