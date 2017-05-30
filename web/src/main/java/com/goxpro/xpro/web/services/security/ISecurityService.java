package com.goxpro.xpro.web.services.security;

import javax.security.auth.Subject;

public interface ISecurityService {

	Object adoptNewSecurityContext(Subject subject, Subject runAsSubject); // Returns previous state, to pass into
																			// restoreOldSecurityContext(state) where it
																			// will be restored.

	void restoreOldSecurityContext(Object context);

}
