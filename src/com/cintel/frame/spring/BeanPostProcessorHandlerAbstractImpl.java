package com.cintel.frame.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;

/**
 * 
 * @version $Id: BeanPostProcessorHandlerAbstractImpl.java 16631 2010-02-03 05:41:19Z wangshuda $
 * @history 
 *          1.0.0 2010-2-3 wangshuda created
 */
public abstract class BeanPostProcessorHandlerAbstractImpl implements BeanPostProcessorHandler {
	protected Log log = LogFactory.getLog(this.getClass());
	
	protected boolean checked = false;
	
	public Object postProcessAfterInitialization(Object obj, String beanNameInSpring) throws BeansException {
		return obj;
	}

	public Object postProcessBeforeInitialization(Object obj, String beanNameInSpring) throws BeansException {
		return obj;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
