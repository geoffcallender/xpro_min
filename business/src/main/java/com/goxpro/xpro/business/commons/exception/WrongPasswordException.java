package com.goxpro.xpro.business.commons.exception;

import javax.ejb.ApplicationException;

import com.goxpro.xpro.business.MessageUtil;

/**
 * Thrown only by LocalIdentity's changePassword(String currentPlainPassword, String newPlainPassword).
 */
@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class WrongPasswordException extends BusinessException {

	public WrongPasswordException() {
		super();
	}

	@Override
	public String getMessage() {

		// We deferred converting the message ids to messages until now, when we are more likely to be in the user's
		// locale.

		String msg = MessageUtil.toText("WrongPasswordException");
		return msg;
	}

}
