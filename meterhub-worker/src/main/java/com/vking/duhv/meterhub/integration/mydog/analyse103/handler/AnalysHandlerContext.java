package com.vking.duhv.meterhub.integration.mydog.analyse103.handler;

import com.vking.duhv.meterhub.integration.mydog.analyse103.annotation.AnalysHanlerAnnotation;
import com.vking.duhv.meterhub.integration.mydog.analyse103.service.AnalysHandlerService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lucan.liu
 * @date 2023-12-15 12:35
 */
@Service(value = "analysHandlerContext")
public class AnalysHandlerContext implements BeanPostProcessor {
        private final Map<String, AnalysHandlerService> analysHandlerMap = new HashMap<>();

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            AnalysHanlerAnnotation annotation = AnnotationUtils.findAnnotation(bean.getClass(), AnalysHanlerAnnotation.class);
            if (ClassUtils.isAssignableValue(AnalysHandlerService.class,bean) && annotation != null) {
                analysHandlerMap.put(annotation.type().name(), (AnalysHandlerService)bean);
            }
            return bean;
        }

        public AnalysHandlerService getHanler(String type) {
            AnalysHandlerService handler = this.analysHandlerMap.get(type);
            if (handler == null) {
                throw new RuntimeException("not.found.BizHandler");
            }
            return handler;
        }

}
