package com.cintel.frame.spring;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-6-18 wangshuda created
 */
public class SpringBean2MapFactory implements FactoryBean, ApplicationContextAware {
    private Log log = LogFactory.getLog(SpringBean2MapFactory.class);
    //
    private ApplicationContext springAppCtx;
    
	private Map<String, Object> springBeanMap;
	
    private boolean reLoadDone = false;
    
	private Class objClass;
	
	public Object getObject() throws Exception {
        boolean needReLoad = (!this.reLoadDone && (this.springBeanMap == null || this.springBeanMap.isEmpty()));
        //
        if(needReLoad) {
            log.debug("Reload for :" + objClass.toString());
            //
            reLoadDone = true;
            this.loadBean2Map();
        }
        //
		return this.springBeanMap;
	}

	public Class getObjectType() {
		return Map.class;
	}

	public boolean isSingleton() {
		return true;
	}

    @SuppressWarnings("unchecked")
    private void loadBean2Map() {
        if(this.springAppCtx != null) {
            boolean includePrototypes = true;
            boolean allowEagerInit = true;
            this.springBeanMap = this.springAppCtx.getBeansOfType(objClass, includePrototypes, allowEagerInit);
            //
            log.debug(String.format("Find %d %s", this.springBeanMap.size(), objClass.getName()));
        }
    }
    
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
        this.springAppCtx = applicationContext;
        
        this.loadBean2Map();
	}

	public void setObjClass(String objClassStr) throws ClassNotFoundException {
		this.objClass = Class.forName(objClassStr);
	}

}
