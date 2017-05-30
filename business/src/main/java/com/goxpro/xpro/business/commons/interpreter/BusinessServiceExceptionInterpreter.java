package com.goxpro.xpro.business.commons.interpreter;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goxpro.xpro.business.commons.exception.BusinessException;
import com.goxpro.xpro.business.commons.exception.UnexpectedException;
import com.goxpro.xpro.util.ExceptionUtil;

public class BusinessServiceExceptionInterpreter {
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceExceptionInterpreter.class);

	private IPersistenceExceptionInterpreter persistenceExceptionInterpreter = new HibernatePersistenceExceptionInterpreter();

	/**
	 * Interpret a Throwable into a Xpro BusinessException or throw an UnexpectedException.
	 */
	public BusinessException interpret(Throwable t) {
		try {
			BusinessException be = null;

			if (t instanceof BusinessException) {
				be = (BusinessException) t;
			}

			else if (t instanceof javax.validation.ConstraintViolationException) {
				be = interpretConstraintViolationException((javax.validation.ConstraintViolationException) t);
			}

			else if (t instanceof javax.persistence.PersistenceException) {
				be = persistenceExceptionInterpreter.interpret((javax.persistence.PersistenceException) t);
			}

			else if (t instanceof java.sql.SQLException) {
				// SQLException is leaking out of JBoss 5.0 in duplicate alternate key situations
				be = persistenceExceptionInterpreter.interpret((java.sql.SQLException) t);
			}

			else if (t.getCause() != null && !t.getCause().equals(t)) {
				be = interpret(t.getCause());
			}

			else {
				LOGGER.error("Cannot interpret Throwable " + t);

				if (t.getMessage() != null && t.getMessage().contains("setRollbackOnly() called")) {
					LOGGER.error(
							">>>>>>>> You're probably using OpenEJB. Make sure your EJB calls flushToWorkAroundTOMEE_172()!!!!!!!!");
				}

				throw new UnexpectedException(t);
			}

			return be;
		}
		catch (UnexpectedException e) {
			LOGGER.error(ExceptionUtil.printStackTrace(e));
			LOGGER.error("  Caused by: " + e.getCause());
			if (e.getRootCause() != null) {
				LOGGER.error("   Root cause: " + e.getRootCause().toString());
			}
			throw e;
		}
	}

	private BusinessException interpretConstraintViolationException(ConstraintViolationException e) {

		// TODO - consider whether doing this is a good idea. Is the resulting message localised? Suitable for a user?
		// Or should the fact that the web tier failed to detect it indicate that our app is getting dodgey and so a
		// "sorry, please call tech support" error message may be better?

		// TODO - we need a way to return more than one error message and the web pages need a way to record those
		// multiple messages.

		// Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		// for (ConstraintViolation<?> constraintViolation : constraintViolations) {
		// // form.recordError(constraintViolation.getMessage());
		// return new GenericBusinessException(constraintViolation.getMessage());
		// }
		//
		// return null;
		throw e;
	}

}
