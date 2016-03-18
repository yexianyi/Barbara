/**
 * Copyright (c) 2016, Xianyi Ye
 *
 * This project includes software developed by Xianyi Ye
 * yexianyi@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.yxy.barbara.test.client.handler.ext;

import java.io.IOException;
import java.net.Socket;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.yxy.barbara.test.client.handler.abs.BaseCommandHandler;
import com.yxy.barbara.test.common.util.JSONUtil;

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
