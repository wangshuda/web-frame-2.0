package com.cintel.frame.ws.axis;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.UserInfoUtils;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.encrypt.Md5Digest;
import com.cintel.frame.util.DateUtils;
import com.cintel.frame.util.StringUtils;


/**
 * 
 * @version $Id: RequestHeaderBuilderAbstractImpl.java 16685 2010-02-08 01:34:21Z wangshuda $
 * @history 
 *          1.0.0 2010-2-8 wangshuda created
 */
public abstract class RequestHeaderBuilderAbstractImpl extends BasicHandler implements RequestHeaderBuilder  {
	//
	protected Log log = LogFactory.getLog(this.getClass());
	//
	protected abstract String loadCurrentUserOperatorType();
	
	protected void generateOperateInfoElement(SOAPElement operateInfoElement) throws SOAPException {
		
		UserDetails userDetails = UserInfoUtils.getUserDetails();
		
		if(userDetails != null) {
			//type
			SOAPElement typeElement = operateInfoElement.addChildElement("type");
			
			String operatorType = this.loadCurrentUserOperatorType();
			if(!StringUtils.hasText(operatorType)) {
				operatorType = this.loadOptConf("conf.defaultOperatorType", "UNKNOWN");
			}
			typeElement.setValue(operatorType);
			
			//id
			SOAPElement idElement = operateInfoElement.addChildElement("id");
			String userId = userDetails.getUserNumber();
			if(!StringUtils.hasText(userId)) {
				userId = this.loadOptConf("conf.defaultOperatorId", "UNKNOWN");
			}
			idElement.setValue(userId);
			
			//ipAddr
			SOAPElement ipAddrElement = operateInfoElement.addChildElement("ipAddr");
			String ipAddr = userDetails.getLoginRequestIp();
			
			if(!StringUtils.hasText(ipAddr)) {
				ipAddr = this.loadOptConf("conf.defaultOperatorIpAddr", "UNKNOWN");
			}
			ipAddrElement.setValue(ipAddr);
			
			//pwd
			SOAPElement pwdElement = operateInfoElement.addChildElement("pwd");
			String pwd = userDetails.getPassword();
			
			if(!StringUtils.hasText(pwd) ) {
				pwd = this.loadOptConf("conf.defaultOperatorPwd", "111111");
			}
			//
			if(pwd.length() < 32 && this.isEncryptedPwdWithMD5()) {
				pwd = Md5Digest.getInstance().getEncryptedPwd(pwd);
			}
			pwdElement.setValue(pwd);
			//
		}
	}
	//
	protected String loadOptConf(String key, String defaultValue) {
		String optConf = (String)this.options.get(key);
		if(!StringUtils.hasText(optConf)) {
			optConf = defaultValue;
			//
			if(log.isInfoEnabled()) {
				log.info(new StringBuffer("Can not find the parameter with name:").append(key));
			}
			
		}
		return optConf;
	}
	
	protected String getVersionTag(String methodNameSpace, String methodName) {
		String defaultVersionTag = this.loadOptConf("conf.defaultVersionTag", "1.0.0");
		
		StringBuffer buffer = new StringBuffer(methodNameSpace);
		buffer.append("/");
		buffer.append(methodName);
		
		String methodLocation = buffer.toString();
		//
		if(log.isDebugEnabled()) {
			log.debug(String.format("Find the version with ws method:%s", methodLocation));
		}
		//
		return this.loadOptConf(methodLocation, defaultVersionTag);
	}
	
	public void invoke(MessageContext msgCtx) throws AxisFault {
		if(this.options != null && !this.options.isEmpty()) {
        	//
        	if(log.isDebugEnabled()) {
        		log.debug("Call RequestCcSoapHeaderHandler!");
        	}
        	//
        	try {
        		SOAPMessage soapMsg = msgCtx.getMessage();
            	//
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                
                SOAPBody soapBody = soapEnv.getBody();
                
                org.w3c.dom.Node firstNode = soapBody.getFirstChild();
                
                String methodName = firstNode.getNodeName();
                String methodNameSpace = firstNode.getNamespaceURI();
            
                //
                SOAPHeader soapHeader =  soapEnv.getHeader();
                SOAPElement requestSoapHeaderElement = soapHeader.addChildElement("RequestSOAPHeader", "");
                //
        		SOAPElement portalElement = requestSoapHeaderElement.addChildElement("portal");
        		String portalType = this.loadOptConf("conf.portalType", PortalType._WEB);
        		portalElement.setValue(portalType);
        		
        		//operator
        		SOAPElement operatorElement = requestSoapHeaderElement.addChildElement("operator");
        		this.generateOperateInfoElement(operatorElement);
        		
        		//timeStamp
        		SOAPElement timeStampElement = requestSoapHeaderElement.addChildElement("timeStamp");
        		timeStampElement.setValue(DateUtils.getCurrentTimeString("yyyyMMddHHmmssSSS"));
            	
        		//versionTag
        		SOAPElement versionTagElement = requestSoapHeaderElement.addChildElement("versionTag");
        		String versionTag = getVersionTag(methodNameSpace, methodName);
        		versionTagElement.setValue(versionTag);

        		//
            	soapMsg.saveChanges();
        	}
        	catch (SOAPException ex) {
    	        log.warn("RequestCcSoapHeaderHandler error!", ex);
    	    }            
        }
	}

	// -------------- get/set methods ----------------------
	public boolean isEncryptedPwdWithMD5() {
		return Boolean.valueOf(this.loadOptConf("encryptedPwdWithMD5", "true"));
	}
}
