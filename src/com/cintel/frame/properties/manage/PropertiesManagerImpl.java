package com.cintel.frame.properties.manage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;


public class PropertiesManagerImpl implements PropertiesManager {
	protected Log log = LogFactory.getLog(PropertiesManagerImpl.class);
	//
	@SuppressWarnings("unchecked")
	private Map<String, PropertiesFileCtx> propertiesFileCtxMap = Collections.EMPTY_MAP;
	
	public PropertiesFileCtx getPropertiesFileCtx(String fileCtxKeyInSpring) {
		return propertiesFileCtxMap.get(fileCtxKeyInSpring);
	}
	
	public Collection<PropertiesFileCtx> getPropertiesFileCtxCollection() {
		return this.propertiesFileCtxMap.values();
	}

	public void save(String fileCtxKeyInSpring, Map<String, String> propertiesMap) {
		PropertiesFileCtx propertiesFileCtx = propertiesFileCtxMap.get(fileCtxKeyInSpring);
		this.save(propertiesFileCtx, propertiesMap);
	}
	
	private void save(PropertiesFileCtx propertiesFileCtx, Map<String, String> propertiesMap) {
		Resource propertiesFileResource = propertiesFileCtx.getFileLocation();
		try {
			PropertiesConfiguration properties = this.loadPropertiesMap(propertiesFileResource);
			//
			String keyName = null;
			
			for(PropertiesKeyInfo propertiesKeyInfo:propertiesFileCtx.getKeysInfoList()) {
				keyName = propertiesKeyInfo.getName();
				properties.setProperty(keyName, propertiesMap.get(keyName));
			}
			properties.save(new FileOutputStream(propertiesFileResource.getFile()), "UTF-8");
		}
		catch (Exception ex) {
			log.warn("propertiesMap save error!", ex);
			throw new ErrorReportException(new ErrorInfo("error.properties.save", new String[]{propertiesFileResource.getDescription()}));
		}
	}

	private PropertiesConfiguration loadPropertiesMap(Resource resource) throws FileNotFoundException, IOException, ConfigurationException {
		PropertiesConfiguration properties = new PropertiesConfiguration();
		properties.load(resource.getInputStream());
		//
		return properties;
	}
	
	public void save(Map<String, String> allPropertiesMap) {
		Collection<PropertiesFileCtx> propertiesFileCtxList = this.getPropertiesFileCtxCollection();
		//
		for(PropertiesFileCtx propertiesFileCtx:propertiesFileCtxList) {
			this.save(propertiesFileCtx, allPropertiesMap);
		}
	}

	public Map<String, PropertiesFileCtx> getPropertiesFileCtxMap() {
		return propertiesFileCtxMap;
	}

	public void setPropertiesFileCtxMap(
			Map<String, PropertiesFileCtx> propertiesFileCtxMap) {
		this.propertiesFileCtxMap = propertiesFileCtxMap;
	}
}
