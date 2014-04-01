package com.cintel.frame.util.xml.dom4j;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocumentType;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-12-29 wangshuda created
 */
public class XmlToolBox {

	private Document doc = null;

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public XmlToolBox(Document doc) {
		setDoc(doc);
	}

	public XmlToolBox(String xmlString) throws DocumentException {
		doc = XmlUtils.xmlString2Doc(xmlString);
	}

	public XmlToolBox(File file) throws DocumentException {
		doc = XmlUtils.file2Doc(file);
	}

	public XmlToolBox(InputStream stream) {
		doc = XmlUtils.stream2Doc(stream, false);
	}
	public XmlToolBox(){
		
	}
	@SuppressWarnings("unchecked")
	public void delAttribute(String element, String attribute) {
		List<Attribute> attrs = doc.selectNodes("//" + element + "/@" + attribute);
		for (Attribute attr : attrs) {
			attr.detach();
		}
	}

	@SuppressWarnings("unchecked")
	public void delElement(String element) {
		List<Element> els = doc.selectNodes("//" + element);
		for (Element el : els) {
			el.detach();
		}
	}

	public String getAttributeValue(String element, String attribute) {
		Attribute att = (Attribute) doc.selectSingleNode("//" + element + "/@" + attribute);
		if(att != null)
			return att.getValue();
		else
			return null;
	}

	public Element getElement(String element) {
		return (Element) doc.selectSingleNode("//" + element);
	}

	@SuppressWarnings("unchecked")
	public List<Element> getElementList(String element) {
		return (List<Element>) doc.selectNodes("//" + element);
	}

	public String getElementText(String element) {
		return ((Element) doc.selectSingleNode("//" + element)).getText().trim();
	}

	public void setAttribute(String element, String attribute, String attributeValue) {
		Attribute att = (Attribute) doc.selectSingleNode("//" + element + "/@" + attribute);
		if (att == null) {
			((Element) doc.selectSingleNode("//" + element)).addAttribute(attribute, attributeValue);
		} else {
			att.setValue(attributeValue);
		}
	}

	public void setElementText(String element, String text) {
		if (text == null) {
			text = "";
		}
		//
		Element el = (Element) doc.selectSingleNode("//" + element);
		el.setText(text);
	}

	public void setElementText(String parentElement, String element, String text) {
		if (text == null) {
			text = "";
		}
		//
		Element el = (Element) doc.selectSingleNode("//" + parentElement + "/" + element);
		if (el == null) {
			Element parentEl = (Element) doc.selectSingleNode("//" + parentElement);
			el = parentEl.addElement(element);
		}
		//
		el.setText(text);
	}
	
	public void addElementText(String parentElement, String element, String text){
		if (text == null) {
			text = "";
		}
		//
		Element parentEl = (Element) doc.selectSingleNode("//" + parentElement);
		Element el = parentEl.addElement(element);
		//
		el.setText(text);
	}
	
	//
	public void addElement(String parentElementPath, String newElementName, Map<String, String> attributeMap, String text) {
		Element newElement = DocumentHelper.createElement(newElementName);
		Element parentEl = (Element) doc.selectSingleNode("//" + parentElementPath);
		parentEl.add(newElement);
		//
		Set<Entry<String, String>> entrySet = attributeMap.entrySet();
		for(Entry<String, String> entry:entrySet) {
			newElement.addAttribute(entry.getKey(), entry.getValue());
		}
		//
		if(text != null) {
			newElement.setText(text);
		}
	}
	
	public void setDocType(String elementName, String publicID, String systemID) {
		doc.setDocType(new DefaultDocumentType(elementName, publicID, systemID));
	}
}
