// Based on http://www.tapina.com/blog/spring-security-propagate-principal-ejb.html

package com.goxpro.xpro.web.services.security;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.login.LoginContext;

public class JBossSecurityService implements ISecurityService {

	private static String JBOSS_CLIENT_LOGIN_MODULE_NAME = "client-login";

	private LoginContext loginContext = null;

	public JBossSecurityService() {
	}

	public Object adoptNewSecurityContext(Subject subject, Subject runAsSubject) {
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>> In adoptNewSecurityContext, subject = " + subject);

		LoginContext newLoginContext = null;

		if (subject != null) {
			CallbackHandler callbackHandler = new SubjectCallbackHandler(subject);

			try {
				NameCallback nameCallback = new NameCallback("Enter username");
				callbackHandler.handle(new Callback[] { nameCallback });

				// We use the JBoss client-login module because it rightly assumes we've already done the authentication

				newLoginContext = new LoginContext(JBOSS_CLIENT_LOGIN_MODULE_NAME, callbackHandler);
				newLoginContext.login();
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		// Save the old state

		SecurityContextInfo oldSecurityContextInfo = new SecurityContextInfo(loginContext);

		// Set the new state

		this.loginContext = newLoginContext;

		return oldSecurityContextInfo;
	}

	public void restoreOldSecurityContext(Object oldSecurityContextInfo) {
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>> In restoreOldSecurityContext, oldSecurityContextInfo = " + oldSecurityContextInfo);

		if (oldSecurityContextInfo instanceof SecurityContextInfo) {
			SecurityContextInfo oldWebAppState = (SecurityContextInfo) oldSecurityContextInfo;
			this.loginContext = oldWebAppState.loginContext;

			// Is this necessary? I doubt it.
			// try {
			// this.loginContext.login();
			// }
			// catch (Exception e) {
			// e.printStackTrace();
			// throw new RuntimeException(e);
			// }
		}
	}

	private static class SecurityContextInfo {
		private final LoginContext loginContext;

		public SecurityContextInfo(LoginContext loginContext) {
			this.loginContext = loginContext;
		}
	}
}
