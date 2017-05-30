package com.goxpro.xpro.business.domain.base;

import java.security.Principal;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is part of a solution to the following problem: our entity beans want to know which user is creating/modifying
 * them, but entity beans cannot do what session beans do, which is inject SessionContext and call
 * SessionContext#getCallerPrincipal().
 * <p/>
 * Use this class as an interceptor on your session beans, ie. annotate the session bean class with
 * <code>@Interceptors({ UsernameCacheRefresher.class })</code>.
 * <p/>
 * As each method of the session bean is invoked, this interceptor gets the name of the caller principal and saves it in
 * UsernameCache.
 * <p/>
 * When an entity bean wants the name of the caller principal, it can call BaseEntity#getCurrentUsername(), which in
 * turn calls UsernameCache.getThreadUsername().
 * <p/>
 * This solution depends on the caller principal being set up by SecurityContextPropagator in the web tier.
 */
@Interceptor
public class UsernameCacheRefresher {

	@Resource
	private javax.ejb.SessionContext context;

	private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

	@AroundInvoke
	public Object refreshUsernameCache(InvocationContext ic) throws Exception {

		Principal callerPrincipal = context.getCallerPrincipal();

		if (callerPrincipal != null) {
			UsernameCache.setThreadUsername(callerPrincipal.getName());
			// System.out.println(">>>>>>>>>>>>>>>> In UsernameCacheRefresher, principal.getName() = "
			// + callerPrincipal.getName());
		}
		else {
			// Is this even possible? Isn't there a default principal of guest (OpenEJB) or anonymous (JBoss)?
			logger.error("WATCH OUT - UsernameCacheRefresher found callerPrincipal is null!!!!!!!!!!!!!");
			UsernameCache.setThreadUsername(null);
		}

		return ic.proceed();
	}

}
