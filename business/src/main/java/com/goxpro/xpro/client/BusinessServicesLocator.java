package com.goxpro.xpro.client;

import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.goxpro.xpro.business.commons.jndi.JndiObjectLocator;
import com.goxpro.xpro.util.EJBProviderEnum;
import com.goxpro.xpro.util.EJBProviderUtil;

/**
 * BusinessServicesLocator is used to centralize all lookups of business services in JNDI. At the time of writing it is
 * used by the business tier's BaseTest and the web tier's EJBAnnotationWorker and PageProtectionFilter.
 * 
 * This version knows the formats of JNDI names assigned by OpenEJB, GlassFish, and JBoss (the EJB 3.1 specification
 * only "almost" standardized them). It minimises the overhead of JNDI lookups by caching the objects it looks up. If
 * this class becomes a bottleneck, then you may need to decentralise it.
 */
public class BusinessServicesLocator extends JndiObjectLocator implements IBusinessServicesLocator {
	private static final String REMOTE = "REMOTE";
	private static final String LOCAL = "LOCAL";
	private static final Pattern StripLocalPattern = Pattern.compile(LOCAL + "|" + REMOTE, Pattern.CASE_INSENSITIVE);

	private final EJBProviderEnum ejbProvider;

	public BusinessServicesLocator(Logger logger) {
		super(logger);
		// You wouldn't normally have to do this but Xpro has to deal with many types of environment...
		this.ejbProvider = EJBProviderUtil.detectEJBProvider(logger);
	}

	/**
	 * An example of interfaceClass is IPersonServiceLocal.
	 */
	@Override
	public Object getService(Class<?> interfaceClass) {
		return getService(interfaceClass.getCanonicalName());
	}

	/**
	 * We expect canonicalInterfaceName to be like "com.goxpro.xpro.business.domain.examples.iface.IPersonServiceLocal",
	 * ie. its simple name has a leading "I" and trailing "Local" or "Remote".
	 */
	@Override
	public Object getService(String canonicalInterfaceName) {
		String jndiName = null;

		// You wouldn't normally have to do all this work but Xpro has to deal with many types of environment and
		// EJB 3.1 still hasn't quite standardised JNDI names.

		if (ejbProvider == EJBProviderEnum.OPENEJB_4_LOCAL || ejbProvider == EJBProviderEnum.TOMCAT_7_OPENEJB_4_LOCAL
				|| ejbProvider == EJBProviderEnum.OPENEJB_4_REMOTE) {
			// Uses the implementation class name eg. "PersonServiceLocal".
			jndiName = getSimpleName(canonicalInterfaceName).substring(1);
		}
		else {
			throw new IllegalStateException("Don't know how to use ejbProvider = " + ejbProvider);
		}

		return getJNDIObject(jndiName);
	}

	private static String getSimpleName(String s) {
		return s.substring(s.lastIndexOf(".") + 1);
	}

	private static String stripOffLocalOrRemote(String s) {
		String stripped = s;
		String uc = s.toUpperCase();

		if (uc.endsWith(LOCAL) || uc.endsWith(REMOTE)) {
			stripped = StripLocalPattern.matcher(s).replaceFirst("");
		}

		return stripped;
	}
}
