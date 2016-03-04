package com.yxy.barbara.test.client.dispatcher.impl;

import java.net.Socket;

import com.yxy.barbara.test.client.exception.UnRecognizedCommandException;
import com.yxy.barbara.test.client.handler.abs.BaseCommandHandler;
import com.yxy.barbara.test.client.handler.ext.AddBirdCommandHandler;
import com.yxy.barbara.test.client.handler.ext.AddSightingCommandHandler;
import com.yxy.barbara.test.client.handler.ext.ListBirdsCommandHandler;
import com.yxy.barbara.test.client.handler.ext.ListSightingsCommandHandler;
import com.yxy.barbara.test.client.handler.ext.QuitCommandHandler;
import com.yxy.barbara.test.client.handler.ext.RemoveBirdCommandHandler;
import com.yxy.barbara.test.client.handler.inf.IDispatcher;

public class CommandDispatcher implements IDispatcher {

	private Socket socket ;
	private String command ;
	
	public CommandDispatcher(Socket socket, String command)
	{
		this.socket = socket ;
		this.command = command ;
		
	}
	
	@Override
	public String dispatch() {

		String response = null ;
		BaseCommandHandler handler = null ;
		
		if(command.equalsIgnoreCase("addbird"))
		{
			handler = new AddBirdCommandHandler(socket,command) ;
			response = handler.execute() ;
		}
		
		else if(command.equalsIgnoreCase("addsighting"))
		{
			handler = new AddSightingCommandHandler(socket,command) ;
			response = handler.execute() ;
		}
		
		else if(command.equalsIgnoreCase("listbirds"))
		{
			handler = new ListBirdsCommandHandler(socket,command) ;
			response = handler.execute() ;
		}
		
		else if(command.equalsIgnoreCase("listsightings"))
		{
			handler = new ListSightingsCommandHandler(socket,command) ;
			response = handler.execute() ;
		}
		
		else if(command.equalsIgnoreCase("remove"))
		{
			handler = new RemoveBirdCommandHandler(socket,command) ;
			response = handler.execute() ;
		}
		
		else if(command.equalsIgnoreCase("quit"))
		{
			handler = new QuitCommandHandler(socket,command) ;
			response = handler.execute() ;
			
			if(response.equalsIgnoreCase("SUCCESS"))
			{
				System.out.println("Server has been shutdown successfully.") ;
			}
			else
			{
				System.err.println("Server shutting down is failed, due to "+ response) ;
			}
		}
		
		else
		{
			try {
				throw new UnRecognizedCommandException("Invalid Command:"+command) ;
			} catch (UnRecognizedCommandException e) {
				response = e.getMessage() ;
			}
		}
		
		return response;
	}

}
