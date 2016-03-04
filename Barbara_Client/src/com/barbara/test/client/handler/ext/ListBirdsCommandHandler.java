package com.barbara.test.client.handler.ext;

import java.io.IOException;
import java.net.Socket;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.barbara.test.client.handler.abs.BaseCommandHandler;
import com.barbara.test.common.util.JSONUtil;

public class ListBirdsCommandHandler extends BaseCommandHandler {

	
	public ListBirdsCommandHandler(Socket socket, String command)
	{
		super(socket,command) ;
	}

	
	
	@Override
	protected String handle(String command) {


		String json = "{\"BARBARA\":" +
							"{" +
					        "   \"MSG TYPE\":\"List Birds Command\"" +
					        "}"+ 
					  "}";
		
		
		try {
			
			out.write(json) ;
			out.newLine() ;
			out.flush() ;
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String jsonRsp = null ;
		
		try {
			jsonRsp = in.readLine() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return getResponse(jsonRsp) ;
	}



	@Override
	protected String getResponse(String json) {
		
		String response = null ;
		JSONObject jsonObj;
		try {
			
			jsonObj = new JSONObject(json);
			String status = JSONUtil.findAttribute(jsonObj, "STATUS") ;
			
			if(status.equalsIgnoreCase("SUCCESS"))
			{
				JSONArray array = JSONUtil.findArray(jsonObj, "BIRDS") ;
				
		
				response = "NAME	" + "COLOR	" + "WEIGHT	" + "HEIGHT		" + "\n" ;
				response += "------------------------------------------------------------------" + "\n" ;
				
				for(int i=0; i<array.length() ; i++)
				{
					JSONObject bird = array.getJSONObject(i) ;
					
					String name = JSONUtil.findAttribute(bird, "NAME") ;
					String color = JSONUtil.findAttribute(bird, "COLOR") ;
					String weight = JSONUtil.findAttribute(bird, "WEIGHT") ;
					String height = JSONUtil.findAttribute(bird, "HEIGHT") ;
					
					response += name + "	" + color + "	"+ weight + "	"+height + "\n" ;
					
				}
				
				response += "------------------------------------------------------------------" + "\n" ;
				response += "Total: " + array.length() ;
				
			}
			else
			{
				response = "Failure: "+JSONUtil.findAttribute(jsonObj, "ERROR") ;
			}
			
		} catch (JSONException e) {
			
			response = "Cannot parse server response." ;
		
		}
		
		
		return response ;
	}
	
	
	
	
	
	
	
	

}
