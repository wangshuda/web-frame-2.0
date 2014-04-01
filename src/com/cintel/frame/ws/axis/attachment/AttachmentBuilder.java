package com.cintel.frame.ws.axis.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.axis.MessageContext;
import org.apache.axis.client.Stub;

import com.sun.xml.internal.ws.api.message.Attachment;

/**
 * 
 * @version $Id: AttachmentBuilder.java 39040 2013-04-11 06:50:00Z lifeifei $
 * @history 
 *          1.0.0 2010-2-5 wangshuda created
 */
public interface AttachmentBuilder {

	public void addIntoSendingMsg(Stub axisStub, InputStream in, String contentType) throws IOException;
	
	public List<AttachmentInfo> loadFromNotifiedMsg(MessageContext notifyMsgConetxt) throws SOAPException;
	
	public List<Attachment> loadFromResponse(Stub axisStub) throws SOAPException;

}
