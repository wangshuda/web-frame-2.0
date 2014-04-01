package com.cintel.frame.util.xml.jdom;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.xml.sax.InputSource;


/**
 * XmlToolBox
 * 
 * @version : 1.0.0
 * @date    : 2007/10/23
 * @author  : WangShuDa
 * @descript: 
 * 
 */
public class XmlToolBox {
	
	private boolean autoSave = false;
	
	private Document doc = null;
	
	private File file = null;
	
	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}
	
	public boolean getAutoSave() {
		return autoSave;
	}

	public Document getDoc() {
		return doc;
	}
	
	public File getFile() {
		return file;
	}
	
	private void setDoc(Document doc) throws Exception {
		if(doc == null) {
			throw new Exception("Doc can not be null !");
		}
		this.doc = doc;
	}
	
	public XmlToolBox(File file, boolean autoSave, boolean validate)  throws Exception {
		Document doc = XmlUtil.file2Doc(file, validate);
		
		setDoc(doc);
		setAutoSave(autoSave);
	}
	
	public XmlToolBox(String filePath, boolean autoSave, boolean validate)  throws Exception {
		this(new File(filePath), autoSave, validate);
	}
	
	public XmlToolBox(Document doc)  throws Exception {
		setDoc(doc);
		setAutoSave(false);
	}

	public XmlToolBox(String filePath)  throws Exception {
		this(new File(filePath), false, false);
	}
	
	public XmlToolBox(InputStream stream, boolean validate)  throws Exception {
		Document doc = XmlUtil.stream2Doc(stream, validate);
		setDoc(doc);
		setAutoSave(false);
	}
	
	public XmlToolBox(InputSource inputSource, boolean validate)  throws Exception {
		Document doc = XmlUtil.inputSource2Doc(inputSource, validate);
		setDoc(doc);
		setAutoSave(false);
	}
	
	public XmlToolBox(String xmlStr, boolean validate)  throws Exception {
		Document doc = XmlUtil.xmlStr2Doc(xmlStr, validate);
		setDoc(doc);
		setAutoSave(false);
	}
	
	public XmlToolBox(File file)  throws Exception {
		this(file, false, false);
	}
	
	public synchronized void doSave() throws Exception {
		XmlUtil.doc2File(doc, file);
	}

	public Element getElement(String elementName) {
	    //for example: elementName is info\\sagInfo\\service
	    String[] nameArr = XmlUtil.parseElementName(elementName);
	    Element element = doc.getRootElement();

	    for (int i = 0; i < nameArr.length; i++) {
	    	element = element.getChild(nameArr[i]);
	    	if(element == null) {
	    		return null;
	    	}
	    }
	    return element;
	}
	
	public Element findCreate(String elementName) throws Exception {
	    String[] nameArr = XmlUtil.parseElementName(elementName);
	    Element element = doc.getRootElement();
	    //
	    for (int i = 0; i < nameArr.length; i++) {
	      if(element.getChild(nameArr[i]) == null) {
	        //add the element into the doc
	        element.addContent(new Element(nameArr[i]));
	      }
	      element = element.getChild(nameArr[i]);
	    }
	    if(autoSave) {
	    	doSave();
	    }
	    //
	    return element;
	}
	
	public List getElementList(String elementName) {
		List result = new ArrayList();
	    String[] nameArr = XmlUtil.parseElementName(elementName);
	    Element element = doc.getRootElement();
	    //find info , and then find sagInfo in info,
	    //and then find service in sagInfo
	    for (int i = 0; i < nameArr.length; i++) {
	    	element = element.getChild(nameArr[i]);
	    	if(element == null) {
	    		break;
	    	}
	    }
	    //
	    if(element != null) {
	    	result = element.getChildren();
	    }
		return result;
	}
	
	public String getElementText(String elementName) {
	    //
		String value = null;
	    Element element = this.getElement(elementName);
	    if(element != null) {
	    	value = element.getTextTrim();
	    }
	    return value;
	}
	
	public void setElementText(String elementName, String elementText) throws Exception {
	    Element element = this.findCreate(elementName);
	    element.setText(elementText);
	    
	    if(autoSave) {
	    	doSave();
	    }
	}

	@SuppressWarnings("unchecked")
	public void setElementsText(String parentElementName, String childrenElementName, String []elementTextArr) throws Exception {
		if(elementTextArr == null) {
			return;
		}
		
		Element element = this.findCreate(parentElementName);
		List<Element> childrenList = element.getChildren(childrenElementName);
		 //
		Element tempElement = null;
		for(int i = 0; i < elementTextArr.length; i++) {
			tempElement = new Element(childrenElementName);
			tempElement.setText(elementTextArr[i]);
			//
			childrenList.add(tempElement);
		}
		//
	    if(autoSave) {
	    	doSave();
	    }
	}
	
	public void delElement(String elementName) throws Exception {
	    Element element = this.getElement(elementName);
	    if(element != null) {
	    	element.detach();
		    if(autoSave) {
		    	doSave();
		    }
	    }
	}
	
	public String getAttributeValue(String elementName, String attributeName) {
	    //
		String value = null;
	    Element element = this.getElement(elementName);
	    if(element != null) {
	    	value = element.getAttributeValue(attributeName);
	    }
	    return value;
	}

	public void setAttribute(String elementName, String attributeName, String value) throws Exception {
	    Element element = this.findCreate(elementName);
	    element.setAttribute(attributeName, value);
	    if(autoSave) {
	    	doSave();
	    }
	}
	
	public void delAttribute(String elementName, String attributeName) throws Exception {
	    Element element = this.getElement(elementName);
	    if(element != null) {
	    	element.removeAttribute(attributeName);
		    if(autoSave) {
		    	doSave();
		    }
	    }
	}	
}
