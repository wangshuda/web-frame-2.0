package com.cintel.frame.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;



/**
 * 
 * @version 
 * @history 
 *          1.0.0 2012-9-17 wangshuda created
 */
public final class PinYinUtils {
    protected static Log log = LogFactory.getLog(PinYinUtils.class);
            
    private static HanyuPinyinOutputFormat pyOutputFmt = null;
        
    static {
        pyOutputFmt = new HanyuPinyinOutputFormat();
        //
        pyOutputFmt.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }
    
    public static String buildChar4FirstPy(char paraChar) {
        String firstCaptialPy = null;
        String pyStr = buildChar2Py(paraChar, true);
        if(StringUtils.hasLength(pyStr)) {
            firstCaptialPy = StringUtils.capitalize("" + pyStr.charAt(0));
        }
        return firstCaptialPy; 
    }
    
    public static String buildChar2Py(char paraChar, boolean firstCaptial) {
        String[] resultStrArr = null;
        try {
            // if paraChar is not chinese, toHanyuPinyinStringArray will return null.
            resultStrArr = PinyinHelper.toHanyuPinyinStringArray(paraChar, pyOutputFmt);
        }
        catch(BadHanyuPinyinOutputFormatCombination ex){
            log.warn("", ex);
        }
        String rtnStr =  null;//?  : null;
        if(resultStrArr != null) {
            rtnStr = resultStrArr[0];
            //
            if(StringUtils.hasLength(rtnStr) && firstCaptial) {
                if(rtnStr.length() == 1) {
                    rtnStr = StringUtils.capitalize("" + rtnStr.charAt(0));
                }
                else {
                    rtnStr = StringUtils.capitalize("" + rtnStr.charAt(0)) + rtnStr.substring(1);
                }
            }
        }
        // only return the first result.
        return rtnStr;  
    }
    
    public static String[] buildChar2PyArr(char paraChar) {
        String[] resultStrArr = null;
        try {
            // if paraChar is not chinese, toHanyuPinyinStringArray will return null.
            resultStrArr = PinyinHelper.toHanyuPinyinStringArray(paraChar, pyOutputFmt);
        }
        catch(BadHanyuPinyinOutputFormatCombination ex){
            log.warn("", ex);
        }
        // only return the first result.
        return resultStrArr != null ? resultStrArr : new String[]{};  
    }
    
    public static String buildStr2Py(String paraStr) {
        return buildStr2Py(paraStr, " ", true, false, false);
    }
    
    public static String buildStr2Py(String paraStr, String intervalPara, boolean firstCaptial, boolean onlyFirst, boolean keepUnChn) {
        StringBuilder rtnBuffer = new StringBuilder();
        //
        if(StringUtils.hasLength(paraStr)) {
            String resultPyStr = null;
            int paraLen = paraStr.length();
            char loopChar;
            boolean linkInterval = false;
            for(int i = 0; i < paraLen; ++i) {
                linkInterval = false;
                loopChar = paraStr.charAt(i);
                if(!onlyFirst) {
                    resultPyStr = buildChar2Py(loopChar, firstCaptial);
                }
                else {
                    resultPyStr = buildChar4FirstPy(loopChar);
                }
                //
                if (resultPyStr == null) {
                    if(!onlyFirst && keepUnChn) {
                        linkInterval = true;
                        rtnBuffer.append(loopChar);
                    }
                }
                else {
                    linkInterval = true;
                    rtnBuffer.append(resultPyStr);
                }
                //
                if(i != paraLen - 1 && linkInterval) {
                    rtnBuffer.append(intervalPara);
                }
            }
        }
        //
        return rtnBuffer.toString();
    }
    
    public static String buildStr4FirstCaptialPy(String paraStr) {
        return buildStr2Py(paraStr, "", true, true, false);
    }
    
    public static void main(String args[]) {
        System.out.println(PinYinUtils.buildStr4FirstCaptialPy("鹅！ ,@#￥%副井口信号室兖矿菏泽能化公司"));
        
        System.out.println(PinYinUtils.buildStr2Py("鹅！ ,@#￥%副井口信号室兖矿菏泽能化公司"));
        
        System.out.println(PinYinUtils.buildStr2Py("鹅！ ,@#￥%副井口信号室兖矿菏泽能化公司", " ", true, true, false));
        
    }
}
