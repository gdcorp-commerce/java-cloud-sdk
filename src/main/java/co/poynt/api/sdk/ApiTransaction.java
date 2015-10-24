package co.poynt.api.sdk;

import java.util.List;

import co.poynt.api.model.Transaction;

public class ApiTransaction extends Api {

	public ApiTransaction(PoyntSdk sdk) {
		super(sdk, Constants.POYNT_API_HOST + Constants.API_TRANSACTIONS);
	}

	public Transaction get(String businessId, String transactionId) {
		return this.getFromBusiness(Transaction.class, businessId, transactionId);
	}

	public List<Transaction> getAll(String businessId) {
		throw new RuntimeException("Not yet implemented.");
	}

}
