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
