package com.cintel.frame.util;

import java.util.Random;
import java.util.UUID;

/**
 * 
 * @file    : RandomUtils.java
 * @author  : WangShuDa
 * @date    : 2008-10-15
 * @corp    : CINtel
 * @version : 1.0
 */
public class RandomUtils {

	private static long randomSeed = Long.MIN_VALUE;
	
	/**
	 * getRandomSeed
	 * 
	 * @return randomSeed
	 */
	private static synchronized long getRandomSeed() {
		randomSeed = (randomSeed + 1) % Long.MAX_VALUE;
		return randomSeed;
	}

	private static String getRandomStr(String seedStr, int length) {
	    StringBuffer resultRandStr = new StringBuffer();
	    int randNum = 0;
	    Random rand = new Random(getRandomSeed());
	    int randStrLen = seedStr.length();
	    for (int i = 0; i < length; i++) {
	    	randNum = rand.nextInt(randStrLen);
	    	resultRandStr.append(seedStr.substring(randNum, randNum + 1));
	    }
	    return resultRandStr.toString() ;
	}
    
    public static String genRandomStr(String seedStr, int length) {
        return getRandomStr(seedStr, length);
    }
	/**
	 * GenerateRandomStr
	 * 
	 * @param length
	 * @return
	 */
	public static String generateRandomNumberStr(int length) {
	    String seedStr = "0123456789";
	    return getRandomStr(seedStr, length);
	}
	
	/**
	 * GenerateRandomStr
	 * 
	 * @param length
	 * @return
	 */
	public static String generateRandomStr(int length) {
	    String seedStr = "ABCDEFGHIJKLMNOPQRSTUVWSYZabcdefghijklmnopqrstuvwsyz0123456789";
	    return getRandomStr(seedStr, length);
	}
	
	/**
	 * 
	 * @method: generateRandomInt
	 * @return: int
	 * @author: WangShuDa
	 * @param maxValue
	 * @return
	 */
	public static int generateRandomInt(int maxValue) {
		Random rand = new Random(getRandomSeed());
		return rand.nextInt(maxValue);
	}
	
	/**
	 * replace with getStreamNo23 or getCorrelator32 please.
	 * getStreamNo
	 * @return random(2bit) + yyyyMMddHHmmss + random(4bit)
	 */
	@Deprecated
	public final static String getStreamNo20() {
		return generateTimeStampRandomStr(1, 2);
	}

	public final static String getStreamNo23() {
		return generateTimeStampRandomStr(2, 4);
	}
	
	/**
	 * getCorrelator32
	 * @return random(2bit) + yyyyMMddHHmmss + random(2bit)
	 */
	public static String getCorrelator32() {
		return generateTimeStampRandomStr(9, 6);
	}

	/**
	 * 
	 * @method: generateTimeStampRandomStr
	 * @return: String
	 * @author: WangShuDa
	 * @param randomLenBefore
	 * @param randomLenAfter
	 * @param timeFormat: yyyyMMddHHmmssSSS
	 * @return
	 */
	public static String generateTimeStampRandomStr(int randomLenBefore, int randomLenAfter, String timeFormat) {
		StringBuffer resultStr = new StringBuffer();
		resultStr.append(generateRandomStr(randomLenBefore));
		resultStr.append(DateUtils.getCurrentTimeString(timeFormat));
		resultStr.append(generateRandomStr(randomLenAfter));
		return resultStr.toString();
	}
	
	/**
	 * 
	 * @method: generateTimeStampRandomStr
	 * @return: String
	 * @author: WangShuDa
	 * @param randomLenBefore
	 * @param randomLenAfter
	 * @return generateTimeStampRandomStr(randomLenBefore, randomLenAfter, "yyyyMMddHHmmss");
	 */
	public static String generateTimeStampRandomStr(int randomLenBefore, int randomLenAfter) {
		return generateTimeStampRandomStr(randomLenBefore, randomLenAfter, "yyyyMMddHHmmssSSS");
	}
	
	public static String generateUuidStr() {
	    return UUID.randomUUID().toString().replace("-", "");
    }
	
    public static String genDataStreamNum() {
        return genDataStreamNum(null);
    }
    
    public final static String _DATA_STREAM_NUM_PREFIX = "S";
    public static String genDataStreamNum(String dynTblSuffix) {
        StringBuffer buffer = new StringBuffer(_DATA_STREAM_NUM_PREFIX);
        buffer.append(DateUtils.getCurrentTimeString());
        buffer.append("U");
        buffer.append(UUID.randomUUID().toString().replace("-", ""));
        //
        if(StringUtils.hasLength(dynTblSuffix)) {
            buffer.append("@").append(dynTblSuffix);
        }
        //
        return buffer.toString();
    }
    
	public static void main(String[] args) {
        System.out.println("S123456789012345678".substring(1, 14));
	}
}
