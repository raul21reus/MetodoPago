package main.java;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

public class BraintreeClient {

	
	public static BraintreeGateway gateway = new BraintreeGateway(
			  Environment.SANDBOX,
			  "jt3jvnbfgd5nmc99",
			  "v52cww75p3jkph29",
			  "7ef7cfa53298080d60265a0eb96f8630"
			);
	
	public static void braintreeProcessing() {
		
	}
	
}
