package com.goxpro.xpro.business.commons.security;

import java.security.Principal;

@SuppressWarnings("serial")
public class RolePrincipal implements Principal, java.io.Serializable {

	/**
	 * @serial
	 */
	private String name;

	/**
	 * Create a GoXproPrincipal with a Sample username.
	 * 
	 * <p>
	 * 
	 * @param name the Sample username for this user.
	 * 
	 * @exception NullPointerException if the <code>name</code> is <code>null</code>.
	 */
	public RolePrincipal(String name) {
		if (name == null)
			throw new NullPointerException("illegal null input");

		this.name = name;
	}

	/**
	 * Return the Sample username for this <code>GoXproPrincipal</code>.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return a string representation of this <code>GoXproPrincipal</code>.
	 */
	public String toString() {
		return ("GoXproRolePrincipal:  " + name);
	}

	/**
	 * Compares the specified Object with this <code>GoXproPrincipal</code> for equality. Returns true if the given
	 * object is also a <code>GoXproPrincipal</code> and the two GoXproPrincipals have the same username.
	 * 
	 * <p>
	 * 
	 * @param o Object to be compared for equality with this <code>GoXproPrincipal</code>.
	 * 
	 * @return true if the specified Object is equal equal to this <code>GoXproPrincipal</code>.
	 */
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (this == o)
			return true;

		if (!(o instanceof RolePrincipal))
			return false;
		RolePrincipal that = (RolePrincipal) o;

		if (this.getName().equals(that.getName()))
			return true;
		return false;
	}

	/**
	 * Return a hash code for this <code>GoXproPrincipal</code>.
	 */
	public int hashCode() {
		return name.hashCode();
	}
}
