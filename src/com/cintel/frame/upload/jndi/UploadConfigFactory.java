/**
* filename: UploadConfigFactory.java
*
* @author:Xin Wang(wangxin@cintel.net.cn)
* @Corp:CINtel
*
* date:2008-11-26
* time:обнГ04:17:50
*
*/
package com.cintel.frame.upload.jndi;

import com.cintel.frame.jndi.AbstractJndiFactory;
import com.cintel.frame.upload.UploadConfig;

public class UploadConfigFactory extends AbstractJndiFactory{

	
	@Override
	protected Class getWantedBeanInstance() {
		return UploadConfig.class;
	}	

}
