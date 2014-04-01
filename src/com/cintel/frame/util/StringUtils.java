package com.cintel.frame.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @file    : StringUtils.java
 * @author  : WangShuDa
 * @date    : 2008-9-4
 * @corp    : CINtel
 * @version : 1.0
 */
public class StringUtils {	
	public static boolean hasLength(String str) {
		return (str != null && str.length() > 0);
	}
	
	public static boolean hasText(String str) {
		return hasLength(str);
	}
	
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return org.springframework.util.StringUtils.tokenizeToStringArray(str, delimiters);
    }
    
    public static String[] commaDelimitedListToStringArray(String str) {
        return org.springframework.util.StringUtils.commaDelimitedListToStringArray(str);
    }
    
    public static boolean hasNoneBlankText(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
	public static boolean match(String str, String regStr) {
		Pattern pattern = Pattern.compile(regStr, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static String replaceAll(String str, String regStr, String newStr) {
		Pattern pattern = Pattern.compile(regStr, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll(newStr);
	}
	
	public static String[] split(String text, String regStr) {
		Pattern pattern = Pattern.compile(regStr, Pattern.CASE_INSENSITIVE);
		return pattern.split(text);
	}
}
