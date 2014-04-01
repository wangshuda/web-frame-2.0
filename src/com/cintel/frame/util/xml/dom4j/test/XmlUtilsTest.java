/**
 * filename: XmlUtilsTest.java
 *
 * @author:Xin Wang(wangxin@cintel.net.cn)
 * @Corp:CINtel
 *
 * date:2008-7-25
 * time:ÉÏÎç10:27:40
 *
 */
package com.cintel.frame.util.xml.dom4j.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cintel.frame.util.xml.dom4j.XmlUtils;

public class XmlUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFile2DocString() throws DocumentException {
		assertNotNull(XmlUtils.file2Doc("src/com/cintel/frame/util/xml/dom4j/test/test.xml"));
	}

	@Test
	public void testFile2DocFile() throws DocumentException {
		File file = new File("src/com/cintel/frame/util/xml/dom4j/test/test.xml");
		assertNotNull(XmlUtils.file2Doc(file));
	}

	@Test
	public void testGetAttributeValue() {

	}

	@Test
	public void testStream2Doc() throws FileNotFoundException {
		FileInputStream inputStream = new FileInputStream("src/com/cintel/frame/util/xml/dom4j/test/test.xml");
		assertNotNull(XmlUtils.stream2Doc(inputStream, false));
	}

	@Test
	public void testDoc2FileDocumentFile() throws Exception {
		Document doc = XmlUtils.file2Doc("src/com/cintel/frame/util/xml/dom4j/test/test.xml");
		File file = new File("src/com/cintel/frame/util/xml/dom4j/test_back.xml");
		XmlUtils.doc2File(doc, file);
		assertNotNull(file);
	}

	@Test
	public void testDoc2FileDocumentFileString() throws Exception {
		Document doc = XmlUtils.file2Doc("src/com/cintel/frame/util/xml/dom4j/test/test.xml");
		File file = new File("src/com/cintel/frame/util/xml/dom4j/test_file.xml");
		XmlUtils.doc2File(doc, file, null);
		assertNotNull(file);
	}

	@Test
	public void testDoc2OutputStream() throws Exception {
		Document doc = XmlUtils.file2Doc("src/com/cintel/frame/util/xml/dom4j/test/test.xml");
		FileOutputStream out = new FileOutputStream("src/com/cintel/frame/util/xml/dom4j/test_outputStream.xml");
		XmlUtils.doc2OutputStream(doc, out);
	}

	@Test
	public void testDoc2Writer() throws Exception {
		Document doc = XmlUtils.file2Doc("src/com/cintel/frame/util/xml/dom4j/test/test.xml");
		Writer file = new OutputStreamWriter(new FileOutputStream("src/com/cintel/frame/util/xml/dom4j/test_writer.xml"),"utf-8");
		XmlUtils.doc2Writer(doc, file);
	}

	@Test
	public void testDoc2XmlString() throws DocumentException {
		Document doc = XmlUtils.file2Doc("src/com/cintel/frame/util/xml/dom4j/test/test.xml");
		assertNotNull(XmlUtils.doc2XmlString(doc));
		System.out.print(XmlUtils.doc2XmlString(doc));
	}

	@Test
	public void testXmlString2Doc() throws DocumentException {
		Document doc = XmlUtils.xmlString2Doc("<hello>ÍõÐÂ</hello>");
		assertNotNull(doc);
		System.out.print(XmlUtils.doc2XmlString(doc));
	}

}
