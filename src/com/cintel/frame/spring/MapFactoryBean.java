/* 
* Copyright (c)2006-2009 ShangHai CINtel Co. Ltd. 
* All right reserved. 
*/
package com.cintel.frame.spring;

import java.util.Map;

import org.springframework.beans.factory.FactoryBean;

/**
 * @file    : MapFactoryBean.java
 * @version : 1.0
 * @desc    : help the spring load the map. using the factory bean to declare the map at one place and using it everywhere.
 * @history : 
 *          1) 2009-6-23 wangshuda created 
 */
public class MapFactoryBean implements FactoryBean {

    private Map map;
    
    private boolean singleton = true;
    
    public void setMap(Map map) {
        this.map = map;
    }

    public Object getObject() throws Exception {
        return this.map;
    }

    public Class getObjectType() {
        return Map.class;
    }

    public boolean isSingleton() {
        return this.singleton;
    }
}
