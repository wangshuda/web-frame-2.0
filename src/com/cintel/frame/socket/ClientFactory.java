package com.cintel.frame.socket;

import org.springframework.beans.factory.FactoryBean;

/**
 * 
 * @file    : ClientFactory.java
 * @author  : WangShuDa
 * @date    : 2009-6-14
 * @corp    : CINtel
 * @version : 2.0
 */
public interface ClientFactory extends FactoryBean {
	public void destroy();
}
