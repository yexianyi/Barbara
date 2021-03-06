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
package com.yxy.barbara.test.client.handler.abs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.yxy.barbara.test.client.exception.BarbaraClientBaseException;
import com.yxy.barbara.test.client.exception.InitCommChannelFailedException;
import com.yxy.barbara.test.client.exception.ReleaseCommChannelFailedException;
import com.yxy.barbara.test.common.util.JSONUtil;

public abstract class BaseCommandHandler {

	protected Socket socket ;
	protected BufferedWriter out ;
	protected BufferedReader in ;
	protected String command ;
	
	
	public BaseCommandHandler(Socket socket, String command)
	{
		this.socket = socket ;
		this.command = command ;
	}
	
	
	/*
	 * init client out/in interface
	 */
	private void init() throws BarbaraClientBaseException
	{
		
		try {
			
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
		
		} catch (IOException e) {
			throw new InitCommChannelFailedException("Failed to initialize Socket Out/In Stream because \n"+e.getMessage()) ;
		}
	}
	

	protected abstract String handle(String command) ;
	
	
	public String execute()
	{
		String response = null ;
		
		try {
			init() ;
			
			response = handle(command) ;
			
			close() ;
		} catch (BarbaraClientBaseException e) {

			response = "Cannot process command due to error: "+e.getMessage() ;
			
		} 
		
		return response ;
	}
	
	
	/*
	 * release socket and close Communication Channel
	 */
	private void close() throws BarbaraClientBaseException
	{
		
		try {
			out.close() ;
			in.close();
			socket.close();
		} catch (IOException e) {
			throw new ReleaseCommChannelFailedException("Failed to release socket because \n"+e.getMessage()) ;
		}
		
		
	}
	
	
	protected String getResponse(String json)
	{
		String response = null ;
		JSONObject jsonObj;
		try {
			
			jsonObj = new JSONObject(json);
			String status = JSONUtil.findAttribute(jsonObj, "STATUS") ;
			
			if(status.equalsIgnoreCase("SUCCESS"))
			{
				response = "Success" ;
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
