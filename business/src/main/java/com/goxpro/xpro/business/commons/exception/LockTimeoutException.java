package com.goxpro.xpro.business.commons.exception;

import javax.ejb.ApplicationException;

import com.goxpro.xpro.business.MessageUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class LockTimeoutException extends BusinessException {
	private Throwable rootCause;

	/**
	 * This exception is thrown by an IPersistenceExceptionInterpreter when a javax.persistence.LockTimeoutException has
	 * been detected.
	 * 
	 * This is unwanted in a well-running system, so be sure to log them in the IPersistenceExceptionInterpreter as
	 * ERROR, watch the logs for them, then try to eliminate their cause.
	 * 
	 * @param rootCause the root cause of the exception.
	 */
	public LockTimeoutException(Throwable rootCause) {

		// Don't convert the message ids to messages yet because we're in the server's locale, not the user's.

		super();

		this.rootCause = rootCause;
	}

	@Override
	public String getMessage() {
		String originalThrowableMessage = rootCause == null ? "" : rootCause.getLocalizedMessage();
		Object[] msgArgs = new Object[] { originalThrowableMessage };

		String msg = MessageUtil.toText("LockTimeoutException", msgArgs);
		return msg;
	}

	public Throwable getThrowable() {
		return rootCause;
	}
}
