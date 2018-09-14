package co.poynt.api.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	Properties prop = new Properties();
	String apiHost;

	public void load(String configFile) throws IOException {
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configFile)) {
			this.prop.load(inputStream);
			// 1. Try to get host from system properties first
			apiHost = System.getProperty("apiHost");
			// 2. Then check config.properties
			if (apiHost == null) {
				apiHost = prop.getProperty(Constants.PROP_API_HOST);
			}
			// 3. Then default to what we have in Constants
			if (apiHost == null){
				apiHost = Constants.POYNT_API_HOST;
			}
		}
	}

	public String getAppId() {
		return prop.getProperty(Constants.PROP_APP_ID);
	}

	public String getAppKeyFile() {
		return prop.getProperty(Constants.PROP_APP_KEY_FILE);
	}

	public String getApiHost() {
		return apiHost;
	}

	public int getHttpSocketTimeout() {
		return Integer.valueOf(prop.getProperty(Constants.PROP_API_SOCKET_TIMEOUT));
	}

	public int getHttpConnectTimeout() {
		return Integer.valueOf(prop.getProperty(Constants.PROP_API_CONNECT_TIMEOUT));
	}

	public int getHttpRequestTimeout() {
		return Integer.valueOf(prop.getProperty(Constants.PROP_API_REQUEST_TIMEOUT));
	}

	public int getHttpMaxConnection() {
		return Integer.valueOf(prop.getProperty(Constants.PROP_API_MAX_CONNECTION));
	}

	public int getHttpMaxConnPerRoute() {
		return Integer.valueOf(prop.getProperty(Constants.PROP_API_MAX_CONN_PER_ROUTE));
	}
}
