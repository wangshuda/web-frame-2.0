package com.cintel.frame.socket.jndi;

import javax.naming.spi.ObjectFactory;

import com.cintel.frame.jndi.AbstractJndiFactory;
import com.cintel.frame.socket.SocketConfig;

/**
 * 
 * @file    : SocketConfigJndiFactory.java
 * @author  : WangShuDa
 * @date    : 2008-11-26
 * @corp    : CINtel
 * @version : 1.0
 */
public class SocketConfigJndiFactory extends AbstractJndiFactory implements ObjectFactory {
	@Override
	protected Class getWantedBeanInstance() {
		return SocketConfig.class;
	}
}
