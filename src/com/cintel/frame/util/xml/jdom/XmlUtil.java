package com.cintel.frame.util.xml.jdom;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

import org.xml.sax.InputSource;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class XmlUtil {
	public static final String nameSeparator = "\\\\";
	/**
	 * Title: file2Doc
	 * 
	 * @param  file
	 * @param  validate
	 * @return
	 * @author wangshuda
	 */
	public static Document file2Doc(File file, boolean validate) {
		Document doc = null;
    	try {
			SAXBuilder builder = new SAXBuilder(validate);
	    	doc = builder.build(file);
    	}
    	catch(Exception ex) {
    		doc = null;
    	}
    	return doc;
	}
	
	public static Document file2Doc(String filePath, boolean validate) {
		File file = new File(filePath);
		return file2Doc(file, validate);
	}
	
	public static Document file2Doc(String filePath) {
		File file = new File(filePath);
		return file2Doc(file, false);
	}
	
	public static Document file2Doc(File file) {
		return file2Doc(file, false);
	}
	
	public static Document stream2Doc(InputStream stream, boolean validate) {
		Document doc = null;
    	try {
			SAXBuilder builder = new SAXBuilder(validate);
	    	doc = builder.build(stream);
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    		doc = null;
    	}
    	return doc;
	}
	
	public static Document inputSource2Doc(InputSource inputSource, boolean validate) {
		Document doc = null;
    	try {
			SAXBuilder builder = new SAXBuilder(validate);
	    	doc = builder.build(inputSource);
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    		doc = null;
    	}
    	return doc;
	}

	public static Document xmlStr2Doc(String xmlStr, boolean validate) throws Exception {
		Document doc = null;
    	try {
    	    String disposedStr = disposeXmlStr(xmlStr);
 		   
    	    StringReader strReader = new StringReader(disposedStr);
    	    InputSource inputSource= new InputSource(strReader);
    	    //
			SAXBuilder builder = new SAXBuilder(validate);
	    	doc = builder.build(inputSource );
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    		doc = null;
    	}
    	return doc;
	}
	
	public static synchronized void doc2File(Document doc, File file) throws Exception {
		FileWriter writer = null;
		try {
		    XMLOutputter out = new XMLOutputter();
		    //
	    	writer = new FileWriter(file);
	    	out.output(doc, writer);
	    	
	    }
	    catch (Exception ex) {
	      throw ex;
	    }
	    finally {
	    	if(writer != null) {
	    		writer.close();
	    		writer = null;
	    	}
	    }
	}
	
	public static synchronized void doc2OutputStream(Document doc, OutputStream stream) throws Exception {
	    XMLOutputter out = new XMLOutputter();
	    //
    	out.output(doc, stream);
	}
	
	public static synchronized void doc2Writer(Document doc, Writer writer) throws Exception {
	    XMLOutputter out = new XMLOutputter();
	    //
    	out.output(doc, writer);
	}
	
	public static synchronized void doc2Writer(Document doc, Writer writer, String encoding) throws Exception {
		Format format = Format.getCompactFormat();
		format.setEncoding(encoding);
		format.setLineSeparator("\n");
		XMLOutputter out = new XMLOutputter(format);
	    //
		
    	out.output(doc, writer);
	}
	
	public static synchronized String doc2String(Document doc, String encoding) throws Exception {
		StringWriter writer = new StringWriter();
		doc2Writer(doc, writer);
    	return writer.toString();
	}
	
	public static synchronized void doc2File(Document doc, String filePath) throws Exception {
		File file = new File(filePath);
		doc2File(doc, file);
	}
	
	public static String[] parseElementName(String elementName) {
		return elementName.split(nameSeparator);
	}
	
	
	public static String disposeXmlStr(String originStr) {
		String result = null;
		if(originStr != null) {
			result = originStr.replaceAll("\"", "\\\"");
		}
		return result;
	}
}
