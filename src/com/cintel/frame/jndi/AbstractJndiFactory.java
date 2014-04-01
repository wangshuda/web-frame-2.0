package com.cintel.frame.jndi;

import java.beans.PropertyDescriptor;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.socket.SocketConfig;

/**
 * 
 * @file    : AbstractJndiFactory.java
 * @author  : WangShuDa
 * @date    : 2008-11-26
 * @corp    : CINtel
 * @version : 1.0
 */
public abstract class AbstractJndiFactory implements ObjectFactory {
	protected Log log = LogFactory.getLog(AbstractJndiFactory.class);
	
	protected abstract Class getWantedBeanInstance();
	
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
		// Acquire an instance of our specified bean class
		Object resultObj = this.getWantedBeanInstance().newInstance();

		// Customize the bean properties from our attributes
		Reference ref = (Reference) obj;
		
		PropertyDescriptor[] propertyInfoArr = PropertyUtils.getPropertyDescriptors(resultObj);
		String propertyName = null;
		String propertyType = null;
		for(PropertyDescriptor propertyInfo:propertyInfoArr) {
			propertyName = propertyInfo.getName();
			propertyType = propertyInfo.getPropertyType().toString();
			
			if(!"class".equals(propertyName)) {
				try {
					RefAddr refAddr = ref.get(propertyName);
					
					if(refAddr == null) {
						continue;
					}
					//
					Object value = refAddr.getContent();
					
					if(value == null) {
						continue;
					}
					//
					if("int".equals(propertyType)) {
						PropertyUtils.setProperty(resultObj, propertyName, Integer.valueOf(value.toString()));
					}
					else if("boolean".equals(propertyType)) {
						PropertyUtils.setProperty(resultObj, propertyName, Boolean.valueOf(value.toString()));
					}
					else {
						PropertyUtils.setProperty(resultObj, propertyName, value);
					}
				}
				catch (Exception ex) {
					log.error("propertyName:" + propertyName + "; propertyType:" + propertyType +" set failed!", ex);
				}
			}
		}
		return resultObj;
	}
	
	public static void  main(String args[]) throws InstantiationException, IllegalAccessException {
		Object socketConfig = SocketConfig.class.newInstance();
		PropertyDescriptor[] propertyInfoArr = PropertyUtils.getPropertyDescriptors(socketConfig);
		String propertyName = null;
		for(PropertyDescriptor propertyInfo:propertyInfoArr) {
			propertyName = propertyInfo.getName();
			if(!"class".equals(propertyName)) {
				System.out.println(propertyName);
				System.out.println(propertyInfo.getPropertyType());
			}
		}
		return;
	}	
}
