package com.cintel.frame.ws.axis.encode;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cintel.frame.ws.axis.auth.SoapMessageNameHolder;

/**
 * 
 * @file    : ClientResponseEncodingHandler.java
 * @version : 1.0
 * @desc    : decode the soap message element from unicode to gbk.
 * @history : 
 *          1) 2009-7-24 wangshuda created 
 */
public class ClientResponseEncodingHandler extends BasicHandler {
    private static final long serialVersionUID = 1L;
    //
    private static Log log = LogFactory.getLog(ClientResponseEncodingHandler.class);
    
    protected String getPropertyTagName(String bodyTagName) {
        return (String)this.options.get(bodyTagName);
    }
    //
    public void invoke(MessageContext msgCtx) throws AxisFault {
    	if(log.isDebugEnabled()) {
    		log.debug("Enter: ClientResponseEncodingHandler");
    	}
        //
        SOAPMessage soapMsg = msgCtx.getMessage();
        try {
            SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
            //
            SOAPBody soapBody = soapEnv.getBody();
            
            if(soapBody != null && soapBody.getFirstChild() != null) {
            	String tagName = soapBody.getFirstChild().getNodeName();
                
                SoapMessageNameHolder.save(tagName);
                
                String propertyTagName = this.getPropertyTagName(tagName);
                if(propertyTagName != null) {
                	if(log.isDebugEnabled()) {
                		log.debug(String.format("unicodeToGb2312 with propertyTagName: %s, in tagName:%s", propertyTagName, tagName));
                	}
                    //
                    NodeList nodeList = soapBody.getElementsByTagName(propertyTagName);
                    //
                    synchronized(nodeList) {
                        if(nodeList != null) {
                            int nodeCount = nodeList.getLength();
                            Node node = null;
                            for(int i = 0; i < nodeCount; i ++) {
                                node = nodeList.item(i).getFirstChild();
                                // fixed by wangxin.
                                if(null != node){
                                    node.setNodeValue(EncodeUtils.unicodeToGb2312(node.getNodeValue()));
                                }
                                //end.
                            }
                        } 
                    }
                    
                    //
                    soapMsg.saveChanges();
                }
            }
        }
        catch (SOAPException ex) {
            log.warn("AxisClientHandler error!", ex);
        }

    }

}
