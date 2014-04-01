package com.cintel.frame.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-11-2 wangshuda created
 */
public class ConvertUtils {
    private static Log log = LogFactory.getLog(ConvertUtils.class);
    
    public static String unicodeToGb2312(String str) {
        String[] strArr = str.split("&#x");
        StringBuffer reStrBuf = new StringBuffer();
        int a = 0;
        try {
            for (int i = 0; i < strArr.length; i++) {
                if (strArr[i].indexOf(";") == -1) {
                    reStrBuf.append(strArr[i]);
                    continue;
                }
                if (strArr[i].length() != 5) {
                    String[] strArrSub = strArr[i].split(";");
                    
                    if(strArrSub != null) {
                        int subStrLen =strArrSub[0].length();
                        strArrSub[0] = strArrSub[0].substring(0, subStrLen < 4 ? subStrLen : 4); // Modified by wangshuda 2011/11/02
                        
                        a = Integer.parseInt(strArrSub[0], 16);
                        char strChar = (char) a;
                        reStrBuf.append(String.valueOf(strChar));
                        if(strArrSub.length > 1) { // be sure then length is 2.
                            reStrBuf.append(strArrSub[1]);
                        }
                    }
                }
                else {
                    strArr[i] = strArr[i].substring(0, 4);
                    a = Integer.parseInt(strArr[i], 16);
                    char strChar = (char) a;
                    reStrBuf.append(String.valueOf(strChar));
                }
            }
        }
        catch(Exception ex) {
            log.error("", ex);
        }

        return reStrBuf.toString();
    }
    
	public static int str2Int(String str) {
		return str2Int(str, 0);
	}
	
	public static int str2Int(String str, int defaultVal) {
		int retVal = defaultVal;
		if(StringUtils.hasText(str)) {
			retVal = Integer.valueOf(str);
		}
		return retVal;
	}
    // 
    private static String _ASCII_PREFIX = "\\u";

    private static char ascii2Char(String str) {
        if (str.length() != 6) {
            throw new IllegalArgumentException("Ascii string of a native character must be 6 character.");
        }
        if (!_ASCII_PREFIX.equals(str.substring(0, 2))) {
            throw new IllegalArgumentException("Ascii string of a native character must start with \"\\u\".");
        }
        String tmp = str.substring(2, 4);
        int code = Integer.parseInt(tmp, 16) << 8;
        tmp = str.substring(4, 6);
        code += Integer.parseInt(tmp, 16);
        return (char) code;
    }

    public static String ascii2Native(String str) {
        StringBuffer sb = new StringBuffer();
        //
        try {
            if(str != null) {
                int begin = 0;
                int index = str.indexOf(_ASCII_PREFIX);
                int strLen = str.length();
                while (index != -1) {
                    sb.append(str.substring(begin, index));
                    if(index + 6 < strLen) {
                        sb.append(ascii2Char(str.substring(index, index + 6)));
                        begin = index + 6;
                    }
                    else {
                        begin = index + 1;
                    }
                    
                    index = str.indexOf(_ASCII_PREFIX, begin);
                }
                //
                sb.append(str.substring(begin));
            }
        }
        catch(Exception ex) {
            log.error(str, ex);
        }
        //
        return sb.toString();
    }
    
    /**
     * 
     * @param str
     * @return
     */
    public static String native2Ascii(String str) {
        if(str == null) {
            return null;
        }
        //
        char[] chars = str.toCharArray();        
        StringBuffer sb = new StringBuffer();        
        for (int i = 0; i < chars.length; i++) {        
            sb.append(char2Ascii(chars[i]));        
        }        
        return sb.toString();        
    }        

    private static String char2Ascii(char c) {
        if (c > 255) {
            StringBuffer sb = new StringBuffer();
            sb.append(_ASCII_PREFIX);
            int code = (c >> 8);
            String tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            code = (c & 0xFF);
            tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            return sb.toString();
        }
        else {
            return Character.toString(c);
        }
    }
    
    /**
     * Title:convert hexStr to ByteArr.
     * 
     * @author  wangshuda
     * @version 1.0
     * @date    2006-12-04
     * @param   hexStr
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
     * @date    2006-12-04
     * @param   originByteArr
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
    
    public static String encode2Base64(String origStr) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String rtnMsg = base64Encoder.encode(origStr.getBytes());
        //
        return rtnMsg;
    }
    
    public static String decodeFromBase64(String base64CodeStr) throws IOException {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        return new String(base64Decoder.decodeBuffer(base64CodeStr));
    }
    
    public static void  main(String args[]) {
        System.out.println(ConvertUtils.ascii2Native("\u52a8\u611f\u5730\u5e26,n\\ulall"));
        
        System.out.println(ConvertUtils.unicodeToGb2312("&#x53F7;&#x5750;&#x5E2D;"));
        System.out.println(ConvertUtils.unicodeToGb2312("&#x52a8;"));
        //&#x53F7;&#x5750;&#x5E2D;
    }
}
