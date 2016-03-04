package com.barbara.test.client.handler.ext;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.barbara.test.client.handler.abs.BaseCommandHandler;

public class AddBirdCommandHandler extends BaseCommandHandler {

	
	public AddBirdCommandHandler(Socket socket, String command)
	{
		super(socket,command) ;
	}

	
	
	@Override
	protected String handle(String command) {


		String name = null;
		String color = null;
		String weight = null;
		String height = null;
		
		Scanner input = new Scanner(System.in);
		System.out.print("server@localhost>Please input Bird Name:");
		name = input.nextLine();
		
		System.out.println() ;
		
		System.out.print("server@localhost>Please input Bird Color:");
		color = input.nextLine();
		
		System.out.println() ;
		
		System.out.print("server@localhost>Please input Bird Weight:");
		weight = input.nextLine();
		
		System.out.println() ;
		
		System.out.print("server@localhost>Please input Bird Height:");
		height = input.nextLine();
    	
		String json = "{\"BARBARA\":" +
				"{" +
		        "   \"MSG TYPE\":\"Add Bird Command\"," +
		        "   \"MSG PARAM\":{" +
		        "                \"NAME\":\""+name+"\"," +
		        "                \"COLOR\":\""+color+"\"," +
		        "                \"WEIGHT\":\""+weight+"\"," +
		        "                \"HEIGHT\":\""+height+"\"" +
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
		
		String response = getResponse(jsonRsp) ;
		if(response.equalsIgnoreCase("SUCCESS"))
		{
			response = "Bird "+name+" successfully added to the database" ;
		}
		
		return response ;
	}


	

}
