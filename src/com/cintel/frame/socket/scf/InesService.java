package com.cintel.frame.socket.scf;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.cintel.frame.socket.SocketMsgSender;
import com.cintel.frame.util.RandomUtils;

/**
 * 
 * @file    : InesService.java
 * @author  : WangShuDa
 * @date    : 2008-12-26
 * @corp    : CINtel
 * @version : 1.0
 */
public class InesService implements InesServiceInterface {
	private Log log = LogFactory.getLog(this.getClass());
	//
	public static String SEQUENCE_ID_ERROR = "-1";
	
	// properties define.
	/**
	 * the value likes 'SCF01 * * register '
	 */
	private String registerCommand = null;
	
	/**
	 * the value likes "SCF01 * {0} invokeservice 21 sm_wap_push sender "
	 * {0} is rondom string to identify the request.
	 */
	private String requestCommandPattern = null;

	private SocketMsgSender sender;
	
	private boolean hasRegistered = false;
	
	private boolean checkSequenceId = true;
	
	/**
	 * the method will be called using spring with init-method.
	 * 
	 */
	@SuppressWarnings("unused")
	private void init() {
		if(!StringUtils.hasText(registerCommand)) {
			log.error("registerCommand can not be empty!");
		}
		//
		if(!StringUtils.hasText(requestCommandPattern)) {
			log.error("requestCommandPattern can not be empty!");
		}
		//
		if(sender == null) {
			log.error("SocketMsgSender can not be null!");
		}
		//
		this.register();
	}
	
    //command methods.
	private void register() {
		String registerRequestStr = getRegisterCommand();
		try {
			log.debug(registerRequestStr);
			sender.post(registerRequestStr, false);
			//
			hasRegistered = true;
		}
		catch (Exception ex) {
			log.warn("Post the msg for register failed:" + registerRequestStr, ex);
			hasRegistered = false;
		}
		//
		if(log.isDebugEnabled()) {
			log.debug("Succeed in sending register message!");
		}
	}

	/**
	 * 
	 * @method: send
	 * @return: String
	 * @author: WangShuDa
	 * @param args : keep the args sequence with the message defined with server.
	 * @return : parse the response text like "SCS01 002 0000000002 serviceevent * sm_wap_rsp 'resultCode' *" and get the 'resultCode'
	 * @throws Exception 
	 * @throws Exception
	 */
	public String send(String ... args) throws Exception  {
		if(!hasRegistered) {
			this.register();
		}
		//orgnize the request str.
		String reqSequenceId = RandomUtils.generateTimeStampRandomStr(6, 6, "yyyyMMddHHmmssSSS");
		String requestCommand = MessageFormat.format(requestCommandPattern, reqSequenceId);
		//
		StringBuilder builder = new StringBuilder();
		builder.append(requestCommand);
		for(String parameter:args) {
			builder.append(" "); //space
			builder.append(parameter);
		}
		String requestStr = builder.toString();
		
		// log
		if(log.isDebugEnabled()) {
			log.debug(requestStr);
		}
		//
		String responseText = "";
		responseText = sender.post(requestStr);
		//
		if(log.isDebugEnabled()) {
			log.debug(responseText);
		}
		//using space to token the responseText
		String[] responseStrArr = StringUtils.tokenizeToStringArray(responseText, " ");
		
		if(this.checkSequenceId) {
			String resSequenceId = responseStrArr[2];
			if(!reqSequenceId.equals(resSequenceId)) {
				log.warn("The sequenceId in the response is not equal with the one in the request!");
				return SEQUENCE_ID_ERROR;
			}
		}
		//
		return responseStrArr[6];
	}

	public static void main(String args[]) {
		String reqeustCommand = "invokeservice 618 sm_wap_push sender";
		StringBuilder builder = new StringBuilder();
		builder.append(reqeustCommand);
		for(String parameter:args) {
			builder.append(" "); //space
			builder.append(parameter);
		}
		System.out.println(builder.toString());
		//
		String responseText = "SCS01 002 0000000002 serviceevent * sm_wap_rsp 'resultCode' *";
		//using space to token the responseText
		String[] responseStrArr = StringUtils.tokenizeToStringArray(responseText, " ");
		//		
		System.out.println(responseStrArr[6]);
		//
		String requestCommandPattern = "SCF01 * {0} invokeservice 21 sm_wap_push sender ";
		String result = MessageFormat.format(requestCommandPattern, RandomUtils.generateTimeStampRandomStr(6, 6, "yyyyMMddHHmmssSSS"));
		System.out.println(result);
	}

	// get and set methods.
	public SocketMsgSender getSender() {
		return sender;
	}

	public void setSender(SocketMsgSender sender) {
		this.sender = sender;
	}
	
	public String getRegisterCommand() {
		return registerCommand;
	}

	public void setRegisterCommand(String registerCommand) {
		this.registerCommand = registerCommand;
	}

	public boolean isHasRegistered() {
		return hasRegistered;
	}

	public void setHasRegistered(boolean hasRegistered) {
		this.hasRegistered = hasRegistered;
	}

	public String getRequestCommandPattern() {
		return requestCommandPattern;
	}

	public void setRequestCommandPattern(String requestCommandPattern) {
		this.requestCommandPattern = requestCommandPattern;
	}

	public boolean isCheckSequenceId() {
		return checkSequenceId;
	}

	public void setCheckSequenceId(boolean checkSequenceId) {
		this.checkSequenceId = checkSequenceId;
	}

}
