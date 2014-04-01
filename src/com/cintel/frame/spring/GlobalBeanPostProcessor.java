package com.cintel.frame.spring;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 
 * @version $Id: GlobalBeanPostProcessor.java 16631 2010-02-03 05:41:19Z wangshuda $
 * @history 
 *          1.0.0 2010-2-2 wangshuda created
 */
@SuppressWarnings("unchecked")
public class GlobalBeanPostProcessor implements BeanPostProcessor {
	private Map<String, BeanPostProcessorHandler> processorHandlerMap = Collections.EMPTY_MAP;
	//
	public Object postProcessAfterInitialization(Object obj, String beanName) throws BeansException {
		BeanPostProcessorHandler processorHandler = null;
		//
		processorHandler = processorHandlerMap.get(beanName);
		//
		if(processorHandler != null) {
			return processorHandler.postProcessAfterInitialization(obj, beanName);
		}
		//
		return obj;
	}

	public Object postProcessBeforeInitialization(Object obj, String beanName) throws BeansException {
		BeanPostProcessorHandler processorHandler = null;
		//
		processorHandler = processorHandlerMap.get(beanName);
		//
		if(processorHandler != null) {
			return processorHandler.postProcessBeforeInitialization(obj, beanName);
		}
		//
		return obj;
	}

	public Map<String, BeanPostProcessorHandler> getProcessorHandlerMap() {
		return processorHandlerMap;
	}

	public void setProcessorHandlerMap(
			Map<String, BeanPostProcessorHandler> processorHandlerMap) {
		this.processorHandlerMap = processorHandlerMap;
	}

}
