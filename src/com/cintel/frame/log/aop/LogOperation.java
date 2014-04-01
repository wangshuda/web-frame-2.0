package com.cintel.frame.log.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation
 * 
 * @file : $Id: LogOperation.java 13450 2009-12-17 00:30:18Z wangshuda $
 * @corp : CINtel
 */

@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)   
@Documented
public @interface LogOperation {

    String key() default "";
    
    String text() default "";
}
