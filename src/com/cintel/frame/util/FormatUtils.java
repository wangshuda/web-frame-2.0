package com.cintel.frame.util;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import sun.misc.BASE64Encoder;

/**
 * @version $Id: FormatUtils.java 37170 2012-11-27 09:30:30Z wangshuda $
 */
public class FormatUtils {	
	public static class EncryptNumInfo {
		public int beginIndex;
		public int starCharCnt;
		
		public EncryptNumInfo(int beginIndex, int starCharCnt) {
			this.beginIndex = beginIndex;
			this.starCharCnt = starCharCnt;
		}
	}

	private static EncryptNumInfo[] encryptNumArr =  new EncryptNumInfo[]{
			new EncryptNumInfo(0, 0) // 0
			,new EncryptNumInfo(0, 0) // 1
			,new EncryptNumInfo(1, 1) // 2
			,new EncryptNumInfo(2, 1) // 3
			,new EncryptNumInfo(2, 2) // 4
			,new EncryptNumInfo(2, 2) // 5
			,new EncryptNumInfo(2, 2) // 6
			,new EncryptNumInfo(2, 3) // 7
			,new EncryptNumInfo(3, 3) // 8
			,new EncryptNumInfo(3, 3) // 9
			,new EncryptNumInfo(3, 4) // 10
			,new EncryptNumInfo(3, 4) // 11
            ,new EncryptNumInfo(4, 4) // 12
            ,new EncryptNumInfo(4, 6) // 13
            ,new EncryptNumInfo(5, 5) // 14
	};
	
	private static final String _STAR_STR = "***********";
	
	public static String encryptedPhoneNumber(String phoneNumber) {
		String rtnStr = phoneNumber;
		if(StringUtils.hasText(phoneNumber)) {
			int strLen = phoneNumber.length();
			int numArrIndex = strLen >= 14 ? 14 : strLen;
			int beginIndex = encryptNumArr[numArrIndex].beginIndex;
			int starCharCnt = encryptNumArr[numArrIndex].starCharCnt;
			//
			if(beginIndex > 0 && starCharCnt > 0) {
				rtnStr = MessageFormat.format("{0}{1}{2}", new Object[] {
									 phoneNumber.substring(0, beginIndex)
									,_STAR_STR.substring(0, starCharCnt)
									,phoneNumber.substring(beginIndex + starCharCnt)});
			}
		}
		return rtnStr;
	}
	/**
	 * Title:convert hexStr to ByteArr.
	 * 
	 * @author  wangshuda
	 * @version 1.0
	 * @date 	2006-12-04
	 * @param 	hexStr
	 * @return
	 */
	public static byte[] hexStr2ByteArr(String hexStr) {
		char[] originCharArr = hexStr.toCharArray();
		byte[] resultArr = new byte[hexStr.length() / 2];
		String swap = null;
		for (int i = 0; i < hexStr.length(); i += 2) { 
			swap = new String(originCharArr, i, 2);
			resultArr[i/2] = (byte)Integer.parseInt(swap, 16); 
		}
		return resultArr; 

    }
    
	/**
	 * Title:convert byteArr to HexStr.
	 * 
	 * @author  wangshuda
	 * @version 1.0
	 * @date 	2006-12-04
	 * @param 	originByteArr
	 * @return
	 */
	public static String byteArr2HexStr(byte[] originByteArr) {
    	String tmpStr = null;
    	StringBuffer retVal = new StringBuffer();
        
        for(int n = 0; n < originByteArr.length; n++){
        	tmpStr = Integer.toHexString(originByteArr[n] & 0xFF);        	
            if(tmpStr.length() == 1) {
            	retVal.append("0" + tmpStr);
            }
            else {
            	retVal.append(tmpStr);
            }
        }
        return retVal.toString();
    }
	
	/**
	 * encode
	 * 
	 * @param pStr
	 * @param newCode
	 * @param oldCode
	 * @return
	 */
	public static String encode(String pStr, String newCode, String oldCode) {
		try {
			if(pStr == null) {
				return "";
			}
			else {
				pStr = new String(pStr.getBytes(newCode), oldCode);
			}
		}
		catch (Exception e) {
		}
		return pStr;
	}
			
	/**
	 * escapeForSql
	 * 
	 * @param pStr
	 * @param mode, true:use it before execute sql. false: use it after execute sql.
	 * 				keep the parameter map value to be the same after execute the sql.
	 * @return
	 */
	public static String escapeForSql(String pStr, boolean mode) {
		if(pStr == null) {
			return "";
		}
		else {
			if(mode) {
				pStr = pStr.replaceAll("'", "''");
				pStr = pStr.replaceAll("%", "\\\\%");
			}
			else {
				pStr = pStr.replaceAll("''", "'");
				pStr = pStr.replaceAll("\\\\%", "%");
			}
		}
		return pStr;
	}
	
	/**
	 * escapeForSql
	 * 
	 * @param map
	 * @return
	 */
	public static void escapeForSql(Map<String, String> map, boolean mode) {
		if(map != null) {
			String key;
			String value;
			Iterator keysIter = map.keySet().iterator();
			Object valueObj = null;
			while(keysIter.hasNext()) {
				key = keysIter.next().toString();
				valueObj = map.get(key);
				//
				if(valueObj instanceof String) {
					value = String.valueOf(valueObj);
					map.put(key, escapeForSql(value, mode));
				}
				else {
					//
				}
			}
		}
		return;
	}
	
	/**
	 * formatNumber
	 * 
	 * @param value : it's type should be Integer or Double
	 * @param format : just like '0000' or '0000.00' and so on.
	 * @return
	 */
	public static String formatNumber(Object value, String format, Object defaultVal) {
		String rtnValue = "";
		if(value == null) {
			value = defaultVal;
		}
		
		DecimalFormat decimalFormat = new DecimalFormat(format);
		if(value instanceof Integer) {
			rtnValue = decimalFormat.format(((Integer)value).doubleValue());
		}
		else if(value instanceof Double) {
			rtnValue = decimalFormat.format(((Double)value).doubleValue());
		}
		return rtnValue;
	}

	/**
	 * formatId
	 * 
	 * @param id
	 * @param idLength
	 * @return
	 */
	public static String formatId(Object id, int idLength) {
		String rtnValue = "";
		StringBuffer format = new StringBuffer();
		for(int i = 0; i < idLength; i ++) {
			format.append('0');
		}
		rtnValue = formatNumber(id, format.toString(), 0);
		return rtnValue;
	}

    public static boolean isNumeric(String str) {
        boolean rtnValue = false;
        if(StringUtils.hasLength(str)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            //
            rtnValue = isNum.matches();
        }
        return rtnValue;
    }
        
    public static void main(String args[]) {
        String testStr = "{'id':'123','name':'ÍõÊ÷´ó','remark':'abcde'}";
        //
        BASE64Encoder encoder = new BASE64Encoder();
        
        String hexStr = FormatUtils.byteArr2HexStr(testStr.getBytes());
        System.out.println(hexStr);
        System.out.println(new String(FormatUtils.hexStr2ByteArr(hexStr)));
        System.out.println(encoder.encode(testStr.getBytes()));
    }
}
