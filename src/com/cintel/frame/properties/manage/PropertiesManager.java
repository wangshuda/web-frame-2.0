package com.cintel.frame.properties.manage;

import java.util.Collection;
import java.util.Map;

public interface PropertiesManager {
	public Collection<PropertiesFileCtx> getPropertiesFileCtxCollection();
	
	public PropertiesFileCtx getPropertiesFileCtx(String mapKey);
	
	public void save(String fileCtxKeyInSpring, Map<String, String> propertiesMap);
	
	public void save(Map<String, String> allPropertiesMap);
}
