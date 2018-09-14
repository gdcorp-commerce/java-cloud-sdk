package co.poynt.api.sdk;

public class Constants {
	public static final String SDK_AGENT = "PoyntJavaSDK 1.0";

	public static final String PROP_APP_ID = "appId";
	public static final String PROP_APP_KEY_FILE = "appKeyFile";
	public static final String PROP_API_HOST = "apiHost";
	public static final String PROP_API_SOCKET_TIMEOUT = "httpSocketTimeout";
	public static final String PROP_API_CONNECT_TIMEOUT = "httpConnectTimeout";
	public static final String PROP_API_REQUEST_TIMEOUT = "httpRequestTimeout";
	public static final String PROP_API_MAX_CONNECTION = "httpMaxConnection";
	public static final String PROP_API_MAX_CONN_PER_ROUTE = "httpMaxConnectionPerRoute";

	public static final String POYNT_API_VERSION = "1.2";
	public static final String POYNT_API_HOST = "https://services.poynt.net";
	public static final String API_TOKEN = "/token";
	public static final String API_BUSINESSES = "/businesses";
	public static final String API_BUSINESS_USERS = "/businesses/{businessId}/businessUsers";
	public static final String API_CATALOGS = "/businesses/{businessId}/catalogs";
	public static final String API_PRODUCTS = "/businesses/{businessId}/products";
	public static final String API_TRANSACTIONS = "/businesses/{businessId}/transactions";
	public static final String API_ORDERS = "/businesses/{businessId}/orders";
	public static final String API_CLOUD_MESSAGES = "/cloudMessages";
	public static final String API_WEBHOOKS = "/hooks";
}
