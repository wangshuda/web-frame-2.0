package com.cintel.frame.ws.axis;

import org.apache.axis.AxisFault;
import org.apache.axis.Handler;
import org.apache.axis.MessageContext;

public interface RequestHeaderBuilder extends Handler {
	public void invoke(MessageContext msgCtx) throws AxisFault;
}
