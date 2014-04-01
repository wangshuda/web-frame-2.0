package com.cintel.frame.socket.test;

import java.io.ByteArrayOutputStream;

import com.cintel.frame.socket.SocketClient;
import com.cintel.frame.socket.SocketClientImpl;
import com.cintel.frame.socket.SocketConfig;

public class TestSocketClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("1234".substring(0, 3));
		
		// TODO Auto-generated method stub
		int loop = 0;
		String testStr = "---------request";
		String requestStr = null;
		String responseStr = null;
		SocketConfig socketConfig = new SocketConfig("169.254.36.52", 4005);
		
		SocketClient socketClient = new SocketClientImpl(socketConfig);
		//SocketClient socketClient = new SocketClient("192.168.2.222", 6666);
		try {
			socketClient.connect();
			//while(true) {
				requestStr = (loop++) + testStr;
				try {
					socketClient.write(requestStr, true);
					System.out.println(requestStr);
					//
					ByteArrayOutputStream out = new ByteArrayOutputStream(SocketClient.BUFFER_SIZE);
					socketClient.read(out);
					responseStr = new String(out.toByteArray());
					
					System.out.println("responseStr=" + responseStr);
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
				Thread.sleep(3*3000);
			//}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally{
			socketClient.close();
			System.out.println("============");
		}
	}

}
