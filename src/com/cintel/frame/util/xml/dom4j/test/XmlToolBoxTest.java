/**
 * filename: XmlToolBoxTest.java
 *
 * @author:Xin Wang(wangxin@cintel.net.cn)
 * @Corp:CINtel
 *
 * date:2008-7-25
 * time:ÏÂÎç02:59:07
 *
 */
package com.cintel.frame.util.xml.dom4j.test;

import static org.junit.Assert.*;
import static java.lang.System.out;
import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cintel.frame.util.xml.dom4j.XmlToolBox;
import com.cintel.frame.util.xml.dom4j.XmlUtils;

public class XmlToolBoxTest {

	XmlToolBox box;

	@Before
	public void setUp() throws Exception {
		box = new XmlToolBox(new File("src/com/cintel/frame/util/xml/dom4j/test/test.xml"));
	}

	@After
	public void tearDown() throws Exception {

	}

	//@Test
	public void testDelAttribute() {
		box.delAttribute("employer", "sex");
		assertNotNull(XmlUtils.doc2XmlString(box.getDoc()));
		out.println(XmlUtils.doc2XmlString(box.getDoc()));
	}

	//@Test
	public void testDelElement() {
		box.delElement("employer");
		assertNotNull(XmlUtils.doc2XmlString(box.getDoc()));
		out.println(XmlUtils.doc2XmlString(box.getDoc()));
	}

	//@Test
	public void testGetAttributeValue() {
		assertEquals("male", box.getAttributeValue("employer", "sex"));
		out.println(box.getAttributeValue("employer", "sex"));
	}

	//@Test
	public void testGetElement() {
		assertNotNull(box.getElement("employer"));
		out.println(box.getElement("employer"));
	}

	//@Test
	public void testGetElementList() {
		assertNotNull(box.getElementList("manager"));
		out.println(box.getElementList("manager"));
	}

	//@Test
	public void testGetElementText() {
		assertEquals("ÍõÐÂ", box.getElementText("employer"));
		out.println(box.getElementText("employer"));
	}

	 //@Test
	public void testSetAttribute() {
		box.setAttribute("employer", "age", "22");
		out.println(XmlUtils.doc2XmlString(box.getDoc()));
	}

	@Test
	public void testSetAttribute2() {
		box.setElementText("corp","cc", "cctext");
		box.setAttribute("cc", "cca", "female");
		out.println(XmlUtils.doc2XmlString(box.getDoc()));
	}

	//@Test
	public void setElementText() {
		box.setElementText("employer", "Alice");
		out.println(XmlUtils.doc2XmlString(box.getDoc()));
	}

	//@Test
	public void setElementTextParent() {
		box.setElementText("corp", "address", "xizhimen");
		out.println(XmlUtils.doc2XmlString(box.getDoc()));
	}
}
