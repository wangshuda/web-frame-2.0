package com.cintel.frame.tag;

import java.util.Calendar;
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
 * @description  CheckEffectDateTag.
 */
public class CheckEffectDateTag extends TagSupport {
	private static final long serialVersionUID = 6294032789411906953L;

	private Log log = LogFactory.getLog(CheckEffectDateTag.class);

	protected String inputStr;

	protected String inputFormat = "yyyyMMdd";

	private int effectDays = 0;

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

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		if (inputStr != null && !inputStr.equals("")) {
			try {
				Date inputDateTime = DateUtils.getDateFromString(inputStr, inputFormat);
				if (inputDateTime == null) {
					return SKIP_BODY;
				}
				//
				String effectDateTimeStr = DateUtils.getCalculateTimeStr(inputFormat,
						Calendar.DATE, effectDays);
				if (inputStr.compareTo(effectDateTimeStr) < 0) {
					log.debug("SKIP_BODY");
					return SKIP_BODY;
				}
			} catch (Exception ex) {
				log.error("CheckEffectDateTag", ex);

				return SKIP_BODY;
			}
		}
		return EVAL_BODY_INCLUDE;
	}

}