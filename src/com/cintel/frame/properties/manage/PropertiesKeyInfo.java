package com.cintel.frame.properties.manage;

import java.util.Collections;
import java.util.List;

import com.cintel.frame.web.action.JsonOption;


public class PropertiesKeyInfo {

	private String name;

	private String title;

	private String uiType = "text"; // text, select

	private String validateStr = "Require";

	private boolean canBeEmpty = false;

	private String description;
	
	private String currentValue;
	
	@SuppressWarnings("unchecked")
	private List<JsonOption> optionsList = Collections.EMPTY_LIST;

	private boolean canBeChinese = true;
	
	private boolean readonly = false;
	
	public PropertiesKeyInfo() {
	}
	
	/**
	 * 
	 * @param name
	 * @param currentValue
	 */
	public PropertiesKeyInfo(String name, String currentValue) {
		this.name = name;
		this.title = name;
		this.currentValue = currentValue;
	}
	
	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isCanBeEmpty() {
		return canBeEmpty;
	}

	public void setCanBeEmpty(boolean canBeEmpty) {
		this.canBeEmpty = canBeEmpty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<JsonOption> getOptionsList() {
		return optionsList;
	}

	public void setOptionsList(List<JsonOption> optionsList) {
		this.optionsList = optionsList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUiType() {
		return uiType;
	}

	public void setUiType(String uiType) {
		this.uiType = uiType;
	}

	public String getValidateStr() {
		return validateStr;
	}

	public void setValidateStr(String validateStr) {
		this.validateStr = validateStr;
	}

	public boolean isCanBeChinese() {
		return canBeChinese;
	}

	public void setCanBeChinese(boolean canBeChinese) {
		this.canBeChinese = canBeChinese;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
}
