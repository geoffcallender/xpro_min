package com.goxpro.xpro.web.services;

import java.io.IOException;

import javax.security.auth.Subject;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.slf4j.Logger;

import com.goxpro.xpro.web.services.security.ISecurityService;
import com.goxpro.xpro.web.services.security.SecurityServiceLocator;
import com.goxpro.xpro.web.state.Visit;

/**
 * If there's a security service (there should be) and the user has a Visit (ie. they've logged in) then:
 * <ol>
 * <li>Call the security service to propagate the user's security context;
 * <li>Pass the request to the next handler, and wait until it returns;
 * <li>Call the security service to reinstate its old security context.
 * </ol>
 */
public class SecurityContextPropagator implements ComponentRequestFilter {

	private ApplicationStateManager sessionStateManager;
	@SuppressWarnings("unused")
	private SecurityServiceLocator securityServiceLocator;
	@SuppressWarnings("unused")
	private final Logger logger;

	private ISecurityService securityService;

	public SecurityContextPropagator(ApplicationStateManager asm, SecurityServiceLocator securityServiceLocator,
			Logger logger) {
		this.sessionStateManager = asm;
		this.securityServiceLocator = securityServiceLocator;
		this.logger = logger;

		this.securityService = securityServiceLocator.getSecurityService();
	}

	@Override
	public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {
		Object oldState = null;

		if (securityService != null) {
			Subject subject = null;

			// If logged in, get the subject
			if (sessionStateManager.exists(Visit.class)) {
				Visit visit = sessionStateManager.get(Visit.class);

				if (visit != null) {
					subject = visit.getLoginContext().getSubject();
				}
			}

			oldState = securityService.adoptNewSecurityContext(subject, subject);
		}

		try {

			handler.handlePageRender(parameters);

		}
		finally {
			if (securityService != null) {
				securityService.restoreOldSecurityContext(oldState);
			}
		}
	}

	@Override
	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {
		Object oldState = null;

		if (securityService != null) {
			Subject subject = null;

			// If logged in, get the subject
			if (sessionStateManager.exists(Visit.class)) {
				Visit visit = sessionStateManager.get(Visit.class);

				if (visit != null) {
					subject = visit.getLoginContext().getSubject();
				}
			}

			oldState = securityService.adoptNewSecurityContext(subject, subject);
		}

		try {

			handler.handleComponentEvent(parameters);

		}
		finally {
			if (securityService != null) {
				securityService.restoreOldSecurityContext(oldState);
			}
		}
	}

}
