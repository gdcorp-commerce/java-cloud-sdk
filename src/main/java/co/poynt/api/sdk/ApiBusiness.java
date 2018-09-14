package co.poynt.api.sdk;

import co.poynt.api.model.Business;

public class ApiBusiness extends Api {

	public ApiBusiness(PoyntSdk sdk) {
		super(sdk, sdk.getConfig().getApiHost() + Constants.API_BUSINESSES);
	}

	public Business get(String businessId) {
		return this.get(Business.class, businessId);
	}
}
