package com.goxpro.xpro.business.commons.exception;

import javax.ejb.ApplicationException;

import com.goxpro.xpro.business.MessageUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class WrongStatusException extends BusinessException {
	private Object _expectedStatus;
	private Object _foundStatus;

	// TODO - give this the usual contents like the other subclasses of BusinessException

	/**
	 * Throw this exception from an entity that has a required property that has not been given a value (eg. null, empty
	 * or 0).
	 * 
	 * @param entity the entity being set.
	 * @param fieldLabelMessageId the key of a message that represents the field that has not been given a value.
	 */
	public WrongStatusException(Object expectedStatus, Object foundStatus) {

		// Don't convert the message ids to messages yet because we're in the server's locale, not the user's.

		super();
		_expectedStatus = expectedStatus;
		_foundStatus = foundStatus;
	}

	public WrongStatusException() {
		super();
	}

	@Override
	public String getMessage() {

		// We deferred converting the message ids to messages until now, when we are more likely to be in the user's
		// locale.

		Object[] msgArgs = new Object[] { _expectedStatus.toString(), _foundStatus.toString() };

		String msg = MessageUtil.toText("WrongStatusException", msgArgs);
		return msg;
	}

}
