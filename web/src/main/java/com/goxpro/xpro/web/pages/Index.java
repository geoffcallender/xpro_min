package com.goxpro.xpro.web.pages;

import javax.ejb.EJB;

import com.goxpro.xpro.business.domain.pretend.iface.IPretendFinderServiceLocal;
import com.goxpro.xpro.business.domain.pretend.iface.IPretendManagerServiceLocal;

public class Index {

	@EJB
	private IPretendFinderServiceLocal pretendFinder;

	@EJB
	private IPretendManagerServiceLocal pretendManager;

	void onInvokeAFinderService() {

		System.out.println(">>>> Before calling pretendFinder.findSomething()");
		pretendFinder.findSomething();
		System.out.println(">>>> After calling pretendFinder.findSomething()");

	}

	void onInvokeAManagerService() {

		System.out.println(">>>> Before calling pretendManager.changeSomething()");
		pretendManager.changeSomething();
		System.out.println(">>>> After calling pretendManager.changeSomething()");

	}

}
