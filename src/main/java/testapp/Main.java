package testapp;

import co.poynt.api.model.*;
import co.poynt.api.sdk.PoyntSdk;

import java.util.*;

/**
 * Created by dennis on 12/19/16.
 */
public class Main {
    public static void main(String[] args) {

        // the merchant needs to authorize the api caller
        // please refer to https://poynt.github.io/developer/tut/integrating-with-poynt-cloud-apis.html
        final String businessId = "24eb9f63-cb3e-4f5c-a0f9-5cf70dc53f62";
        String catalogId = "675f0c80-6db8-4584-a444-6b213d0f4f66";
        String productId = "675f0c80-6db8-4584-a444-6b213d0f4f66";

        // Use as a singleton
        PoyntSdk sdk = PoyntSdk.builder().configure("config.properties").build();

        Business business = sdk.business().get(businessId);
        System.out.println(business);

        List<BusinessUser> users = sdk.businessUser().getAll(businessId);
        System.out.println(users);


        CatalogWithProduct catalog = sdk.catalog().get(businessId, catalogId);
        System.out.println(catalog);

        Product product = sdk.product().get(businessId, productId);
        System.out.println(product);

/*

        // Create a new product

        Product newProduct = new Product();
        newProduct.setId(UUID.randomUUID().toString());
        newProduct.setName("XBOX ONE");
        newProduct.setImageUrl(Collections.singletonList("http://compass.xbox.com/assets/23/0d/230dc52a-8f0e-40bf-bbd1-c51fdb8371e3.png?n=Homepage-360-UA_Upgrade-big_1056x594.png"));
        CurrencyAmount price = new CurrencyAmount();
        price.setAmount(29999l);
        price.setCurrency(Currency.getInstance(Locale.US).getCurrencyCode());
        newProduct.setPrice(price);
        newProduct.setSku(UUID.randomUUID().toString());
        newProduct.setShortCode("XBOX1");
        Product createdProduct = sdk.product().createAtBusiness(newProduct, businessId);

        System.out.println(createdProduct);
*/
    }
}
