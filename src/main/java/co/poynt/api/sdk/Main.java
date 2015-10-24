package co.poynt.api.sdk;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import co.poynt.api.model.Business;
import co.poynt.api.model.BusinessUser;
import co.poynt.api.model.CatalogWithProduct;
import co.poynt.api.model.Hook;
import co.poynt.api.model.Product;

public class Main {
	public static void main(String[] args) {
		final String businessId = "24eb9f63-cb3e-4f5c-a0f9-5cf70dc53f62";
		PoyntSdk sdk = PoyntSdk.builder().configure("config.properties").build();

		System.out.println("============= BUSINESS");
		Business business = sdk.business().get(businessId);
		System.out.println(business);

		System.out.println("============= BUSINESS USER");
		List<BusinessUser> users = sdk.businessUser().getAll(businessId);
		System.out.println(users);

		System.out.println("=============CATALOG");
		CatalogWithProduct catalog = sdk.catalog().get(businessId, "675f0c80-6db8-4584-a444-6b213d0f4f66");
		System.out.println(catalog);

		System.out.println("=============PRODUCT");
		Product product = sdk.product().get(businessId, "675f0c80-6db8-4584-a444-6b213d0f4f66");
		System.out.println(product);

		System.out.println("=============Webhooks");
		Hook hook = new Hook();
		hook.setApplicationId(sdk.getConfig().getAppId());
		hook.setBusinessId(UUID.fromString(businessId));
		hook.setDeliveryUrl("https://my-webhook-recipient.com/somewhere");
		hook.setEventTypes(Arrays.asList(new String[] { "ORDER_OPENED", "PRODUCT_CREATED" }));

		// Poynt will use the secret below to generate a signature using
		// HmacSHA1 of the webhook payload
		// The signature will be send as an http header called
		// "poynt-webhook-signature".
		hook.setSecret("my-shared-secret-with-poynt");
		sdk.webhook().register(hook);

		System.out.println("=============Done!");
	}
}
