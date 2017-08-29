# Poynt API Java SDK

This is the official Java SDK for making calls to Poynt API in the cloud.

# Building

Maven dependency

```
   <dependency>
      <groupId>co.poynt.api</groupId>
      <artifactId>java-cloud-sdk</artifactId>
      <version>X.X.X</version>
   </dependency>
```

Maven repository

```
<snapshotRepository>
	<uniqueVersion>false</uniqueVersion>
	<id>nexus</id>
	<name>Nexus snapshot repository</name>
	<url>https://nexus.poynt.com/content/repositories/snapshots</url>
</snapshotRepository>
```
		

# Installing

1. Signup at [https://poynt.net](https://poynt.net/auth/signup/developer).
2. Login and go to Development > Cloud apps
3. Create a new cloud app. 
4. Copy the app id and download the key file. 
5. Put the `.pem` key file on your application's classpath.
4. Create a new config file on your application's classpath with the following contents:

```properties
appId=urn:aid:...YOUR_APP_ID...
appKeyFile=classpath:YOUR_APP_KEY_FILE.pem

# Adjust to your liking
httpSocketTimeout=2000
httpConnectTimeout=5000
httpRequestTimeout=30000
httpMaxConnection=10
httpMaxConnectionPerRoute=2
```

__NOTE__: `appKeyFile` value can be an absolute path on disk or a path on the classpath. Include `classpath:` if it is on the classpath.

# Calling the SDK

```java
public class Main {
    public static void main(String[] args) {
        final String businessId = "24eb9f63-cb3e-4f5c-a0f9-5cf70dc53f62";
        // Use as a singleton
        PoyntSdk sdk = PoyntSdk.builder().configure("config.properties").build();

        Business business = sdk.business().get(businessId);
        System.out.println(business);

        List<BusinessUser> users = sdk.businessUser().getAll(businessId);
        System.out.println(users);

        CatalogWithProduct catalog = sdk.catalog().get(businessId, "675f0c80-6db8-4584-a444-6b213d0f4f66");
        System.out.println(catalog);

        Product product = sdk.product().get(businessId, "675f0c80-6db8-4584-a444-6b213d0f4f66");
        System.out.println(product);
	}
}
```
