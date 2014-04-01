package com.cintel.frame.socket;

import org.springframework.beans.factory.FactoryBean;

/**
 * 
 * @file    : ClientFactoryImpl.java
 * @author  : WangShuDa
 * @date    : 2009-2-25
 * @corp    : CINtel
 * @version : 1.0
 */
public class ClientFactoryImpl implements ClientFactory, FactoryBean  {
    //~Fileds ===========================================================================
	private SocketClient socketClient = null;

	//
	private SocketConfig socketConfig = null;
	
	public SocketConfig getSocketConfig() {
		return socketConfig;
	}

	public void setSocketConfig(SocketConfig socketConfig) {
		this.socketConfig = socketConfig;
	}
	
    // ------------- methods declare in ClientFactory interface
	public void destroy() {
		if(socketClient != null) {
			socketClient.close();
			socketClient = null;
		}
	}

	// ------------- methods declare in FactoryBean interface
	public Object getObject() throws Exception {
		if (socketClient == null) {
			socketClient = new SocketClientImpl(socketConfig);
			//
			socketClient.connect();
		}
		else {
			socketClient.validate();
		}
		//
		return socketClient;
	}

	public Class getObjectType() {
		return SocketClient.class;
	}

	public boolean isSingleton() {
		return true;
	}
}
