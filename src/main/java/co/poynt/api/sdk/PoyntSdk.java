package co.poynt.api.sdk;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import co.poynt.api.model.TokenResponse;

public class PoyntSdk implements Closeable {
	private Config config;
	private ObjectMapper om;

	private JsonWebToken jsonWebToken;
	private CloseableHttpClient httpClient;

	private String accessToken;
	private String refreshToken;

	private PoyntSdk() {
		// private on purpose
	}

	@Override
	public void close() {
		if (httpClient != null) {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				// ignore
			}
		}
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public void jsonWebToken(JsonWebToken jwt) {
		this.jsonWebToken = jwt;
	}

	public void setHttpClient(CloseableHttpClient client) {
		this.httpClient = client;
	}

	public void setObjectMapper(ObjectMapper om) {
		this.om = om;
	}

	public static class Builder {
		Config cfg;
		JsonWebToken jwt;
		CloseableHttpClient client;
		ObjectMapper om;

		public Builder config(Config cfg) {
			this.cfg = cfg;
			return this;
		}

		public Builder jwtGenerator(JsonWebToken jwtGen) {
			this.jwt = jwtGen;
			return this;
		}

		public Builder httpClient(CloseableHttpClient client) {
			this.client = client;
			return this;
		}

		public Builder jackson(ObjectMapper om) {
			this.om = om;
			return this;
		}

		public Builder configure(String configFile) throws PoyntSdkException {

			this.cfg = new Config();
			try {
				cfg.load(configFile);
			} catch (IOException e) {
				throw new PoyntSdkException("Failed to load configuration");
			}

			this.jwt = new JsonWebToken(cfg);
			try {
				jwt.init();
			} catch (IOException e) {
				throw new PoyntSdkException("Failed to load keys for JWT generator.");
			}

			RequestConfig httpCfg = RequestConfig.custom().setSocketTimeout(cfg.getHttpSocketTimeout())
					.setConnectTimeout(cfg.getHttpConnectTimeout())
					.setConnectionRequestTimeout(cfg.getHttpRequestTimeout()).build();

			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(cfg.getHttpMaxConnection());
			cm.setDefaultMaxPerRoute(cfg.getHttpMaxConnPerRoute());
			this.client = HttpClientBuilder.create().setDefaultRequestConfig(httpCfg).setConnectionManager(cm).build();

			this.om = new ObjectMapper();
			this.om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

			this.om.setDateFormat(new ISO8601DateFormat());
			this.om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			this.om.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
			this.om.setSerializationInclusion(JsonInclude.Include.NON_NULL);

			return this;
		}

		public PoyntSdk build() {
			PoyntSdk sdk = new PoyntSdk();
			sdk.setConfig(cfg);
			sdk.setHttpClient(client);
			sdk.jsonWebToken(jwt);
			sdk.setObjectMapper(this.om);
			return sdk;
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public void clearAccessToken() {
		this.accessToken = null;
	}

	public void clearRefreshToken() {
		this.accessToken = null;
		this.refreshToken = null;
	}

	public String getAccessToken() {
		if (this.accessToken != null) {
			return this.accessToken;
		}
		HttpPost post = new HttpPost(Constants.POYNT_API_HOST + Constants.API_TOKEN);
		post.setHeader("User-Agent", Constants.SDK_AGENT + ": " + this.config.getAppId());
		post.setHeader("api-version", Constants.POYNT_API_VERSION);
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

		if (this.refreshToken == null) {
			String selfIssuedJwt = this.jsonWebToken.selfIssue();
			urlParameters.add(new BasicNameValuePair("grantType", "urn:ietf:params:oauth:grant-type:jwt-bearer"));
			urlParameters.add(new BasicNameValuePair("assertion", selfIssuedJwt));
		} else {
			urlParameters.add(new BasicNameValuePair("grantType", "REFRESH_TOKEN"));
			urlParameters.add(new BasicNameValuePair("refreshToken", this.refreshToken));
		}

		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}

				TokenResponse tokenResponse = om.readValue(result.toString(), TokenResponse.class);
				this.accessToken = tokenResponse.getAccessToken();
				this.refreshToken = tokenResponse.getRefreshToken();
				return this.accessToken;
			} else {
				throw new PoyntSdkException(
						response.getStatusLine().getStatusCode() + ": " + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw new PoyntSdkException("Failed to obtain access token.");
		} finally {
			post.releaseConnection();
		}
	}

	public Config getConfig() {
		return config;
	}

	public ObjectMapper getOm() {
		return om;
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public ApiBusiness business() {
		return new ApiBusiness(this);
	}

	public ApiBusinessUser businessUser() {
		return new ApiBusinessUser(this);
	}

	public ApiCatalog catalog() {
		return new ApiCatalog(this);
	}

	public ApiProduct product() {
		return new ApiProduct(this);
	}

	public ApiTransaction transaction() {
		return new ApiTransaction(this);
	}

	public ApiOrder order() {
		return new ApiOrder(this);
	}
}
