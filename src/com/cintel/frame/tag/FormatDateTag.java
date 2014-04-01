package com.cintel.frame.tag;

import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.DateUtils;


/**
 * 
 * @version      1.0
 * @author       WangShuDa
 * @date         2008-07-24
 * @description  FormatDateTag.
 */
public class FormatDateTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(FormatDateTag.class);
	
	protected String inputStr;
	protected String inputFormat = "yyyyMMdd";
	protected String outputFormat = "yyyy-MM-dd";
	
    protected String var;
    
    protected Integer lastDays = null;
    
	public String getInputFormat() {
		return inputFormat;
	}

	public void setInputFormat(String inputFormat) {
		this.inputFormat = inputFormat;
	}

	public String getInputStr() {
		return inputStr;
	}

	public void setInputStr(String inputStr) {
		this.inputStr = inputStr;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	
	public int doEndTag() throws JspException {
        Date date = null;
        String outputStr = null;
		if(inputStr != null && !inputStr.equals("")) {
            date = DateUtils.getDateFromString(inputStr, inputFormat);
            outputStr = DateUtils.getStringFromDate(date, outputFormat);
    	}
        else {
            if(lastDays != null) {
                outputStr = DateUtils.getCalculateTimeStr(outputFormat, DateUtils.DAY_OF_MONTH, lastDays);
            }
        }
        //
        try {
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
            log.error("FormatDateTag Error!", ex);
            
            return SKIP_BODY;
        }
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
    	return SKIP_BODY;
    }

    public Integer getLastDays() {
        return lastDays;
    }

    public void setLastDays(Integer lastDays) {
        this.lastDays = lastDays;
    }

}