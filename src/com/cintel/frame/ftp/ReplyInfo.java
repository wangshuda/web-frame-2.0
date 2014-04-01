package com.cintel.frame.ftp;

/**
 * Encapsulates the FTP server reply
 * 
 * @author  WangShuDa
 * @version 1.0
 * 
 * See com.enterprisedt.net.ftp.FTPReply
 */
public class ReplyInfo {
	
	private String replyCode;
	private String replyText;
	private String rawReply;
	private String[] data;

    ReplyInfo(String replyCode, String replyText) {
        this.replyCode = replyCode;
        this.replyText = replyText;
        rawReply = replyCode + " " + replyText;
    }

    ReplyInfo(String replyCode, String replyText, String[] data) {
        this.replyCode = replyCode;
        this.replyText = replyText;
        this.data = data;
    }
    
    ReplyInfo(String reply) {        
        // all reply codes are 3 chars long
        this.rawReply = reply.trim();
        replyCode = rawReply.substring(0, 3);
        if (rawReply.length() > 3) {
            replyText = rawReply.substring(4);
        }
        else {
            replyText = "";
        }
    }
    
	public String getRawReply() {
		return rawReply;
	}

	public String getReplyCode() {
		return replyCode;
	}
	
	public String getReplyText() {
		return replyText;
	}
	
	public String[] getData() {
		return data;
	}
}
