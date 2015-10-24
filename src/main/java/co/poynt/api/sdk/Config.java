package co.poynt.api.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	Properties prop = new Properties();

	public void load(String configFile) throws IOException {
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configFile)) {
			this.prop.load(inputStream);
		}
	}

	public String getAppId() {
		return prop.getProperty(Constants.PROP_APP_ID);
	}

	public String getAppKeyFile() {
		return prop.getProperty(Constants.PROP_APP_KEY_FILE);
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
