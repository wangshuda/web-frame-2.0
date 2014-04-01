package com.cintel.frame.ws.axis;

public class TestAxisClientStubFactory {

	public static void main(String []args) throws Exception {
		AxisClientStubFactory factory = new AxisClientStubFactory();
		//
		factory.setLocatorClassName("com.cintel.pn.ws.system.v1_0.vip.service.VipServiceLocator");
		factory.setStubClassName("com.cintel.pn.ws.system.v1_0.vip.service.VipBindingStub");
		factory.setServiceName("Vip");
		factory.setServiceUrlLocation("http://192.168.2.223:16856/Pnc/vip");
		//
		factory.getObject();
	}
}
