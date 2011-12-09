package com.xingcloud.framework.cache;

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.initiator.Initiator;
import com.xingcloud.framework.initiator.annotation.CloudInitiator;

@CloudInitiator("cacheInitiator")
public class CacheInitiator implements Initiator {

	@Override
	public void init() throws Exception {
		CacheServiceFactory.config(
				XingCloudApplication.getInstance().getParameters()
				);
	}

}
