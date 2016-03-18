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
package com.yxy.barbara.test.server.handler.impl;

import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.yxy.barbara.test.common.util.JSONUtil;
import com.yxy.barbara.test.server.handler.inf.ServiceHandler;
import com.yxy.barbara.test.server.main.ServerMain;
import com.yxy.barbara.test.server.model.ClientRequest;
import com.yxy.barbara.test.server.service.BirdService;
import com.yxy.barbara.test.server.service.SightingService;


public class ClientRequestHandler implements ServiceHandler{

	private ClientRequest request ;
	
	private BirdService bs = BirdService.getInstance() ;
	
	private SightingService ss = SightingService.getInstance() ;
	
	
	public ClientRequestHandler(ClientRequest request)
	{
		this.request = request ;
		
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
					String name = JSONUtil.findAttribute(jsonObj, "NAME") ;
					String color = JSONUtil.findAttribute(jsonObj, "COLOR") ;
					String weight = JSONUtil.findAttribute(jsonObj, "WEIGHT") ;
					String height = JSONUtil.findAttribute(jsonObj, "HEIGHT") ;
					
					response = bs.checkAndAddbird(name, color, Float.valueOf(weight), Float.valueOf(height)) ;
				
			}
			
			else if(command.equalsIgnoreCase("Add Sighting Command"))
			{
					String name = JSONUtil.findAttribute(jsonObj, "NAME") ;
					String location = JSONUtil.findAttribute(jsonObj, "LOCATION") ;
					String time = JSONUtil.findAttribute(jsonObj, "TIME") ;
					
					response = ss.addSighting(name, location, time) ;
				
			}
			
			else if(command.equalsIgnoreCase("Remove Bird Command"))
			{
					String name = JSONUtil.findAttribute(jsonObj, "NAME") ;
					response = bs.checkAndRemovebird(name) ;
				
			}
			
			else if(command.equalsIgnoreCase("Quit"))
			{
				new Thread(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						ServerMain.shutdown() ;
						
						System.exit(0) ;
					}
					
				}.start() ;
				
				
				
				response = "{\"BARBARA\":" +
						"{" +
				        "   \"MSG TYPE\":\"Quit Response\"," +
				        "   \"MSG PARAM\":{" +
				        "                \"STATUS\":\""+"SUCCESS"+"\"," +
				        "                \"ERROR\":\""+"NONE"+"\"" +
				        "                    }" +
				        "        }" +
				        "}"  ;
			}
			
			else if(command.equalsIgnoreCase("List Birds Command"))
			{
				response = bs.listBirds() ;
			}
			
			else if(command.equalsIgnoreCase("List Sightings Command"))
			{
				String nameExp = JSONUtil.findAttribute(jsonObj, "NAME REG EXP") ;
				String start = JSONUtil.findAttribute(jsonObj, "START") ;
				String end = JSONUtil.findAttribute(jsonObj, "END") ;
				
				response = ss.listSightings(nameExp, start, end) ;
				
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
