package com.cintel.frame.ws.axis;

import java.lang.reflect.Constructor;
import java.net.URL;

import org.apache.axis.client.Stub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;

public class AxisClientStubFactory implements FactoryBean {
	private static final Log log = LogFactory.getLog(AxisClientStubFactory.class);
	//
	private String locatorClassName;
	
	private String stubClassName;

	private boolean singleton = true;
	
	private String serviceUrlLocation;
	
	private String serviceName;
	
	// is the return value.
	private Stub stub = null;
	
	// implement FactoryBean methods.
	public Object getObject() throws Exception {
		
		if(stub == null) {
			Constructor constructor = Class.forName(stubClassName).getConstructor(java.net.URL.class, javax.xml.rpc.Service.class);
			//
			stub = (Stub)(constructor.newInstance(new URL(serviceUrlLocation), Class.forName(locatorClassName).newInstance()));
			stub.setPortName(serviceName);
			//
			if(log.isDebugEnabled()) {
				log.debug("Generate the stub using:" + locatorClassName + ";" + stubClassName);
			}
		}
		return stub;
	}

	public Class getObjectType() {
		return javax.xml.rpc.Stub.class;
	}

	public boolean isSingleton() {
		return this.singleton;
	}
	
	// ---------------- get/set methods --------------------
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public String getLocatorClassName() {
		return locatorClassName;
	}

	public void setLocatorClassName(String locatorClassName) {
		this.locatorClassName = locatorClassName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceUrlLocation() {
		return serviceUrlLocation;
	}

	public void setServiceUrlLocation(String serviceUrlLocation) {
		this.serviceUrlLocation = serviceUrlLocation;
	}

	public String getStubClassName() {
		return stubClassName;
	}

	public void setStubClassName(String stubClassName) {
		this.stubClassName = stubClassName;
	}
}
