package com.cintel.frame.encrypt;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;

import com.cintel.frame.util.FormatUtils;

import java.security.Key;

/**
 * Title: DesDigest
 * 
 * @version 1.0
 * @author  wangshuda
 * @date   2006-11-18
 */
public class DesDigest {
	
	private Key getKey(byte[] arrBTmp) throws Exception {   
		byte[] arrB = new byte[8];
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++){
			arrB[i] = arrBTmp[i];   
		}
		//the length of arrB must be 8
		//so , if the length of arrBTmp is less than 8, 
		//the default value will be 0 which location is large than the length of arrBTmp
		Key key = new SecretKeySpec(arrB, "DES");
		return key;
	}
	
	private byte[] doEncrypt(Key key, byte[] data) throws Exception {
        //Get a cipher object
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		//Encrypt
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] raw = cipher.doFinal(data);

		return raw;
	}

	private byte[] doDecrypt(Key key, byte[] raw) throws Exception {
	    //Get a cipher object
	    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	    //Decrypt
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    byte[] data = cipher.doFinal(raw);
	    
	    return data;
	}
	
	//
	private String keyStr = null;
	
	private String keyEncodeType = null;
	
	private String pwdEncodeType = null;
	

	//Construct function
	public DesDigest(String keyStr) throws Exception {
		if(keyStr != null && keyStr.length() != 0) {
			this.keyStr = keyStr;
		}
		else {
			throw new Exception("Key can not be null !");
		}
		//
		setKeyEncodeType("utf-8");
		setPwdEncodeType("utf-8");
	}
	
	public String getKeyEncodeType() {
		return (keyEncodeType == null ? keyEncodeType : keyEncodeType.trim());
	}

	public void setKeyEncodeType(String keyEncodeType) {
		this.keyEncodeType = keyEncodeType;
	}

	public String getPwdEncodeType() {
		return (pwdEncodeType == null ? pwdEncodeType : pwdEncodeType.trim());
	}

	public void setPwdEncodeType(String pwdEncodeType) {
		this.pwdEncodeType = pwdEncodeType;
	}
	
	public String getSavedPwd(String rawPwd) {
    	String savedPwd = null;
		try{
			if(rawPwd == null || "".equals(rawPwd)) {
				savedPwd = rawPwd;
			}
			else {
				Key key = getKey(keyStr.getBytes(keyEncodeType));
				byte[] encryptByteArr = doEncrypt(key, rawPwd.getBytes(pwdEncodeType));
				savedPwd = FormatUtils.byteArr2HexStr(encryptByteArr);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return savedPwd;
    }
    
	public String getRawPwd(String savedPwd) {
    	String rawPwd = null;
		try{
			if(savedPwd == null || "".equals(savedPwd)) {
				rawPwd = savedPwd;
			}
			else {
				Key key = getKey(keyStr.getBytes(keyEncodeType));
				byte[] decryptByte = doDecrypt(key, FormatUtils.hexStr2ByteArr(savedPwd));
				rawPwd = new String(decryptByte, pwdEncodeType);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return rawPwd;
    }
}
