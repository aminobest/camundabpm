package org.camunda.bpm.getstarted.call;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Map;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@Stateless
@Named
public class ServiceInvoker {
	 @PersistenceContext
	  private EntityManager entityManager;
	  // Inject task form available through the camunda cdi artifact
	  @Inject
	  private TaskForm taskForm;
	  public void invokeservice(DelegateExecution delegateExecution) {
	
	
		  // Get all process variables
		    Map<String, Object> variables = delegateExecution.getVariables();
	    
		    String ICAOAirportCode = variables.get("city").toString();

		   String outputData = retrieveCity(ICAOAirportCode);
		    // Get all process variables
		
		    // Set order attributes
		   delegateExecution.setVariable("output",outputData);


		  }

	  
	private String retrieveCity(String ICAOAirportCode) {
		String returnvalue = "";
		try {
		 
	
		  URL url = new URL("https://avwx.rest/api/metar.php?station="+ICAOAirportCode+"&format=JSON");
		  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		  conn.setRequestMethod("GET");
		  conn.setRequestProperty("Accept", "application/json");

		  if (conn.getResponseCode() != 200) {
		  throw new RuntimeException("Failed : HTTP error code : "
		  + conn.getResponseCode());
		  }

		  BufferedReader br = new BufferedReader(new InputStreamReader(
		  (conn.getInputStream())));

		  String output;
		 
		  System.out.println("Output from Server .... \n");
		  while ((output = br.readLine()) != null) {
		    returnvalue += output +"\n";
		  }

		  conn.disconnect();
		  } catch (Exception e) {

		  e.printStackTrace();

		 }
		
		return returnvalue ;
	}
	  
	 
	
}
