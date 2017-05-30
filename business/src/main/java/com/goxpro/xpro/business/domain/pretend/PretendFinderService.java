package com.goxpro.xpro.business.domain.pretend;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.goxpro.xpro.business.domain.base.BaseService;
import com.goxpro.xpro.business.domain.pretend.iface.IPretendFinderServiceLocal;
import com.goxpro.xpro.business.domain.pretend.iface.IPretendFinderServiceRemote;

@Stateless
@Local(IPretendFinderServiceLocal.class)
@Remote(IPretendFinderServiceRemote.class)
public class PretendFinderService extends BaseService
		implements IPretendFinderServiceLocal, IPretendFinderServiceRemote {

	@Override
	public void findSomething() {
		System.out.println(">>>> In PretendFinderService.findSomething()");
	}

}
