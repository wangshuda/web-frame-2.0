package com.cintel.frame.socket;

/**
 * 
 * @file    : RuntimeSocketException.java
 * @author  : WangShuDa
 * @date    : 2009-6-14
 * @corp    : CINtel
 * @version : 1.0
 */
public class RuntimeSocketException extends RuntimeException {
	private static final long serialVersionUID = 841389389653381468L;

	public RuntimeSocketException(Exception ex) {
		super(ex);
	}
}
