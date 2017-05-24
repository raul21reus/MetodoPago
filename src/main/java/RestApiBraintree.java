package main.java;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Request;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class RestApiBraintree implements Filter { 

    public static void main(String[] args) {
        SpringApplication.run(RestApiBraintree.class, args);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
 
    public @Bean
    BraintreeGateway braintreeGateway(){
    	return BraintreeClient.gateway;
    }
    
   /* @RequestMapping(value="/client_token/{clientId}", method= RequestMethod.GET)
    public Object getTokenById(@PathVariable("clientId") String todoId) {
    	ClientTokenRequest ctr = new  ClientTokenRequest().customerId(todoId);
        return  braintreeGateway().clientToken().generate(ctr);
    }*/
    
    
    @RequestMapping(value="/client_token/", method= RequestMethod.GET)
    public Object getToken() {
        return  braintreeGateway().clientToken().generate();
    }
    
    @ResponseBody
    @RequestMapping(value="/checkout/", method= RequestMethod.POST)
    public ResponseEntity<String> processNonce(HttpServletRequest request1, HttpServletResponse response)  {
    	//Request request;
    	
    	String payment_method_nonce = request1.getParameter("payment_method_nonce");
     	System.out.println("Paso 4 Nonce: " + payment_method_nonce );
     	//return nonceFromTheClient;
     	
     	TransactionRequest request = new TransactionRequest()
     		    .amount(new BigDecimal("10.00"))
     		    .paymentMethodNonce(payment_method_nonce)
     		    .options()
     		      .submitForSettlement(true)
     		      .done();

     	Result<Transaction> result = braintreeGateway().transaction().sale(request);
     	if(result.isSuccess())
     	{
     		System.out.println("Pago terminado");
     		response.setStatus(HttpServletResponse.SC_ACCEPTED);
     		//ResponseEntity<Boolean> respuesta = new ResponseEntity<Boolean>(HttpStatus.OK)
     		
     		return new ResponseEntity<String>("Pago correcto", HttpStatus.OK);
     		//return new ResponseEntity<Boolean>(uiRequestProcessor.saveData(a),HttpStatus.OK);
     	}else{
     		System.err.println("Algo fallo");
     		System.err.println(result.toString());
     		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
     		return new ResponseEntity<String>("Pago fracaso", HttpStatus.INTERNAL_SERVER_ERROR);
     	}
    }
    
    /*
    
    @ResponseBody
    @RequestMapping(value="/checkout/", method= RequestMethod.POST)
    public String processNonce(@RequestBody String payment_method_nonce)  {
    	//Request request;
    	
     //String nonceFromTheClient = request.queryParams("payment_method_nonce");
     	System.out.println("Paso 4 Nonce: " + payment_method_nonce );
     	//return nonceFromTheClient;
     	
     	TransactionRequest request = new TransactionRequest()
     		    .amount(new BigDecimal("10.00"))
     		    .paymentMethodNonce(payment_method_nonce)
     		    .options()
     		      .submitForSettlement(true)
     		      .done();

     	Result<Transaction> result = braintreeGateway().transaction().sale(request);
     	if(result.isSuccess())
     	{
     		System.out.println("Pago terminado");
     	}else{
     		System.err.println("Algo fallo");
     		System.err.println(result.toString());
     	}
     	
     	return result.getMessage();
    }
    */
    
    /*
    @RequestMapping(value="/checkout/", method= RequestMethod.POST)
    public String processNonce(@RequestParam(value="clientNonce") String clientNonce)  {
    	//Request request;
    	
     //String nonceFromTheClient = request.queryParams("payment_method_nonce");
     	System.out.println("Paso 4 Nonce: " + clientNonce );
     	//return nonceFromTheClient;
     	
     	TransactionRequest request = new TransactionRequest()
     		    .amount(new BigDecimal("10.00"))
     		    .paymentMethodNonce(clientNonce)
     		    .options()
     		      .submitForSettlement(true)
     		      .done();

     	Result<Transaction> result = braintreeGateway().transaction().sale(request);
     	if(result.isSuccess())
     	{
     		System.out.println("Pago terminado");
     	}else{
     		System.err.println("Algo fallo");
     		System.err.println(result.toString());
     	}
     	
     	return result.getMessage();
    }
    */
}
