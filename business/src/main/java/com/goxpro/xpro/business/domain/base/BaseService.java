package com.goxpro.xpro.business.domain.base;

import java.io.Serializable;
import java.security.Principal;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.slf4j.LoggerFactory;

import com.goxpro.xpro.business.commons.exception.BusinessException;
import com.goxpro.xpro.business.commons.exception.CannotDeleteIsReferencedException;
import com.goxpro.xpro.business.commons.exception.DoesNotExistException;
import com.goxpro.xpro.business.commons.exception.DuplicateAlternateKeyException;
import com.goxpro.xpro.business.commons.exception.DuplicatePrimaryKeyException;
import com.goxpro.xpro.business.commons.exception.GenericBusinessException;
import com.goxpro.xpro.business.commons.exception.OptimisticLockException;
import com.goxpro.xpro.business.commons.interpreter.BusinessServiceExceptionInterpreter;
import com.goxpro.xpro.util.EJBProviderEnum;
import com.goxpro.xpro.util.EJBProviderUtil;

public abstract class BaseService {

	private final String demoModeStr = System.getProperty("goxpro.demo-mode");

	@PersistenceContext(unitName = "xpro")
	protected EntityManager em;

	@Resource
	protected SessionContext context;

	// @EJB
	// private IAuthorizationFinderServiceLocal authorizationFinderService;

	protected final BusinessServiceExceptionInterpreter businessServiceExceptionInterpreter = new BusinessServiceExceptionInterpreter();

	protected final EJBProviderEnum ejbProvider = EJBProviderUtil
			.detectEJBProvider(LoggerFactory.getLogger(BaseService.class));

	protected final boolean inOpenEJB = (ejbProvider == EJBProviderEnum.OPENEJB_4_LOCAL
			|| ejbProvider == EJBProviderEnum.OPENEJB_4_REMOTE
			|| ejbProvider == EJBProviderEnum.TOMCAT_7_OPENEJB_4_LOCAL);

	protected void persist(BaseEntity entity) throws BusinessException {
		if (entity == null) {
			throw new IllegalArgumentException("persist(entity) has been given a null entity.");
		}
		try {
			em.persist(entity);
		}
		catch (Exception e) {
			interpretAsBusinessExceptionAndThrowIt(e, entity, entity.getIdForMessages());
			throw new IllegalStateException("Shouldn't get here.", e);
		}
	}

	/**
	 * Beware - the caller MUST USE THE RESULT, eg. invoice = merge(invoice).
	 * 
	 * @param <T>
	 * @param entity
	 * @return
	 * @throws BusinessException
	 */
	protected <T extends BaseEntity> T merge(T entity) throws BusinessException {
		if (entity == null) {
			throw new IllegalArgumentException("merge(entity) has been given a null entity.");
		}
		try {
			entity = em.merge(entity);
		}
		catch (Exception e) {
			interpretAsBusinessExceptionAndThrowIt(e, entity, entity.getIdForMessages());
			throw new IllegalStateException("Shouldn't get here.", e);
		}
		return entity;
	}

	protected void remove(BaseEntity entity) throws BusinessException {
		if (entity == null) {
			throw new IllegalArgumentException("remove(entity) has been given a null entity.");
		}
		try {
			em.remove(entity);
		}
		catch (Exception e) {
			interpretAsBusinessExceptionAndThrowIt(e, entity, entity.getIdForMessages());
			throw new IllegalStateException("Shouldn't get here.", e);
		}
	}

	protected <T> T find(Class<T> cls, Serializable id) {

		if (id == null) {
			throw new IllegalArgumentException(
					"find(class, id) has been given null id.  Class is " + cls.getName() + ".");
		}
		else if (id.equals(0)) {
			throw new IllegalArgumentException(
					"find(class, id) has been given zero id.  Class is " + cls.getName() + ".");
		}

		try {
			T obj = em.find(cls, id);
			return obj;
		}
		catch (IllegalArgumentException e) {
			// Invalid id
			throw new IllegalArgumentException(
					"find(class, id) has been given invalid id.  Class is " + cls.getName() + ", id is \"" + id + "\".",
					e);
		}
		// catch (Exception e) {
		// // Doesn't exist
		// return null;
		// }
	}

	protected <T> T findStrict(Class<T> cls, Serializable id) throws DoesNotExistException {

		if (id == null) {
			throw new IllegalArgumentException(
					"findStrict(class, id) has been given null id.  Class is " + cls.getName() + ".");
		}
		else if (id.equals(0)) {
			throw new IllegalArgumentException(
					"findStrict(class, id) has been given zero id.  Class is " + cls.getName() + ".");
		}

		try {
			T obj = em.find(cls, id);

			if (obj == null) {
				throw new DoesNotExistException(cls, id);
			}

			return obj;
		}
		catch (IllegalArgumentException e) {
			// Invalid id
			throw new IllegalArgumentException("findStrict(class, id) has been given invalid id.  Class is "
					+ cls.getName() + ", id is \"" + id + "\".", e);
		}
		catch (Exception e) {
			// Doesn't exist
			throw new DoesNotExistException(cls, id);
		}
	}

	protected void lock(BaseEntity entity, LockModeType lockModeType) {

		// This needs work (esp. converting exception to a BusinessException ?)

		em.lock(entity, lockModeType);
	}

