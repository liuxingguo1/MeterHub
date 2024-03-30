package com.vking.duhv.meterhub.integration.mydog.analyse103.annotation;

import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.TypeIdentifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lucan.liu
 * @date 2023-12-15 10:57
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface AnalysHanlerAnnotation {
    TypeIdentifier type();
}
