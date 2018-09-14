package co.poynt.api.sdk;

import co.poynt.api.model.Hook;

public class ApiWebhook extends Api {

	public ApiWebhook(PoyntSdk sdk) {
		super(sdk, sdk.getConfig().getApiHost() + Constants.API_WEBHOOKS);
	}

	public Hook get(String hookId) {
		return this.get(Hook.class, hookId);
	}

	public Hook register(Hook hook) {
		return this.create(hook);
	}
}
