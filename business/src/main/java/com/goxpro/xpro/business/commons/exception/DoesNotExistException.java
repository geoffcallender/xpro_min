package com.goxpro.xpro.business.commons.exception;

import java.io.Serializable;

import javax.ejb.ApplicationException;

import com.goxpro.xpro.business.MessageUtil;
import com.goxpro.xpro.util.ClassUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class DoesNotExistException extends BusinessException {
	private Class<?> clazz;
	private String entityLabelMessageId;
	private Serializable id;

	/**
	 * Throw this exception when an object is requested but does not exist.
	 * 
	 * @param clazz the class of the object being requested. It will be stripped down to its unqualified name (eg.
	 *        com.goxpro.xpro.Department would be stripped down to Department) to be used as a message key when
	 *        generating a message in getMessage().
	 * @param id the id of the object being requested.
	 */
	public DoesNotExistException(Class<?> clazz, Serializable id) {

		// Don't convert the message ids to messages yet because we're in the server's locale, not the user's.

		super();
		this.clazz = clazz;
		this.entityLabelMessageId = ClassUtil.extractUnqualifiedName(clazz);
		this.id = id;
	}

	@Override
	public String getMessage() {

		// We deferred converting the message ids to messages until now, when we are more likely to be in the user's
		// locale.

		Object[] msgArgs = new Object[] { MessageUtil.toText(entityLabelMessageId), id };

		String msg = MessageUtil.toText("DoesNotExistException", msgArgs);
		return msg;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getEntityLabelMessageId() {
		return entityLabelMessageId;
	}

	public Serializable getId() {
		return id;
	}
}
