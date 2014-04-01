package com.cintel.frame.properties.db;

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.cintel.frame.properties.MessageLoader;
import com.cintel.frame.webui.IDomainService;

/**
 * 
 * @file    : MessageLoaderDbImpl.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-7-5 wangshuda created
 */
public class MessageLoaderDbImpl implements MessageLoader, InitializingBean {
    private static Log log = LogFactory.getLog(MessageLoaderDbImpl.class);
    //
    private IDomainService dbPropertyService;
    
    private Properties dbProperties = new Properties();

    private boolean loadPropertiesFromDb = false;
    
    //
    public String getMessage(String key) {
        return dbProperties.getProperty(key);
    }

    public IDomainService getDbPropertyService() {
        return dbPropertyService;
    }

    public void setDbPropertyService(IDomainService dbPropertyService) {
        this.dbPropertyService = dbPropertyService;
    }

    @SuppressWarnings("unchecked")
    public void loadProperties() {
        dbProperties.clear();
        //
        try {
            List<DbProperty> dbPropertyList = dbPropertyService.list("listProperties",  null);
            if(dbPropertyList != null) {
                for(DbProperty dbProperty:dbPropertyList) {
                    dbProperties.put(dbProperty.getV_key(), dbProperty.getValue());
                } 
            }   
        }
        catch(Exception ex) {
            log.warn("Can not load properties from database.", ex);
        }
    }
    
    public void afterPropertiesSet() throws Exception {
        if(loadPropertiesFromDb) {
            this.loadProperties();
        }
    }

    public boolean isLoadPropertiesFromDb() {
        return loadPropertiesFromDb;
    }

    public void setLoadPropertiesFromDb(boolean loadPropertiesFromDb) {
        this.loadPropertiesFromDb = loadPropertiesFromDb;
    }
}
