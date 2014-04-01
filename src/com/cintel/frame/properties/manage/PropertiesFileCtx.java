package com.cintel.frame.properties.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class PropertiesFileCtx implements InitializingBean {
	//
	private int index;
	
	private String name;
	
	private String title;
	
	private Resource fileLocation;

	private boolean loadKeysInfoFromFile = false;

	private String[] excludeKeysArr = new String[0];
	
	private boolean readonly;

	private String description;
	
	@SuppressWarnings("unchecked")
	private List<PropertiesKeyInfo> keysInfoList = Collections.EMPTY_LIST;


	private void loadKeysInfoFromProperties(PropertiesConfiguration propertiesConfig) throws Exception{
		//
		keysInfoList = new ArrayList<PropertiesKeyInfo>();
		int excludeArrIndex = -1;
		Iterator iterator = propertiesConfig.getKeys();
		String key;
		while(iterator.hasNext()) {
			key = (String)(iterator.next());
			excludeArrIndex = Arrays.binarySearch(excludeKeysArr, key);
			if(excludeArrIndex < 0) {
				keysInfoList.add(new PropertiesKeyInfo(key, propertiesConfig.getString(key)));
			}
		}
	}
	/**
	 * methods declared in InitializingBean
	 */
	public void afterPropertiesSet() throws Exception {
		PropertiesConfiguration properties = new PropertiesConfiguration();
		
		properties.load(fileLocation.getInputStream());
		//
		if(loadKeysInfoFromFile) {
			this.loadKeysInfoFromProperties(properties);
		}
		else {
			for(PropertiesKeyInfo propertiesKeyInfo:keysInfoList) {
				propertiesKeyInfo.setCurrentValue(properties.getString(propertiesKeyInfo.getName()));
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		// be sure the obj do not be null.
		PropertiesFileCtx otherCtx = (PropertiesFileCtx)obj;
		return otherCtx.getName().equals(this.name);
	}
	
	public List<PropertiesKeyInfo> getKeysInfoList() {
		return keysInfoList;
	}

	public void setKeysInfoList(List<PropertiesKeyInfo> keysInfoList) {
		this.keysInfoList = keysInfoList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getFileTargetURL() throws IOException {
		return fileLocation.getURL().toString();
	}

	public Resource getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(Resource fileLocation) {
		this.fileLocation = fileLocation;
	}

	public boolean isLoadKeysInfoFromFile() {
		return loadKeysInfoFromFile;
	}

	public void setLoadKeysInfoFromFile(boolean loadKeysInfoFromFile) {
		this.loadKeysInfoFromFile = loadKeysInfoFromFile;
	}
	public String[] getExcludeKeysArr() {
		return excludeKeysArr;
	}
	public void setExcludeKeysArr(String[] excludeKeysArr) {
		this.excludeKeysArr = excludeKeysArr;
	}

}
