package com.goxpro.xpro.web.state;

import java.io.Serializable;

import javax.security.auth.login.LoginContext;

import com.goxpro.xpro.util.StringUtil;

@SuppressWarnings("serial")
public class Visit implements Serializable {

	private LoginContext loginContext = null;
	private String myUsername = null;

	public Visit(String myUsername, LoginContext loginContext) {

		if (StringUtil.isEmpty(myUsername)) {
			throw new IllegalArgumentException();
		}

		if (loginContext == null) {
			throw new IllegalArgumentException();
		}

		this.myUsername = myUsername;
		this.loginContext = loginContext;
	}

	public LoginContext getLoginContext() {
		return loginContext;
	}

	public String getMyUsername() {
		return myUsername;
	}

}
