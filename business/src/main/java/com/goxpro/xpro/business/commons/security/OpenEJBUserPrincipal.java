package com.goxpro.xpro.business.commons.security;

import org.apache.openejb.spi.CallerPrincipal;

/**
 * <p>
 * This class implements the <code>Principal</code> interface and represents a goXpro user.
 * 
 * <p>
 * Principals such as this <code>GOpenEJBUserPrincipal</code> may be associated with a particular <code>Subject</code> to
 * augment that <code>Subject</code> with an additional identity. Refer to the <code>Subject</code> class for more
 * information on how to achieve this. Authorization decisions can then be based upon the Principals associated with a
 * <code>Subject</code>.
 * 
 * @version 1.4, 01/11/00
 * @see java.security.Principal
 * @see javax.security.auth.Subject
 */
@SuppressWarnings("serial")
@CallerPrincipal
public class OpenEJBUserPrincipal implements java.io.Serializable, IUserPrincipal {

	/**
	 * @serial
	 */
	private String name;

	/**
	 * Create a GoXproPrincipal with a Sample username.
	 * 
	 * @param name the Sample username for this user.
	 * @exception NullPointerException if the <code>name</code> is <code>null</code>.
	 */
	public OpenEJBUserPrincipal(String name) {
		if (name == null)
			throw new NullPointerException("illegal null input");

		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.goxpro.xpro.business.commons.security.IGoXproUserPrincipal#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.goxpro.xpro.business.commons.security.IGoXproUserPrincipal#toString()
	 */
	@Override
	public String toString() {
		return ("GoXproUserPrincipal:  " + name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.goxpro.xpro.business.commons.security.IGoXproUserPrincipal#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		if (this == o) {
			return true;
		}

		if (!(o instanceof OpenEJBUserPrincipal)) {
			return false;
		}

		OpenEJBUserPrincipal that = (OpenEJBUserPrincipal) o;

		if (this.getName().equals(that.getName())) {
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.goxpro.xpro.business.commons.security.IGoXproUserPrincipal#hashCode()
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
