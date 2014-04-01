package com.cintel.frame.util.xml.dom4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cintel.frame.util.StringUtils;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-12-29 wangshuda created
 */
public class XmlUtils {
	protected static Log log = LogFactory.getLog(XmlUtils.class);
	//
	public static Document file2Doc(String filePath) throws DocumentException {
		return file2Doc(new File(filePath));
	}

	public static Document file2Doc(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		return document;
	}

	public static String getAttributeValue(Node node, String attribute, String defaultValue) {
		String attributeValue = node.valueOf("@" + attribute);
		if (attributeValue == null) {
			attributeValue = defaultValue;
		}
		return attributeValue;
	}

	public static Document stream2Doc(InputStream stream, boolean validate) {
		Document doc = null;
		try {
			SAXReader reader = new SAXReader(validate);
			doc = reader.read(stream);
		}
		catch (DocumentException ex) {
			log.error("", ex);
			doc = null;
		}
		return doc;
	}

	public static synchronized void doc2File(Document doc, File file) throws Exception {
		doc2File(doc, file, null);
	}

	public static synchronized void doc2File(Document doc, File file, String encode) throws Exception {
		XMLWriter writer = null;
		try {
			//
			if (StringUtils.hasText(encode)) {
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding(encode);
				//
				writer = new XMLWriter(new FileOutputStream(file), format);
			}
			else {
				writer = new XMLWriter(new FileOutputStream(file));
			}
			writer.write(doc);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (writer != null) {
				writer.close();
				writer = null;
			}
		}
	}

	/**
     * @author wangxin(wangxin@cintel.net.cn)
     */
	public static synchronized void doc2OutputStream(Document doc, OutputStream out) throws IOException {
		XMLWriter writer = new XMLWriter(out);
		writer.write(doc);
		writer.close();
	}

	/**
     * @author wangxin(wangxin@cintel.net.cn)
     */
	public static synchronized void doc2Writer(Document doc, Writer out) throws IOException {
		// OutputFormat format = OutputFormat.createPrettyPrint();
		// format.setEncoding(doc.getXMLEncoding());
		XMLWriter writer = new XMLWriter(out);
		writer.write(doc);
		writer.close();
	}

	/**
     * @author wangxin(wangxin@cintel.net.cn)
     */
	public static synchronized String doc2XmlString(Document doc) {
		return doc.asXML();
	}

	/**
     * @author wangxin(wangxin@cintel.net.cn)
     */
	public static synchronized Document xmlString2Doc(String xmlString) throws DocumentException {
		return DocumentHelper.parseText(xmlString);
	}

	public static void main(String args[]) {
		Document doc = DocumentFactory.getInstance().createDocument();
		System.out.println(doc.asXML());
	}
}
