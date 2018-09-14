package co.poynt.api.sdk;

import co.poynt.api.model.CloudMessage;

public class ApiNotification extends Api {

	public ApiNotification(PoyntSdk sdk) {
		super(sdk, sdk.getConfig().getApiHost() + Constants.API_CLOUD_MESSAGES);
	}

	public void send(CloudMessage msg) {
		this.create(msg);
	}
}