	/**
	 * Calls to this method are a workaround for https://issues.apache.org/jira/browse/TOMEE-172 and and
	 * https://issues.apache.org/jira/browse/GERONIMO-3907 .
	 * 
	 * @throws BusinessException
	 */
	protected void flushToWorkAroundTOMEE_172() throws BusinessException {
		if (inOpenEJB) {
			try {
				em.flush();
			}
			catch (Exception e) {
				interpretAsBusinessExceptionAndThrowIt(e);
				throw new IllegalStateException("Shouldn't get here.", e);
			}
		}
	}

	/**
	 * Calls to this method are for a normal flush that you require regardless of whether you're using OPENEJB or not.
	 * See also flushToWorkAroundTOMEE_172().
	 * 
	 * @throws BusinessException
	 */
	protected void flush() throws BusinessException {
		try {
			em.flush();
		}
		catch (Exception e) {
			interpretAsBusinessExceptionAndThrowIt(e);
			throw new IllegalStateException("Shouldn't get here.", e);
		}
	}

	/**
	 * Gets the principal set in the calling tier, eg. our web tier sets up the principal in
	 * IBusinessLayerSecurityService#enterWebApp(...). In OpenEJB the principal is carried in ClientIdentity, and in
	 * JBoss the principal is carried in a JAAS LoginModule.
	 * 
	 * @return
	 */
	public String getUsername() {
		Principal principal = context.getCallerPrincipal();
		return principal.getName();
	}

	// @Deprecated
	// public Long getUserId() throws DoesNotExistException {
	// Principal principal = _context.getCallerPrincipal();
	// // System.out.println(">>>>>>>> principal = " + principal);
	// // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> principal.getName() = " + principal.getName());
	// String principalName = principal.getName();
	//
	// if (principalName.equals("guest")) {
	// return 2L;
	// }
	// else {
	// // User user = authorizationFinderService.findUserByUsername(principal.getName());
	// // return user.getId();
	// return 1234L;
	// }
	// }

	public Long getSystemUserId() {
		return 1L;
	}

	protected void checkOptimisticLock(Integer expectedVersion, Integer foundVersion, BaseEntity entity,
			Serializable id) throws OptimisticLockException {
		if (!foundVersion.equals(expectedVersion)) {
			// TODO - Is it OK like this ie. having null for the exception arg?
			throw new OptimisticLockException(entity, id, null);
		}
	}

	protected void interpretAsBusinessExceptionAndThrowIt(Throwable t) throws BusinessException {

		BusinessException be = businessServiceExceptionInterpreter.interpret(t);

		// "Repackage" certain exceptions that we have more info about

		if (be instanceof DuplicatePrimaryKeyException) {
			be = new DuplicatePrimaryKeyException();
		}
		else if (be instanceof DuplicateAlternateKeyException) {
			be = new DuplicateAlternateKeyException(be.getMessage());
		}
		else if (be instanceof CannotDeleteIsReferencedException) {
			CannotDeleteIsReferencedException c = (CannotDeleteIsReferencedException) be;
			if (c.getInformationLevel() == CannotDeleteIsReferencedException.INFORMATIONLEVEL_ENTITY_ID_REFBYENTITY_REFBYPROPERTY) {
				be = new CannotDeleteIsReferencedException(c.getReferencedByEntityName());
			}
		}

		throw be;
	}

	protected void interpretAsBusinessExceptionAndThrowIt(Throwable t, BaseEntity entity, Serializable id)
			throws BusinessException {

		BusinessException be = businessServiceExceptionInterpreter.interpret(t);

		// "Repackage" certain exceptions that we have more info about

		if (be instanceof DuplicatePrimaryKeyException) {
			be = new DuplicatePrimaryKeyException(entity, id);
		}
		else if (be instanceof DuplicateAlternateKeyException) {
			be = new DuplicateAlternateKeyException(entity, be.getMessage());
		}
		else if (be instanceof CannotDeleteIsReferencedException) {
			CannotDeleteIsReferencedException c = (CannotDeleteIsReferencedException) be;
			if (c.getInformationLevel() == CannotDeleteIsReferencedException.INFORMATIONLEVEL_ENTITY_ID_REFBYENTITY_REFBYPROPERTY) {
				be = new CannotDeleteIsReferencedException(entity, id, c.getReferencedByEntityName(),
						c.getReferencedByPropertyName());
			}
		}

		throw be;
	}

	/*******************************************************************************************************************
	 * PRIVATE METHODS
	 ******************************************************************************************************************/

	// These methods are solely to protect the database in the demo site

	protected void checkNotADemo() throws GenericBusinessException {
		if (isADemo()) {
			throw new GenericBusinessException("Demo_prohibited_function");
		}
	}

	protected boolean isADemo() {
		boolean readonly = false;
		if (demoModeStr != null) {
			if (demoModeStr.equalsIgnoreCase("true")) {
				readonly = true;
			}
			else if (demoModeStr.equalsIgnoreCase("false")) {
				readonly = false;
			}
			else {
				throw new IllegalStateException("System property goxpro.demo-mode has been set to \"" + demoModeStr

						+ "\".  Please set it to \"true\" or \"false\".  If not specified at all then it will default to \"false\".");
			}
		}
		return readonly;
	}
}
