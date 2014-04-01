package com.cintel.frame.util.xml.jdom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

public class TestXmlToolBox {

	/**
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		InputStream templateStream = new FileInputStream(new File("E:\\JavaProject\\crm-bjcnc\\src\\com\\cintel\\crm\\batch\\BatchRequestTemplate.xml"));
		
		Document requestDoc = XmlUtil.stream2Doc(templateStream, false);
		
		XmlToolBox xmlBox = new XmlToolBox(requestDoc);
		xmlBox.setElementText("operation", "1");
		xmlBox.setElementText("ringType","1");
		xmlBox.setElementText("ringID", "1");
		//
		InputStream phones = new FileInputStream(new File("D:\\home\\web\\jakarta-tomcat-5.0.28\\webapps\\crdata\\batch-xml\\test-phone.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(phones));
		//
		String tempLineStr = null;
		ArrayList<String> phoneNumberArr = new ArrayList<String>();
		while ((tempLineStr = reader.readLine()) != null) {
			phoneNumberArr.add(tempLineStr);
		}
		//
		
		Element element = xmlBox.getElement("userNumberList");
		List list = element.getChildren("userNumber");
		
		Element element2 = new Element("userNumber");
		element2.setText("11111111111");
		
		list.add(0, element2);
		Element element3 = new Element("userNumber");
		element3.setText("222222222222");
		
		list.add(1, element3);
		//xmlBox.setElementsText("userNumberList\\userNumber", phoneNumberArr.toArray(new String[phoneNumberArr.size()]));
		//
		String fileLocation = "D:\\home\\web\\jakarta-tomcat-5.0.28\\webapps\\crdata\\batch-xml\\a.xml";
		File file = new File(fileLocation);
		file.createNewFile();
		//
		XmlUtil.doc2File(xmlBox.getDoc(), fileLocation);
	}

}
