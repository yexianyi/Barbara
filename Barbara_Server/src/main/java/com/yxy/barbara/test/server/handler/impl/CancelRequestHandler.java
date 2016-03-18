package com.yxy.barbara.test.server.handler.impl;

import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.yxy.barbara.test.common.util.JSONUtil;
import com.yxy.barbara.test.server.handler.inf.ServiceHandler;
import com.yxy.barbara.test.server.model.ClientRequest;


public class CancelRequestHandler implements ServiceHandler{

	private ClientRequest request ;
	private String reason ;
	
	@Deprecated
	public CancelRequestHandler(ClientRequest request, String reason)
	{
		this.request = request ;
		this.reason = reason ;
		
	}
	
	
	public CancelRequestHandler(ClientRequest request) 
	{
		this.request = request ;
		reason = "Reject Request due to Server Cache Queue is full or Server shutdown." ;
	}
	


	@Override
	public void handle() {

		String json = request.getRequest() ;
		
		System.out.println(json) ;
		
		String response = null ;
		
		try {
			JSONObject jsonObj = new JSONObject(json) ;
			
			String command = JSONUtil.findAttribute(jsonObj, "MSG TYPE") ;
			
			
			if(command.equalsIgnoreCase("Add Bird Command"))
			{
				response = "{\"BARBARA\":" +
						"{" +
				        "   \"MSG TYPE\":\"Add Bird Response\"," +
				        "   \"MSG PARAM\":{" +
				        "                \"STATUS\":\""+"FAILED"+"\"," +
				        "                \"ERROR\":\""+reason+"\"" +
				        "                    }" +
				        "        }" +
				        "}"  ;
				
			}
			
			else if(command.equalsIgnoreCase("Add Sighting Command"))
			{
				
				response = "{\"BARBARA\":" +
						"{" +
				        "   \"MSG TYPE\":\"Add Sighting Response\"," +
				        "   \"MSG PARAM\":{" +
				        "                \"STATUS\":\""+"FAILED"+"\"," +
				        "                \"ERROR\":\""+reason+"\"" +
				        "                    }" +
				        "        }" +
				        "}"  ;
				
			}
			
			else if(command.equalsIgnoreCase("Remove Bird Command"))
			{
				response = "{\"BARBARA\":" +
						"{" +
				        "   \"MSG TYPE\":\"Remove Bird Response\"," +
				        "   \"MSG PARAM\":{" +
				        "                \"STATUS\":\""+"FAILED"+"\"," +
				        "                \"ERROR\":\""+reason+"\"" +
				        "                    }" +
				        "        }" +
				        "}"  ;
				
			}
			
			else if(command.equalsIgnoreCase("Quit"))
			{
				
				response = "{\"BARBARA\":" +
						"{" +
				        "   \"MSG TYPE\":\"Quit Response\"," +
				        "   \"MSG PARAM\":{" +
				        "                \"STATUS\":\""+"FAILED"+"\"," +
				        "                \"ERROR\":\""+reason+"\"" +
				        "                    }" +
				        "        }" +
				        "}"  ;
			}
			
			else if(command.equalsIgnoreCase("List Birds Command"))
			{
				response = "{\"BARBARA\":" +
						"{" +
				        "   \"MSG TYPE\":\"List Birds Response\"," +
				        "   \"MSG PARAM\":{" +
				        "                \"STATUS\":\""+"FAILED"+"\"," +
				        "                \"ERROR\":\""+reason+"\"" +
				        "                    }" +
				        "        }" +
				        "}"  ;
			}
			
			else if(command.equalsIgnoreCase("List Sightings Command"))
			{
				response = "{\"BARBARA\":" +
						"{" +
				        "   \"MSG TYPE\":\"List Sightings Response\"," +
				        "   \"MSG PARAM\":{" +
				        "                \"STATUS\":\""+"FAILED"+"\"," +
				        "                \"ERROR\":\""+reason+"\"" +
				        "                    }" +
				        "        }" +
				        "}"  ;
				
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		
		} finally {
			
			try {
				
				request.getWriter().write(response) ;
				request.getWriter().newLine() ;
				request.getWriter().flush() ;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(response) ;
			
		}
		
		
		
	}

}
