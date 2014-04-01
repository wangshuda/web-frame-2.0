package com.cintel.frame.ws.axis.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Stub;

import com.sun.xml.internal.ws.api.message.Attachment;

/**
 * 
 * @version $Id: AttachmentBuilderImpl.java 40057 2013-06-14 06:59:22Z wangshuda $
 * @history 
 *          1.0.0 2010-2-5 wangshuda created
 */
public class AttachmentBuilderImpl implements AttachmentBuilder {

	public void addIntoSendingMsg(Stub stub, InputStream in, String contentType) throws IOException {
		stub.addAttachment(new DataHandler(new ByteArrayDataSource(in, contentType)));
	}
	
	public List<AttachmentInfo> loadFromNotifiedMsg(MessageContext responseMsgConetxt) throws SOAPException {
		List<AttachmentInfo> attachmentsList = new ArrayList<AttachmentInfo>();
		
		SOAPMessage soapMessage = responseMsgConetxt.getMessage();
    	Iterator attachments = soapMessage.getAttachments();
    	AttachmentPart loopAttatch = null;
    	//
    	if(attachments.hasNext()) {
    		int index = 0;
    		AttachmentInfo attachmentInfo = null;
    		//
	    	while(attachments.hasNext()) {
	    		loopAttatch = (AttachmentPart)attachments.next();
	    		attachmentInfo = this.attachmentPart2Info(loopAttatch, index++);
	    		//
	    		attachmentsList.add(attachmentInfo);
	    	}
    	}
		return attachmentsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Attachment> loadFromResponse(Stub stub) throws SOAPException {
		Object[] os = stub.getAttachments();
		List<Attachment> resultList = new ArrayList();
		//
		if(os != null && os.length > 0) {
			for(Object object:os) {
				resultList.add((Attachment)object);
			}
		}
		return resultList;
	}

	private AttachmentInfo attachmentPart2Info(AttachmentPart attachmentPart, int index) throws SOAPException {
		AttachmentInfo attachmentInfo = new AttachmentInfo();

		attachmentInfo.setIndex(index);
		attachmentInfo.setFileName(attachmentPart.getAttachmentFile());
		attachmentInfo.setDataHandler(attachmentPart.getDataHandler());
		attachmentInfo.setContentType(attachmentPart.getContentType());
		attachmentInfo.setSize(attachmentPart.getSize());
		//
		return attachmentInfo;
	}
	
}
