package com.cintel.frame.spring;

import org.springframework.beans.BeansException;

/**
 * 
 * @version $Id: BeanPostProcessorHandler.java 16631 2010-02-03 05:41:19Z wangshuda $
 * @history 
 *          1.0.0 2010-2-3 wangshuda created
 */
public interface BeanPostProcessorHandler {
    public abstract Object postProcessBeforeInitialization(Object obj, String beanName) throws BeansException;

    public abstract Object postProcessAfterInitialization(Object obj, String beanName) throws BeansException;
}
