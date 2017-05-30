package com.goxpro.xpro.business.commons.security;

/**
 * <p>
 * This class implements the <code>Principal</code> interface and represents a goXpro user.
 * 
 * <p>
 * Principals such as this <code>JBossUserPrincipal</code> may be associated with a particular <code>Subject</code> to
 * augment that <code>Subject</code> with an additional identity. Refer to the <code>Subject</code> class for more
 * information on how to achieve this. Authorization decisions can then be based upon the Principals associated with a
 * <code>Subject</code>.
 * 
 * @version 1.4, 01/11/00
 * @see java.security.Principal
 * @see javax.security.auth.Subject
 */
@SuppressWarnings("serial")
public class JBossUserPrincipal implements IUserPrincipal, java.io.Serializable {

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
	public JBossUserPrincipal(String name) {
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
		return ("GoXproUserPrincipal:  " + name);
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
		if (o == null) {
			return false;
		}

		if (this == o) {
			return true;
		}

		if (!(o instanceof JBossUserPrincipal)) {
			return false;
		}

		JBossUserPrincipal that = (JBossUserPrincipal) o;

		if (this.getName().equals(that.getName())) {
			return true;
		}

		return false;
	}

	/**
	 * Return a hash code for this <code>GoXproPrincipal</code>.
	 */
	public int hashCode() {
		return name.hashCode();
	}
}
