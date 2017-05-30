package com.goxpro.xpro.business.domain.pretend;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import com.goxpro.xpro.business.domain.base.BaseService;
import com.goxpro.xpro.business.domain.base.UsernameCacheRefresher;
import com.goxpro.xpro.business.domain.pretend.iface.IPretendManagerServiceLocal;
import com.goxpro.xpro.business.domain.pretend.iface.IPretendManagerServiceRemote;

@Stateless
@Local(IPretendManagerServiceLocal.class)
@Remote(IPretendManagerServiceRemote.class)
@Interceptors({ UsernameCacheRefresher.class })
public class PretendManagerService extends BaseService
		implements IPretendManagerServiceLocal, IPretendManagerServiceRemote {

	@Override
	public void changeSomething() {
		System.out.println(">>>> In PretendManagerService.changeSomething()");
	}

}
