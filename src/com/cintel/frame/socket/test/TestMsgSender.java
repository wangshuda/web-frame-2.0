package com.cintel.frame.socket.test;

import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import com.cintel.frame.socket.ClientFactoryImpl;
import com.cintel.frame.socket.SocketClient;
import com.cintel.frame.socket.SocketConfig;
import com.cintel.frame.socket.SocketMsgSenderImpl;

public class TestMsgSender {
	private Log log = LogFactory.getLog(TestMsgSender.class);

	private SocketMsgSenderImpl socketMsgSender = null;

	private ClientFactoryImpl clientFactory = null;

	@Before
	public void setUp() throws Exception {
		//String serverIpAddr = InetAddress.getLocalHost().getHostAddress();
		String serverIpAddr = "192.168.2.222";
		int port = 8888;
		SocketConfig socketConfig = new SocketConfig();
		socketConfig.setServerIpAddr(serverIpAddr);
		socketConfig.setServerPort(port);
		
		//
		clientFactory = new ClientFactoryImpl();
		clientFactory.setSocketConfig(socketConfig);

		//
		socketMsgSender = new SocketMsgSenderImpl();
		socketMsgSender.setSocketClient((SocketClient)(clientFactory.getObject()));
	}

	@After
	public void tearDown() throws Exception {

	}

	//@Test
	public void testPostStr() throws Exception {
		String response = socketMsgSender.post("Test");
		log.debug("The response of testPostStr is:" + response);
		assertTrue(response != null && response.length() > 0);
	}
	
	//@Test
	public void testPostStr2() throws Exception {
		String response = socketMsgSender.post("Test2");
		log.debug("The response of testPostStr is:" + response);
		assertTrue(response != null && response.length() > 0);
	}
	
	//@Test
	public void testLoop() throws Exception {
		int loop= 0;
		while(true) {
			log.debug("loop:" + loop);
			try {
				String response = socketMsgSender.post("Test" + (loop++));
				log.debug("The response of testPostStr is:" + response);
			}
			catch(Exception ex) {
				ex.printStackTrace();
				
			}
			
			
			//Thread.sleep(5*1000);
		}
	}
}
