package com.vking.duhv.meterhub.integration.mydog.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 从Spring的上下文中去获取到类，解决@Autowired注入空指针的问题
 * @author nld
 * @version 1.0
 */
@Component
public class ApplicationContextHelperUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext1 ) throws BeansException {
        applicationContext = applicationContext1;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     *
     * @param clazz 需要注入的类
     * @param <T> 泛型
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        return (T) applicationContext.getBean(clazz);
    }
}
