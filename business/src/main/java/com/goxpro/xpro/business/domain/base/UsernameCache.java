package com.goxpro.xpro.business.domain.base;

/**
 * Based on http://thediningphilosopher.blogspot.com/2008/07/help-me-threadlocal-variables.html and
 * http://www.theserverside.com/tt/articles/article.tss?l=JPAObjectModel .
 */
public class UsernameCache {

	// WARNING - I've seen mention that using ThreadLocal may not be enough in JBoss nested transactions. If this turns
	// out to be true, have a look at JBoss class TransactionLocal.

	private static final ThreadLocal<String> threadUsername = new ThreadLocal<String>();

	public static String getThreadUsername() {
		return threadUsername.get();
	}

	public static void setThreadUsername(String username) {
		threadUsername.set(username);
	}
}
