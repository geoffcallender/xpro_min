package com.goxpro.xpro.web.services.security;

import org.apache.openejb.loader.SystemInstance;
import org.apache.openejb.spi.SecurityService;
import org.slf4j.Logger;

import com.goxpro.xpro.util.EJBProviderEnum;
import com.goxpro.xpro.util.EJBProviderUtil;

public class SecurityServiceLocator {

	private final EJBProviderEnum ejbProvider;

	private static ISecurityService securityService;

	public SecurityServiceLocator(Logger logger) {

		ejbProvider = EJBProviderUtil.detectEJBProvider(logger);

		if (ejbProvider == EJBProviderEnum.OPENEJB_4_LOCAL) {
			securityService = (ISecurityService) SystemInstance.get().getComponent(SecurityService.class);
		}
		else {
			throw new IllegalStateException(
					"We do not have an ISecurityService for the given EJB provider. Expected provider OPENEJB_4_LOCAL or JBOSS_7_LOCAL, found "
							+ ejbProvider.name() + ".");
		}

	}

	public ISecurityService getSecurityService() {
		return securityService;
	}

}
