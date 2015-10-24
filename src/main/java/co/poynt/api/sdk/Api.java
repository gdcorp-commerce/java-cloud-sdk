package co.poynt.api.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import co.poynt.api.model.Code;
import co.poynt.api.model.ErrorInfo;

public abstract class Api {
	protected PoyntSdk sdk;
	protected String endPoint;

	public Api(PoyntSdk sdk, String endPoint) {
		this.sdk = sdk;
		this.endPoint = endPoint;
	}

	public HttpGet createGetRequest(String url) {
		HttpGet get = new HttpGet(url);
		get.setHeader("User-Agent", Constants.SDK_AGENT + ": " + this.sdk.getConfig().getAppId());
		get.setHeader("api-version", Constants.POYNT_API_VERSION);
		return get;
	}

	public HttpPost createPostRequest(String url) {
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", Constants.SDK_AGENT + ": " + this.sdk.getConfig().getAppId());
		post.setHeader("api-version", Constants.POYNT_API_VERSION);
		return post;
	}

	public void handleError(HttpResponse response) {
		try {
			ErrorInfo error = this.readResponse(response, ErrorInfo.class);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				if (error.getCode() == Code.INVALID_ACCESS_TOKEN) {
					this.sdk.clearAccessToken();
				} else if (error.getCode() == Code.INVALID_REFRESH_TOKEN) {
					this.sdk.clearRefreshToken();
				}
			}

			throw new PoyntSdkException(error.getDeveloperMessage(), error);
		} catch (IOException e) {
			throw new PoyntSdkException("Failed to handle error for response.");
		}
	}

	private String readResponse(HttpResponse response) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer sb = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

	public <T> T readResponse(HttpResponse response, Class<T> resourceType) throws IOException {
		String responseContent = readResponse(response);

		T result = (T) this.sdk.getOm().readValue(responseContent, resourceType);
		return result;
	}

	public <T> List<T> readListResponse(HttpResponse response, Class<T> resourceType) throws IOException {
		String responseContent = readResponse(response);

		List<Map<String, Object>> tmp = this.sdk.getOm().readValue(responseContent, List.class);
		List<T> result = new ArrayList<T>(tmp.size());
		for (Map<String, Object> obj : tmp) {
			T item = this.sdk.getOm().convertValue(obj, resourceType);
			result.add(item);
		}

		return result;
	}

	public <T> T get(Class<T> resourceType, String itemId) {
		String accessToken = sdk.getAccessToken();

		String baseUrl = this.endPoint + "/" + itemId;
		HttpGet get = this.createGetRequest(baseUrl);

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		T result = null;
		try {
			HttpResponse response = this.sdk.getHttpClient().execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = (T) this.readResponse(response, resourceType);
			} else {
				handleError(response);
			}
		} catch (IOException e) {
			throw new PoyntSdkException("Failed to get resource.");
		} finally {
			get.releaseConnection();
		}

		return result;
	}

	public <T> T getFromBusiness(Class<T> resourceType, String businessId, String resourceId) {
		String accessToken = sdk.getAccessToken();

		String baseUrl = this.endPoint.replace("{businessId}", businessId) + "/" + resourceId;
		HttpGet get = this.createGetRequest(baseUrl);

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		T result = null;
		try {
			HttpResponse response = this.sdk.getHttpClient().execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = (T) this.readResponse(response, resourceType);
			} else {
				handleError(response);
			}
		} catch (IOException e) {
			throw new PoyntSdkException("Failed to get resource.");
		} finally {
			get.releaseConnection();
		}

		return result;
	}

	public <T> List<T> getAllFromBusiness(Class<T> resourceType, String businessId) {
		List<T> result = null;
		String accessToken = sdk.getAccessToken();

		String baseUrl = this.endPoint.replace("{businessId}", businessId);
		HttpGet get = this.createGetRequest(baseUrl);

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		try {
			HttpResponse response = this.sdk.getHttpClient().execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = this.readListResponse(response, resourceType);
			} else {
				handleError(response);
			}
		} catch (IOException e) {
			throw new PoyntSdkException("Failed to get business");
		} finally {
			get.releaseConnection();
		}

		return result;
	}

	public <T> T create(T resource) {
		String accessToken = sdk.getAccessToken();

		String baseUrl = this.endPoint;
		HttpPost post = this.createPostRequest(baseUrl);

		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		T result = null;
		try {
			String payload = this.sdk.getOm().writeValueAsString(resource);
			StringEntity entity = new StringEntity(payload);
			post.setEntity(entity);

			HttpResponse response = this.sdk.getHttpClient().execute(post);
			int httpCode = response.getStatusLine().getStatusCode();
			if (httpCode == HttpStatus.SC_ACCEPTED || httpCode == HttpStatus.SC_NO_CONTENT) {
				// no result
				result = null;
			} else if (httpCode == HttpStatus.SC_CREATED || httpCode == HttpStatus.SC_OK) {
				result = (T) this.readResponse(response, resource.getClass());
			} else {
				handleError(response);
			}
		} catch (IOException e) {
			throw new PoyntSdkException("Failed to create resource.");
		} finally {
			post.releaseConnection();
		}

		return result;
	}

	public <T> T createAtBusiness(T resource, String businessId) {
		String accessToken = sdk.getAccessToken();

		String baseUrl = this.endPoint.replace("{businessId}", businessId);
		HttpPost post = this.createPostRequest(baseUrl);

		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		T result = null;
		try {
			String payload = this.sdk.getOm().writeValueAsString(resource);
			StringEntity entity = new StringEntity(payload);
			post.setEntity(entity);

			HttpResponse response = this.sdk.getHttpClient().execute(post);
			int httpCode = response.getStatusLine().getStatusCode();
			if (httpCode == HttpStatus.SC_ACCEPTED || httpCode == HttpStatus.SC_NO_CONTENT) {
				result = null;
			} else if (httpCode == HttpStatus.SC_CREATED || httpCode == HttpStatus.SC_OK) {
				result = (T) this.readResponse(response, resource.getClass());
			} else {
				handleError(response);
			}
		} catch (IOException e) {
			throw new PoyntSdkException("Failed to create resource at business.");
		} finally {
			post.releaseConnection();
		}

		return result;
	}
}
