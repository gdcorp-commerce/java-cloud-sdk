package co.poynt.api.sdk;

import co.poynt.api.model.CatalogWithProduct;

public class ApiCatalog extends Api {

	public ApiCatalog(PoyntSdk sdk) {
		super(sdk, Constants.POYNT_API_HOST + Constants.API_CATALOGS);
	}

	public CatalogWithProduct get(String businessId, String catalogId) {
		return getFromBusiness(CatalogWithProduct.class, businessId, catalogId);
	}

}
