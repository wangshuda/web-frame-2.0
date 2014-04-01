package com.cintel.frame.net.ip;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 
 * @version
 * @history 1.0.0 2011-3-11 wangshuda created
 */
public class IpAddrV4Builder {
	private final static int _FOUR = 4;

	private final static String _IP_SEPERATE_STR = "\\.";

	private String ipAddrStr;

	private int[] ipIntValueArr = new int[4];

	public IpAddrV4Builder(String ipAddrStr) {
		this.ipAddrStr = ipAddrStr;
		//
		String[] strArr = ipAddrStr.split(_IP_SEPERATE_STR);
		if (strArr.length == _FOUR) {
			for (int i = 0; i < _FOUR; i++) {
				ipIntValueArr[i] = Integer.parseInt(strArr[i]);
			}
		}
	}

	private int doAndCal4Int(int firstInt, int secondInt) {
		// 1) int value to Binary.
		String binaryStr1 = Integer.toBinaryString(firstInt);
		String binaryStr2 = Integer.toBinaryString(secondInt);
		// 2) Conf the zero before the short Binary str. be sure the length is same with the two Binary string. 
		int len1 = binaryStr1.length();
		int len2 = binaryStr2.length();
		//
		int binaryCnt = (len1 > len2 ? len1 : len2);
		//
		char[] zeroCharArr = new char[binaryCnt];
		Arrays.fill(zeroCharArr, '0');

		String zeroPattern = new String(zeroCharArr);
		DecimalFormat numberFmt = new DecimalFormat(zeroPattern);
		//
		binaryStr1 = numberFmt.format(Long.valueOf(binaryStr1));
		binaryStr2 = numberFmt.format(Long.valueOf(binaryStr2));

		// 3) do calculate
		char[] resultCharArr = new char[binaryCnt];
		char[] str1CharArr = binaryStr1.toCharArray();
		char[] str2CharArr = binaryStr2.toCharArray();
		//
		int andRtn;
		for (int i = 0; i < binaryCnt; i++) {
			andRtn = Integer.valueOf(str1CharArr[i]) & Integer.valueOf(str2CharArr[i]);
			resultCharArr[i] = (char)andRtn;
		}
		return Integer.valueOf(new String(resultCharArr), 2);
	}

	public String maskIp2NetAddr(String maskIpAddStr) {
		return this.maskIp2NetAddr(new IpAddrV4Builder(maskIpAddStr));
	}

	public String maskIp2NetAddr(IpAddrV4Builder maskIpAddrV4Ctx) {
		int maskedArr[] = new int[4];
		int ipIntValueArr[] = maskIpAddrV4Ctx.getIpIntValueArr();
		for (int i = 0; i < _FOUR; i++) {
			maskedArr[i] = this.doAndCal4Int(ipIntValueArr[i],
					this.ipIntValueArr[i]);
		}
		//
		return MessageFormat.format("{0}.{1}.{2}.{3}", maskedArr[0], maskedArr[1], maskedArr[2], maskedArr[3]);

	}
	
	// ------------------------------------- get/set methods ------------------------------------- 
	public String getIpAddrStr() {
		return ipAddrStr;
	}

	public void setIpAddrStr(String ipAddrStr) {
		this.ipAddrStr = ipAddrStr;
	}
	
	public int[] getIpIntValueArr() {
		return this.ipIntValueArr;
	}
	
	public void setIpIntValueArr(int[] ipIntValueArr) {
		this.ipIntValueArr = ipIntValueArr;
	}

    // ------------------------------------- static main method -------------------------------------
	public static void main(String args[]) {
		String ipAddr = "192.168.2.89";
		String maskAddr = "255.240.230.0";
		IpAddrV4Builder ipAddrBuilder = new IpAddrV4Builder(ipAddr);

		System.out.println(ipAddrBuilder.maskIp2NetAddr(maskAddr));
	}
}