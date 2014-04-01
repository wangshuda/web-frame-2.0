package com.cintel.frame.tag;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.DateUtils;


/**
 * 
 * @version      1.0
 * @author       WangShuDa
 * @date         2008-07-24
 * @description  FormatEffectDateTag.
 */
public class FormatEffectDateTag extends FormatDateTag {
	private static final long serialVersionUID = -2017708476601522968L;

	private Log log = LogFactory.getLog(FormatEffectDateTag.class);
	
	private int effectDays = 2*30;
	private String invalidCssClass = "invalidEffect";
	
	public String getInvalidCssClass() {
		return invalidCssClass;
	}

	public void setInvalidCssClass(String invalidCssClass) {
		this.invalidCssClass = invalidCssClass;
	}

	public int getEffectDays() {
		return effectDays;
	}

	public void setEffectDays(int effectDays) {
		this.effectDays = effectDays;
	}

	public int doEndTag() throws JspException {
		if(inputStr != null && !inputStr.equals("")) {
			try {
				//
				StringBuffer bufferStr = new StringBuffer();
	    		Date inputDateTime = DateUtils.getDateFromString(inputStr, inputFormat);
	    		//
	    		String outputDateTimeStr = DateUtils.getStringFromDate(inputDateTime, outputFormat);
	    		
	    		String effectDateTimeStr = DateUtils.getCalculateTimeStr(inputFormat, Calendar.DATE, effectDays);
	    		
	    		if(inputStr.compareTo(effectDateTimeStr) < 0) {
	    			bufferStr.append("<font class='" + invalidCssClass +"'>");
	    			bufferStr.append(outputDateTimeStr);
	    			bufferStr.append("</font>");
	    		}
	    		else {
	    			bufferStr.append(outputDateTimeStr);
	    		}
	    		//
	    		pageContext.getOut().print(bufferStr.toString());
	    	}
	    	catch(Exception ex) {
	    		log.error("FormatEffectDateTag Error!", ex);
	    		
	    		return SKIP_BODY;
	    	}
    	}
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
    	return SKIP_BODY;
    }

}