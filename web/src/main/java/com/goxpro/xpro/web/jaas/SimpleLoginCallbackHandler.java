package com.goxpro.xpro.web.jaas;

import java.io.IOException;
import java.io.Serializable;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * Utility class to capture login and password for JAAS authentication.
 */
public class SimpleLoginCallbackHandler implements CallbackHandler, Serializable {
	public static final long serialVersionUID = -3007103690276274772L;
	private String username;

	private char[] password;

	/**
	 * Constructor
	 * 
	 * @param username the users login name.
	 * @param password the associated password.
	 */
	public SimpleLoginCallbackHandler(String username, String password) {
		assert username != null;
		assert password != null;

		this.username = username;
		this.password = password.toCharArray();
	}

	/**
	 * callback method from JAAS
	 * 
	 * @param callbacks request for info
	 * @throws IOException on error
	 * @throws UnsupportedCallbackException on error
	 */
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				NameCallback nc = (NameCallback) callbacks[i];
				nc.setName(username);
			}
			else if (callbacks[i] instanceof PasswordCallback) {
				PasswordCallback pc = (PasswordCallback) callbacks[i];
				pc.setPassword(password);
			}
			else {
				throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
			}
		}
	}
}
