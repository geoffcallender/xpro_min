package com.goxpro.xpro.business.commons.security;

import java.security.Principal;


public interface IUserPrincipal extends Principal {

	/**
	 * Return the Sample username for this <code>GoXproPrincipal</code>.
	 */
	public abstract String getName();

	/**
	 * Return a string representation of this <code>GoXproPrincipal</code>.
	 */
	public abstract String toString();

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
	public abstract boolean equals(Object o);

	/**
	 * Return a hash code for this <code>GoXproPrincipal</code>.
	 */
	public abstract int hashCode();

}
