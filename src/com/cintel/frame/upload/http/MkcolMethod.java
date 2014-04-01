package com.cintel.frame.upload.http;

import org.apache.commons.httpclient.methods.EntityEnclosingMethod;

public class MkcolMethod extends EntityEnclosingMethod {
	
	public MkcolMethod() {
	}

	public MkcolMethod(String uri) {
		super(uri);
	}

	@Override
	public String getName() {
		return "MKCOL";
	}

}
