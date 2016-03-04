package com.barbara.test.client.handler.ext;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.barbara.test.client.handler.abs.BaseCommandHandler;

public class AddSightingCommandHandler extends BaseCommandHandler {

	
	public AddSightingCommandHandler(Socket socket, String command)
	{
		super(socket,command) ;
	}

	
	
	@Override
	protected String handle(String command) {
		
		String name = null;
		String location = null;
		String date = null;
		
		Scanner input = new Scanner(System.in);
		System.out.print("server@localhost>Please input Sighting Name:");
		name = input.nextLine();
		
		System.out.println() ;
		
		System.out.print("server@localhost>Please input Sighting Location:");
		location = input.nextLine();
		
		System.out.println() ;
		
		System.out.print("server@localhost>Please input Date&Time (yy/MM/dd HH:mm):");
		date = input.nextLine();
		
		System.out.println() ;
		
    	
		String json = "{\"BARBARA\":" +
				"{" +
		        "   \"MSG TYPE\":\"Add Sighting Command\"," +
		        "   \"MSG PARAM\":{" +
		        "                \"NAME\":\""+name+"\"," +
		        "                \"LOCATION\":\""+location+"\"," +
		        "                \"TIME\":\""+date+"\"" +
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
