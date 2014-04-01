package com.cintel.frame.tag;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @version      1.0
 * @author       
 * @date         2011-01-20
 * @description  FileSizeFormatTag
 */
public class FileSizeFormatTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(FileSizeFormatTag.class);
	//
	protected String fileSize;
	protected String outputFormat;
    protected String var;

	
	public int doEndTag() throws JspException {
        String outputStr = null;
        //
        DecimalFormat df=(DecimalFormat)NumberFormat.getInstance();
    	df.setMaximumFractionDigits(2);
    	//
        try {
        	double n;
            //
            if("KB".equals(outputFormat) || "".equals(outputFormat) || outputFormat == null) {
            	n = Double.parseDouble(fileSize) / 1024;
            	outputStr = df.format(n) + " KB" + " ( " + df.format(Integer.parseInt(fileSize)) + " bytes )";
            }else if("MB".equals(outputFormat)){
            	n = Double.parseDouble(fileSize) / (1024*1024);
            	outputStr = df.format(n) + " MB" + " ( " + df.format(Integer.parseInt(fileSize)) + " bytes )";
            }else if("GB".equals(outputFormat)){
            	n = Double.parseDouble(fileSize) / (1024*1024*1024);
            	outputStr = df.format(n) + " GB" + " ( " + df.format(Integer.parseInt(fileSize)) + " bytes )";
            }
            //
            if(outputStr != null) {
                if(var != null) {
                    pageContext.setAttribute(var, outputStr);
                }
                else {
                    pageContext.getOut().print(outputStr);  
                }
            }
            //
        }
        catch(Exception ex) {
            log.error("FileSizeFormatTag Error!", ex);
            //
            return SKIP_BODY;
        }
        //
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
    	return SKIP_BODY;
    }

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
    
	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

}