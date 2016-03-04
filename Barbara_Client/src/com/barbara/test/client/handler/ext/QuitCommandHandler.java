package com.barbara.test.client.handler.ext;

import java.io.IOException;
import java.net.Socket;

import com.barbara.test.client.handler.abs.BaseCommandHandler;

public class QuitCommandHandler extends BaseCommandHandler {

	
	public QuitCommandHandler(Socket socket, String command)
	{
		super(socket,command) ;
	}

	
	
	@Override
	protected String handle(String command) {
		String json = "{\"BARBARA\":" +
				"{" +
		        "   \"MSG TYPE\":\"Quit\"" +
		        "        }" +
		        "}" ;
		
		
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
		
		
		return getResponse(jsonRsp);
	}
	
	
	
	
	
	
	

}
