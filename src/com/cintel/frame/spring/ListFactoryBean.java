/* 
* Copyright (c)2006-2009 ShangHai CINtel Co. Ltd. 
* All right reserved. 
*/
package com.cintel.frame.spring;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;

/**
 * @file    : ListFactoryBean.java
 * @version : 1.0
 * @desc    : help the spring load the map. using the factory bean to declare the map at one place and using it everywhere.
 * @history : 
 *          1) 2009-6-23 wangshuda created 
 */
public class ListFactoryBean implements FactoryBean {

    private List list;
    
    private boolean singleton = true;
    
    public Object getObject() throws Exception {
        return this.list;
    }

    public Class getObjectType() {
        return List.class;
    }

    public boolean isSingleton() {
        return this.singleton;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }
}
