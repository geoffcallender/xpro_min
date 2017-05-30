// Based on http://svn.apache.org/repos/asf/openejb/trunk/openejb3/assembly/openejb-tomcat/openejb-tomcat-catalina/src/main/java/org/apache/openejb/tomcat/catalina/TomcatSecurityService.java
// as suggested here http://n4.nabble.com/How-to-propagate-security-context-from-Jetty-to-OpenEJB-td1289042.html#a1289042

package com.goxpro.xpro.web.services.security;

import java.util.LinkedList;
import java.util.UUID;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import org.apache.openejb.core.security.AbstractSecurityService;

public class OpenEJBSecurityService extends AbstractSecurityService implements ISecurityService {

	static protected final ThreadLocal<LinkedList<Subject>> runAsStack = new ThreadLocal<LinkedList<Subject>>() {
		protected LinkedList<Subject> initialValue() {
			return new LinkedList<Subject>();
		}
	};

	public OpenEJBSecurityService() {
	}

	@Override
	public UUID login(String realmName, String username, String password) throws LoginException {
		throw new UnsupportedOperationException();
	}

	// @Override
	// public UUID login(String arg0, String arg1, String arg2, long arg3) throws LoginException {
	// throw new UnsupportedOperationException();
	// }

	@Override
	public Object adoptNewSecurityContext(Subject subject, Subject runAsSubject) {
		Identity newIdentity = null;

		if (subject != null) {
			newIdentity = new Identity(subject, null);
		}

		// Save the current identity and current "run as" subject

		Identity oldIdentity = clientIdentity.get();
		SecurityContextInfo oldSecurityContextInfo = new SecurityContextInfo(oldIdentity, runAsSubject != null);

		// Set the new identity and new "run as" subject

		clientIdentity.set(newIdentity);

		if (runAsSubject != null) {
			runAsStack.get().addFirst(runAsSubject);
		}

		return oldSecurityContextInfo;
	}

	@Override
	public void restoreOldSecurityContext(Object oldSecurityContextInfo) {

		// Restore the old identity and old "run as" subject

		if (oldSecurityContextInfo instanceof SecurityContextInfo) {
			SecurityContextInfo securityContextInfo = (SecurityContextInfo) oldSecurityContextInfo;
			clientIdentity.set(securityContextInfo.oldIdentity);

			if (securityContextInfo.hadRunAs) {
				runAsStack.get().removeFirst();
			}
		}
	}

	// protected Subject getRunAsSubject(CoreDeploymentInfo callingDeploymentInfo) {
	// Subject runAsSubject = super.getRunAsSubject(callingDeploymentInfo);
	// if (runAsSubject != null)
	// return runAsSubject;
	//
	// LinkedList<Subject> stack = runAsStack.get();
	// if (stack.isEmpty()) {
	// return null;
	// }
	// return stack.getFirst();
	// }
	//
	// public boolean isCallerInRole(String role) {
	// if (role == null)
	// throw new IllegalArgumentException("Role must not be null");
	//
	// LinkedList<Subject> stack = runAsStack.get();
	// if (stack.isEmpty()) {
	// return false;
	// }
	// Subject subject = stack.getFirst();
	//
	// for (Principal principal : subject.getPrincipals(GoXproRolePrincipal.class)) {
	// if (principal.getName().equals(role)) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	private static class SecurityContextInfo {
		private final Identity oldIdentity;
		private final boolean hadRunAs;

		public SecurityContextInfo(Identity oldIdentity, boolean hadRunAs) {
			this.oldIdentity = oldIdentity;
			this.hadRunAs = hadRunAs;
		}
	}
}
