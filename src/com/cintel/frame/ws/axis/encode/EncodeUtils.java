package com.cintel.frame.ws.axis.encode;

import com.cintel.frame.util.ConvertUtils;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-6-22 wangshuda created
 */
public class EncodeUtils {
    public static String unicodeToGb2312(String str) {
        return ConvertUtils.unicodeToGb2312(str);
    }
    
    public static void main(String args[]) {
        System.out.println(unicodeToGb2312("&#x7B2C;&#x4E00;&#x4F01;&#x4E1A;&#xB7;&#x7B2C;&#x4E00;&#x4EBA;&#x5DE5;&#x670D;&#x52A1;"));
    	
    }
}
