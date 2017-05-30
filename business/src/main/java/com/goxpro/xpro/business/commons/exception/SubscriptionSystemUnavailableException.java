package com.goxpro.xpro.business.commons.exception;

import javax.ejb.ApplicationException;

import com.goxpro.xpro.business.MessageUtil;

@ApplicationException(rollback = true)
public class SubscriptionSystemUnavailableException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public SubscriptionSystemUnavailableException() {
		super();
	}

	@Override
	public String getMessage() {
		String msg = MessageUtil.toText("SubscriptionSystemUnavailableException");
		return msg;
	}
}
