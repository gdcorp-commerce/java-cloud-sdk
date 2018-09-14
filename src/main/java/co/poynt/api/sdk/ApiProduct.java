package co.poynt.api.sdk;

import co.poynt.api.model.Product;

public class ApiProduct extends Api {

	public ApiProduct(PoyntSdk sdk) {
		super(sdk, sdk.getConfig().getApiHost() + Constants.API_PRODUCTS);
	}

	public Product get(String businessId, String productId) {
		return getFromBusiness(Product.class, businessId, productId);
	}
}
