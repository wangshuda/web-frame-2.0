package com.cintel.frame.poi.excel.csv;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.cintel.frame.poi.excel.ColumnInfo;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-9-30 wangshuda created
 */
public class CsvFileConfig implements InitializingBean {
	
	private boolean withTitle = true;
	
	private String commandClass;
    
    @SuppressWarnings("unchecked")
    private Set<ColumnInfo> columnInfoList = Collections.EMPTY_SET;
    
    public int getColumnCnt() {
    	return columnInfoList.size();
    }
    
    public void afterPropertiesSet() throws Exception {
        this.checkConfig();
    }

    public void checkConfig() throws Exception {
        Assert.notNull(commandClass);
        Assert.notNull(columnInfoList);
        //
        Object resultObj = Class.forName(commandClass).newInstance();
        for(ColumnInfo columnInfo:columnInfoList) {
            PropertyUtils.getProperty(resultObj, columnInfo.getPropertyName());
        }
    }

	public Set<ColumnInfo> getColumnInfoList() {
		return columnInfoList;
	}

	public void setColumnInfoList(Set<ColumnInfo> columnInfoList) {
		this.columnInfoList = columnInfoList;
	}

	public String getCommandClass() {
		return commandClass;
	}

	public void setCommandClass(String commandClass) {
		this.commandClass = commandClass;
	}

	public boolean isWithTitle() {
		return withTitle;
	}

	public void setWithTitle(boolean withTitle) {
		this.withTitle = withTitle;
	}
}
