package co.poynt.api.sdk;

import co.poynt.api.model.Order;

public class ApiOrder extends Api {

	public ApiOrder(PoyntSdk sdk) {
		super(sdk, sdk.getConfig().getApiHost() + Constants.API_ORDERS);
	}

	public Order get(String businessId, String orderId) {
		return this.getFromBusiness(Order.class, businessId, orderId);
	}
}
