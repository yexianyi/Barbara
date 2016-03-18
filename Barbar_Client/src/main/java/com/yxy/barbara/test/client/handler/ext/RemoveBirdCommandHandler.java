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
import java.util.Scanner;

import com.yxy.barbara.test.client.handler.abs.BaseCommandHandler;

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
