package com.barbara.test.client.handler.ext;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.barbara.test.client.handler.abs.BaseCommandHandler;

public class RemoveBirdCommandHandler extends BaseCommandHandler {

	
	public RemoveBirdCommandHandler(Socket socket, String command)
	{
		super(socket,command) ;
	}

	
	
	@Override
	protected String handle(String command) {
		String name = null;
		
		Scanner input = new Scanner(System.in);
		System.out.print("server@localhost>Please input Bird Name:");
		name = input.nextLine();
		
		System.out.println() ;
		
		
		String json = "{\"BARBARA\":" +
				"{" +
		        "   \"MSG TYPE\":\"Remove Bird Command\"," +
		        "   \"MSG PARAM\":{" +
		        "                \"NAME\":\""+name+"\"" +
		        "                    }" +
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
		
		
		
		return getResponse(jsonRsp) ;
	}
	
	
	
	
	
	
	

}
