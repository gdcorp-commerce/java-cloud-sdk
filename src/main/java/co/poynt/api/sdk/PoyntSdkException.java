package co.poynt.api.sdk;

import co.poynt.api.model.ErrorInfo;

public class PoyntSdkException extends RuntimeException {
	private static final long serialVersionUID = 308037402406785379L;
	private ErrorInfo apiErrorInfo;

	public PoyntSdkException(String msg, ErrorInfo apiErrorInfo) {
		super(msg);
		this.apiErrorInfo = apiErrorInfo;
	}

	public PoyntSdkException(String msg) {
		super(msg);
	}

	public ErrorInfo getApiErrorInfo() {
		return apiErrorInfo;
	}

	public void setApiErrorInfo(ErrorInfo apiErrorInfo) {
		this.apiErrorInfo = apiErrorInfo;
	}
}
