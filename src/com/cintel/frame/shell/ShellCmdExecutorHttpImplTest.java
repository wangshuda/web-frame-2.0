package com.cintel.frame.shell;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.cintel.frame.net.http.HttpMethodExecutor;

/**
 * 
 * @version $Id: ShellCmdExecutorHttpImplTest.java 13999 2009-12-23 05:02:22Z wangshuda $
 * @history 
 *          1.0.0 2009-12-17 wangshuda created
 */
public class ShellCmdExecutorHttpImplTest {
	private Log log = LogFactory.getLog(this.getClass());
	
	ShellCmdExecutorHttpImpl cmdExecutor = new ShellCmdExecutorHttpImpl();
	
	private Command unzipCommand = new Command();
	
	private Command forceUnzipCommand = new Command();
	
	private Command phoneNumberLineGrepCommand = new Command();
	@Before
	public void initial() {
		HttpMethodExecutor httpMethodExecutor = new HttpMethodExecutor();
		
		httpMethodExecutor.setTargetURL("http://192.168.2.154:9999/media/cmd");
		//
		cmdExecutor.setHttpMethodExecutor(httpMethodExecutor);
		//
		unzipCommand.setCmdTextPattern("unzip -n {0} && echo Ok");
		unzipCommand.setSuccessResponsePattern("Ok");
		//
		forceUnzipCommand.setCmdTextPattern("unzip -o {0} && echo Ok");
		forceUnzipCommand.setSuccessResponsePattern("Ok");
		//
		// 0: phonenumber, 1: task outCallFile
		phoneNumberLineGrepCommand.setCmdTextPattern("grep -n {0} {1}.ix");
		
	}
	
	//@Test
	public void testLs() {
		Command lsCommand = new Command();
		String cmdTextPattern = "ls {0}";
		lsCommand.setCmdTextPattern(cmdTextPattern);
		//
		lsCommand.setCmdArgs(new String[]{""});
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		cmdExecutor.execute(lsCommand, out);
		
		log.debug(new String(out.toByteArray()));
	}
	
	//@Test
	public void testUnzip() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] args = new String[]{"/home/huangyu2/media/100999/outAddress/error2.zip"};
		unzipCommand.setCmdArgs(args);
		cmdExecutor.execute(unzipCommand, out);
		
		String resultStr = new String(out.toByteArray());
		
		unzipCommand.assertIsOk(resultStr);
	}
	
	//@Test
	public void testForceUnzip() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] args = new String[]{"/home/huangyu2/media/100999/outAddress/error2.zip33"};
		forceUnzipCommand.setCmdArgs(args);
		cmdExecutor.execute(forceUnzipCommand, out);
		
		String resultStr = new String(out.toByteArray());
		forceUnzipCommand.assertIsOk(resultStr);
	}
	
	@Test
	public void testPhoneNumberLineGrepCommand() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		String[] args = new String[]{"1381026489", "200208/outAddress/number.txt"};
		
		phoneNumberLineGrepCommand.setCmdArgs(args);
		cmdExecutor.execute(phoneNumberLineGrepCommand, out);
		
		String resultStr = new String(out.toByteArray());
		log.debug("resultStr = " + resultStr);
	}
}
