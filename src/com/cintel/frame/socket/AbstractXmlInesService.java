package com.cintel.frame.socket;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.springframework.core.io.Resource;

import com.cintel.frame.util.xml.dom4j.XmlToolBox;
import com.cintel.frame.util.xml.dom4j.XmlUtils;

/**
 * 
 * @file    : AbstractXmlInesService.java
 * @author  : WangShuDa
 * @date    : 2009-1-1
 * @corp    : CINtel
 * @version : 1.0
 */
public class AbstractXmlInesService {
	protected Log log = LogFactory.getLog(AbstractXmlInesService.class);
	
	// ~Outside member
	protected SocketMsgSender msgSender = null;

	public void setMsgSender(SocketMsgSender msgSender) {
		this.msgSender = msgSender;
	}
	
	// ~Inside member
	protected XmlToolBox xmlMsgBox = null;
	
	protected Document doc;
	
	protected void loadTemplateXmlDoc(Resource templateLocation) throws IOException {
		this.loadTemplateXmlDoc(templateLocation.getInputStream());
	}
	
	protected void loadTemplateXmlDoc(InputStream initialXmlMsgInputStream) {
		if(initialXmlMsgInputStream != null) {
			doc = XmlUtils.stream2Doc(initialXmlMsgInputStream, false);
		}
		else {
			doc = DocumentFactory.getInstance().createDocument();
		}
		//
		initXmlMsgBox();
	}
	
	/**
	 * 
	 * @method: initXmlMsgBox
	 * @return: void
	 * @author: WangShuDa
	 */
	protected void initXmlMsgBox() {
		Document clonedDoc = (Document)doc.clone();
		if(xmlMsgBox == null) {
			xmlMsgBox = new XmlToolBox(clonedDoc);
		}
		else {
			xmlMsgBox.setDoc(clonedDoc);
		}
		
	}
	
	// ~ Basic element methods
	// =========================================================================================
	protected void setAttribute(String elementName, String attributeName, String attributeValue) {
		xmlMsgBox.setAttribute(elementName, attributeName, attributeValue);
	}

	protected void setElementText(String parentElementName, String elementName, String text) {
		xmlMsgBox.setElementText(parentElementName, elementName, text);
	}
	protected void addElementText(String parentElementName, String elementName, String text) {
		xmlMsgBox.addElementText(parentElementName, elementName, text);
	}
	
	/**
	 * 
	 * @method: send
	 * @return: String
	 * @author: WangShuDa
	 * @return: post the the content of the xml doc and return the response text.
	 * @throws Exception
	 */
	public String send() throws Exception {
		String request = XmlUtils.doc2XmlString(xmlMsgBox.getDoc());
		//
		if (log.isDebugEnabled()) {
			log.debug(request);
		}
		//
		String responseText = msgSender.post(request);
		//
		if (log.isDebugEnabled()) {
			log.debug(responseText);
		}
		return responseText;
	}
}
