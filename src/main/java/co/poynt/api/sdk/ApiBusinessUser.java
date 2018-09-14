package co.poynt.api.sdk;

import java.util.List;

import co.poynt.api.model.BusinessUser;

public class ApiBusinessUser extends Api {

	public ApiBusinessUser(PoyntSdk sdk) {
		super(sdk, sdk.getConfig().getApiHost() + Constants.API_BUSINESS_USERS);
	}

	public List<BusinessUser> getAll(String businessId) {
		return this.getAllFromBusiness(BusinessUser.class, businessId);
	}
}
