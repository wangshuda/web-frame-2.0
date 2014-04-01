package com.cintel.frame.ws.axis;

import java.util.Map;
import java.util.Set;

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

/**
 * 
 * @version $Id: RequestSoapHeaderHandler.java 16685 2010-02-08 01:34:21Z wangshuda $
 * @history 
 *          1.0.0 2010-2-8 wangshuda created
 */
public class RequestSoapHeaderHandler extends BasicHandler {
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(RequestSoapHeaderHandler.class);
			
	@SuppressWarnings("unchecked")
	public void invoke(MessageContext msgCtx) throws AxisFault {
		
        if(this.options != null && !this.options.isEmpty()) {
        	//
        	if(log.isDebugEnabled()) {
        		log.debug("Call RequestSoapHeaderHandler!");
        	}
        	//
        	try {
        		SOAPMessage soapMsg = msgCtx.getMessage();
            	//
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                //
                SOAPHeader soapHeader =  soapEnv.getHeader();
                //
            	Set<Map.Entry> entrySet = this.options.entrySet();
            	for(Map.Entry entry:entrySet) {
            		//
            		SOAPElement fromSoapElement = soapHeader.addChildElement(entry.getKey().toString());
            		fromSoapElement.setValue(entry.getValue().toString());
            	}
            	//
            	soapMsg.saveChanges();
        	}
        	catch (SOAPException ex) {
    	        log.warn("RequestSoapHeaderHandler error!", ex);
    	    }            
        }
	    
	}

}
