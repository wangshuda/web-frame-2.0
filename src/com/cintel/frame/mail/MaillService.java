package com.cintel.frame.mail;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Title: MaillRecvService
 * 
 * @version 1.0
 * @author  wangshuda
 * @date   2006-11-18
 */
public class MaillService {
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(MaillService.class);
	
	@SuppressWarnings("unchecked")
	public static List getNewMsgList(MailAccountInfo mailAccount, int earlyMinutes) {
		List newMsgList = new ArrayList();	
		try {
			// Get the store
			Store pop3Store = getStoreByAccount(mailAccount);
			if(pop3Store != null) {
				// Get folder
				Folder inboxfolder = pop3Store.getFolder("INBOX");
				inboxfolder.open(Folder.READ_ONLY);
				// Get directory
				Message message[] = inboxfolder.getMessages();
			
				int mailCount = message.length;
	
				String loopMsgSentTime = null;
				SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
	
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MINUTE, 0 - earlyMinutes);
				
				String minuteEarly = sd.format(c.getTime());

				for (int i = 0; i < mailCount; i++) {
					loopMsgSentTime = sd.format(message[i].getSentDate());
					
					if(loopMsgSentTime.compareTo(minuteEarly) >= 0) {
						
                        outPutMsg(message[i]);
						newMsgList.add(message[i]);
					}
				}
				
				//Close connection
				inboxfolder.close(false);
				pop3Store.close();
			}

		}
		catch(Exception ex) {
            log.error("", ex);
		}
		return newMsgList;
	}
	
	public static void sendMail(MailAccountInfo fromAccount, String mailTo,  String subject, String text, List<IAttachmentInfo> attachmentList) {
		Properties props = System.getProperties();
		
		String host = fromAccount.getSendServer().getAddress();
		String accountName = fromAccount.getAccountName();
		String accountPwd = fromAccount.getAccountPwd();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		
		Authenticator auth = new MyAuthenticator(accountName, accountPwd);
		Session session = Session.getDefaultInstance(props, auth);
		
		session.setDebug(false);
		
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(accountName));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
			message.setSubject(subject);
			//
			Multipart multipart = new MimeMultipart();
			//Set mail content text
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText(text);
			multipart.addBodyPart(contentPart);
			
	    	if(attachmentList != null) {
	    		IAttachmentInfo attachmentInfo = null;
	    		BodyPart attachmentPart= new MimeBodyPart();
	    		for(int i = 0; i < attachmentList.size(); i ++) {
	    			attachmentInfo = attachmentList.get(i);
	    			
	    			attachmentPart.setDataHandler(attachmentInfo.getDataHandler());
	    			attachmentPart.setFileName(String.valueOf(i) + "." + attachmentInfo.getFileType());
	    			multipart.addBodyPart(attachmentPart);
	    		}
	    	}
	    	message.setContent(multipart);
	    	message.saveChanges();

            Transport transport = session.getTransport("smtp");
            transport.connect(host, accountName, accountPwd);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
		}
		catch(Exception ex) {
            log.error("", ex);
		}
	}
	
	public static boolean checkMailAccount(MailAccountInfo mailAccount) {
		boolean rtnVal = true;
		try {
			Store pop3Store = getStoreByAccount(mailAccount);
			if(pop3Store == null) {
				rtnVal = false;
			}
		}
		catch(Exception ex) {
			rtnVal = false;
			
		}
		return rtnVal;
	}
	
	public static String decodeText(String text) throws UnsupportedEncodingException {
		  if(text == null) {
			  return null;
		  }
		  if(text.startsWith("=?GB") || text.startsWith("=?gb")) {
			  text = MimeUtility.decodeText(text);
		  }
		  else {
			  text = new String(text.getBytes("ISO8859_1"));
		  }
		  return text;
	}

	private static Store getStoreByAccount(MailAccountInfo mailAccount) {
		Store pop3Store = null;
		try{
			String recvMailServerAddr = mailAccount.getRecvServer().getAddress();
			String accountName = mailAccount.getAccountName();
			String accountPwd = mailAccount.getAccountPwd();
			int port = mailAccount.getRecvServer().getPort();

			//Create empty properties
			Properties props = new Properties();
			props.put("mail.pop.host", recvMailServerAddr);

			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(false);
			// Get the store
			pop3Store = session.getStore("pop3");

			pop3Store.connect(recvMailServerAddr, port, accountName, accountPwd);
		}
		catch(Exception ex) {
			pop3Store = null;
		}
		return pop3Store;
	}
	
	private static void outPutMsg(Message msg) throws Exception {
		try {
			log.info("From = " + decodeText((msg.getFrom()[0]).toString()));
            log.info("Subject = " + msg.getSubject());
            log.info("SentDate = " + msg.getSentDate().toString());
		}
		catch(Exception ex) {
			throw(ex);
		}
	}
    
    public static void main(String args[]) {
        MailAccountInfo mailAccount = new MailAccountInfo();
        mailAccount.setAccountName("wangshuda@cincc.cn");
        mailAccount.setAccountPwd("123456");
        //
        MailServerInfo sendServerCtx = new MailServerInfo();
        sendServerCtx.setAddress("smtp.ym.163.com");
        sendServerCtx.setPort(25);
        ///
        mailAccount.setSendServer(sendServerCtx );
        //
        MaillService.sendMail(mailAccount, "wangshuda@cintel.net.cn", "TestSubject", "TestText", null);
    }
}


