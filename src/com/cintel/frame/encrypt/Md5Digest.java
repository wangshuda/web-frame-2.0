package com.cintel.frame.encrypt;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.FormatUtils;

public class Md5Digest {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(Md5Digest.class);
	
	private MessageDigest messageDigest = null;//

	public Md5Digest() {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException ex) {
            log.error("Md5Digest instance error!", ex);
        }
	}
    
    private static Md5Digest instance = new Md5Digest();
    
    public static Md5Digest getInstance() {
        return instance;
    }
	//
	public Md5Digest(String args) {
		this();
		this.update(args);
	}
	
	public Md5Digest(String args[]) {
		this();
		this.update(args);
	}
	//
	private void update(String updateStr) {
		messageDigest.update(updateStr.getBytes());
	}

	private void update(String[] updateStrArr) {
		if(updateStrArr != null) {
			for (int i = 0; i < updateStrArr.length; i++) {
				update(updateStrArr[i]);
			}
		}
	}
	
	private String getHexStr() {
		byte[] digestByteArr = messageDigest.digest();
		return FormatUtils.byteArr2HexStr(digestByteArr);
	}

	
	public String getEncryptedPwd(String arg) {
		return getEncryptedPwd(new String[]{arg});
	}
	
	public String getEncryptedPwd(String args[]) {
		update(args);
		return getHexStr();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Md5Digest md5Digest;
        md5Digest = new Md5Digest();
        //
        //md5Digest.update("1234567a");
        //
        //String encryptedPwd = md5Digest.getHexStr();
        //encryptedPwd = fe008700f25cb28940ca8ed91b23b354
        //System.out.println("encryptedPwd = " + encryptedPwd);
        //
        //Md5Digest md5Digest2 = new Md5Digest();
        
        String encryptedPwd = md5Digest.getEncryptedPwd("SDK-BBX-010-18671d3abb3-6");
        System.out.println("encryptedPwd = " + encryptedPwd);

	}
}
