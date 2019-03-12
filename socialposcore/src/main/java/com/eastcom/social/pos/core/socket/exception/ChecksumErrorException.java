package com.eastcom.social.pos.core.socket.exception;

/**
 * 校验码错误异常
 * @author XZK
 *
 */
public class ChecksumErrorException extends Exception {
	private static final long serialVersionUID = 1L;

	public ChecksumErrorException(String msg){
		super(msg);
	}
}
