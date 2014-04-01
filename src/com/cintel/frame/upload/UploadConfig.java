package com.cintel.frame.upload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.util.StringUtils;

public class UploadConfig {
	private Log log = LogFactory.getLog(this.getClass());
	
	private String fileSuffix = null;
	//
	private boolean saveLocalFlag = false;

	private boolean backupFlag = false;

	private String fileHome;

	private String backupFileHome;

	// ~ Ftp config info
	private boolean useFtpFlag = false;

	private String ftpServerIp;

	private int ftpServerPort;

	private String ftpUserName;

	private String ftpUserPassword;

	private String ftpFileHome;

	private boolean ftpBackupFlag = false;

	private String ftpBackupFileHome;
	
	private int maxFileSize;
	
	private String contentTypeRegex;

	// ~ Property fields set methods.

	public String getContentTypeRegex() {
		return contentTypeRegex;
	}

	public void setContentTypeRegex(String contentTypeRegex) {
		this.contentTypeRegex = contentTypeRegex;
	}

	public int getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		if(StringUtils.hasText(fileSuffix) && '.' != fileSuffix.charAt(0)) {
			log.warn("fileSuffix should be started with '.', for example, '.gif' or '.wav'");
		}
		this.fileSuffix = fileSuffix;
	}
	
	//
	public void setSaveLocalFlag(boolean saveLocalFlag) {
		this.saveLocalFlag = saveLocalFlag;
	}

	public void setBackupFileHome(String backupFileHome) {
		this.backupFileHome = backupFileHome;
	}

	public void setBackupFlag(boolean backupFlag) {
		this.backupFlag = backupFlag;
	}

	public void setFileHome(String fileHome) {
		this.fileHome = fileHome;
	}

	public void setFtpFileHome(String ftpFileHome) {
		this.ftpFileHome = ftpFileHome;
	}

	public void setFtpServerIp(String ftpServerIp) {
		this.ftpServerIp = ftpServerIp;
	}

	public void setFtpServerPort(int ftpServerPort) {
		this.ftpServerPort = ftpServerPort;
	}

	public void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}

	public void setFtpUserPassword(String ftpUserPassword) {
		this.ftpUserPassword = ftpUserPassword;
	}

	public void setUseFtpFlag(boolean useFtpFlag) {
		this.useFtpFlag = useFtpFlag;
	}

	public void setFtpBackupFileHome(String ftpBackupFileHome) {
		this.ftpBackupFileHome = ftpBackupFileHome;
	}

	public void setFtpBackupFlag(boolean ftpBackupFlag) {
		this.ftpBackupFlag = ftpBackupFlag;
	}

	public String getBackupFileHome() {
		return backupFileHome;
	}

	public boolean isBackupFlag() {
		return backupFlag;
	}

	public String getFileHome() {
		return fileHome;
	}

	public String getFtpBackupFileHome() {
		return ftpBackupFileHome;
	}

	public boolean isFtpBackupFlag() {
		return ftpBackupFlag;
	}

	public String getFtpFileHome() {
		return ftpFileHome;
	}

	public String getFtpServerIp() {
		return ftpServerIp;
	}

	public int getFtpServerPort() {
		return ftpServerPort;
	}

	public String getFtpUserName() {
		return ftpUserName;
	}

	public String getFtpUserPassword() {
		return ftpUserPassword;
	}

	public boolean isSaveLocalFlag() {
		return saveLocalFlag;
	}

	public boolean isUseFtpFlag() {
		return useFtpFlag;
	}
}
