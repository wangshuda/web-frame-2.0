package com.cintel.frame.ftp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

import com.cintel.frame.util.FileUtils;
import com.cintel.frame.util.StringUtils;


/**
 * 
 * @File    : EasyFtpClient.java
 * @Author  : WangShuDa
 * @Date    : 2008-9-3
 * @Corp    : CINtel
 * @Version : 1.0
 */
public class EasyFtpClient extends FtpClient{
	private Log log = LogFactory.getLog(this.getClass());
	
	private ReplyInfo lastReply = null;
	
	private String[] splitPathStr2Arr(String path) throws Exception {
		if(!StringUtils.hasText(path)) {
			throw new Exception("Ftp Parameter Error! Path can not be empty and must be seperated with single '/'! ");
		}
		String [] pathArr = path.split("/");
		return pathArr;
	}
	//
	public EasyFtpClient() {
	}
	//
	public EasyFtpClient(String hostAddr, int hostPort) throws Exception {
		this.openServer(hostAddr, hostPort);
	}

	public EasyFtpClient(String hostAddr, int hostPort, String userName, String passWord) throws Exception {
		this.openServer(hostAddr, hostPort);
		login(userName, passWord);
	}
	
    public ReplyInfo executeCmd(String cmdStr) {
        String responseStr = null;
        try {
            this.issueCommandCheck(cmdStr);
            responseStr = this.getResponseString();
        }
        catch (Exception ex) {
            log.warn(ex.getMessage(), ex);
            //
            String exMsgStr = ex.getMessage();
            if(cmdStr != null && exMsgStr != null); {
                responseStr = exMsgStr.substring(cmdStr.length() + 2);
            }
        }
        
        return new ReplyInfo(responseStr);
    }
    
	//~ private methods.
	private void sendCommand(String cmdStr) {		
		lastReply = this.executeCmd(cmdStr);
	}
	
	private void checkConnection (boolean shouldBeConnected) throws Exception{
		if(shouldBeConnected && !this.serverIsOpen()) {
			throw new Exception("The FTP client has not yet connected to the server.  "
    				+ "The requested action cannot be performed until after a connection has been established.");
		}
	}
	
	// ~ public methods.
	public boolean exist(String path) throws Exception {
		checkConnection(true);
		
		boolean rtnVal = false;
		String cmdStr = "MDTM " + path;
		sendCommand(cmdStr);
		char ch = lastReply.getReplyCode().charAt(0);
		if(ch == '2') {
			return true;
		}
		return rtnVal;
	}
	
	public boolean isExist(String path) throws Exception {
		checkConnection(true);
		boolean rtnVal = false;
		String[] fileListArr = listFileNames("");
		for(String file : fileListArr){
			if(path.equals(file)){
				rtnVal = true;
				break;
			}
		}
		return rtnVal;
		
	}
	public void mkdir(String newPath) throws Exception {
		checkConnection(true);
		//
		String cmdStr = "MKD " + newPath;
		this.issueCommandCheck(cmdStr);
	}
	
	public void mkdirs(String newPath) throws Exception {
		checkConnection(true);
		//
		String cmdStr = null;
		String [] pathArr = splitPathStr2Arr(newPath);
		for(int i = 0; i < pathArr.length; i ++) {
			//
			if(!isExist(pathArr[i])) {
				cmdStr = "MKD " + pathArr[i];
				sendCommand(cmdStr);
			}
			cd(pathArr[i]);
		}
	}

	public void delete(String filePath, String fileName) throws Exception {
		//
		checkConnection(true);
		//
		String cmdStr = null;
		String [] pathArr = splitPathStr2Arr(filePath);
		for(int i = 0; i < pathArr.length; i ++) {
			cd(pathArr[i]);
		}
		//
		cmdStr = "DELE " + fileName;
		sendCommand(cmdStr);
	}
	
	public void upload(File file, String fileName) throws Exception {
		FileInputStream inStream = new FileInputStream(file);
		upload(inStream, fileName);
	}

	public void upload(byte[] fileData, String fileName) throws Exception {
		ByteArrayInputStream inStream = new ByteArrayInputStream(fileData);
		upload(inStream, fileName);
	}

	public void upload(InputStream inStream, String fileName) throws Exception{
	    if(!this.serverIsOpen()){
	    	throw new Exception("Error: Server is not open !");
	    }
	    
	    this.binary();
	    TelnetOutputStream telnetOutStream = this.put(fileName);
		FileUtils.copy(inStream, telnetOutStream);
		
	    this.ascii();
	}
	
	public InputStream downLoad(String filePath) throws Exception {
		this.binary();
		return this.get(filePath);
	}

	public void cdUp(String currentDirPath) throws Exception {
		int currentDirLevel = splitPathStr2Arr(currentDirPath).length;
		cdUp(currentDirLevel);
	}
	
	public void cdUp(int upCount) throws Exception {
		for(int i = 0; i < upCount; i++) {
			this.cdUp();
		}
	}
	
	public String[] listFileNames(String fileNameReg) throws IOException {
		List<String> list = new ArrayList<String>();
		//
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.nameList(fileNameReg)));

		String filename = null;
		while ((filename = reader.readLine()) != null) {
			list.add(filename);
		}
		return (String[])list.toArray(new String[list.size()]);

	}
}
