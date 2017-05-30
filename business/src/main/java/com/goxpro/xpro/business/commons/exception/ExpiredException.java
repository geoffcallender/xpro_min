package com.goxpro.xpro.business.commons.exception;

import java.io.Serializable;

import javax.ejb.ApplicationException;

import com.goxpro.xpro.business.MessageUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class ExpiredException extends BusinessException {
	private Serializable id;

	/**
	 * Throw this exception when an object is requested but does not exist.
	 * 
	 * @param id the id of the object being requested.
	 */
	public ExpiredException(Serializable id) {

		// Don't convert the message ids to messages yet because we're in the server's locale, not the user's.

		super();
		this.id = id;
	}

	@Override
	public String getMessage() {

		// We deferred converting the message ids to messages until now, when we are more likely to be in the user's
		// locale.

		Object[] msgArgs = new Object[] { id };

		String msg = MessageUtil.toText("ExpiredException", msgArgs);
		return msg;
	}

	public Serializable getId() {
		return id;
	}
}
