package com.goxpro.xpro.web.services.security;

import java.io.IOException;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import com.goxpro.xpro.business.commons.security.IUserPrincipal;

public class SubjectCallbackHandler implements CallbackHandler {
	@SuppressWarnings("unused")
	private Subject subject = null;
	private IUserPrincipal user = null;

	public SubjectCallbackHandler(Subject subject) {
		this.subject = subject;

		Set<IUserPrincipal> users = subject.getPrincipals(IUserPrincipal.class);
		
		if (users.size() < 1) {
			throw new IllegalStateException();
		}
		else if (users.size() > 1) {
			throw new IllegalStateException(users.toString());
		}

		this.user = users.iterator().next();
		// System.out.println(">>>>>>>>>>>>>>>>>>> In SubjectCallbackHandler, user = " + user);
	}

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (Callback callback : callbacks) {
			if (callback instanceof NameCallback) {
				((NameCallback) callback).setName(user.getName());
			}
		}

	}
}
