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
package com.yxy.barbara.test.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import com.yxy.barbara.test.server.main.ServerMain;
import com.yxy.barbara.test.server.model.ClientRequest;
import com.yxy.barbara.test.server.model.Task;


public class Server extends Thread {

	private static AtomicInteger clientCount = new AtomicInteger(0) ;
	
	private ThreadPoolExecutor pool ;
	
	private Socket socket;
	
	public Server(Socket ss, ThreadPoolExecutor pool) {
		this.socket = ss;
		this.pool = pool ;
	}


	//receiving message
	public void run() {
		
		BufferedReader in = null ;
		BufferedWriter out = null ;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			//capture client request
			while (!Thread.currentThread().isInterrupted()) 
			{
				//limit the number of max connection could not be greater than 100. 
				if(clientCount.intValue()>100)
				{
					rejectRequest(out) ;
					break ;
				}
				else
				{
					clientCount.incrementAndGet() ;
				}
				
				
				String request = null ;
				try
				{
					request = in.readLine() ;
					
				} catch (SocketException e){
					//allow thread to exit
					break ;
				}
				
				if( request != null && request.length()>=0 && !ServerMain.getStatus().equalsIgnoreCase("SHUTDOWN"))
                {
					ClientRequest cr = new ClientRequest(out,request.trim()) ;
					Future result = pool.submit(new Task(cr)) ;
					try {
						result.get() ;
					} catch (ExecutionException ignored) {
						//allow to exit
					}
                    
                }
				else if(ServerMain.getStatus().equalsIgnoreCase("SHUTDOWN"))
				{
					//reject new request, because of shutdown
					while(in.readLine()!=null)
					{
						out.write("Reject Request since server is shutdown.") ;
						out.newLine() ;
						out.flush() ;
					}
					
				}
                else
                {
                    try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						//allow to exit
						break ;
					}
                }
			}//end while
			

			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ignored) {
			//allow to exit
		} finally {
			
			// close connection
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			clientCount.decrementAndGet();
			
		}
		
	}

	private void rejectRequest(BufferedWriter out) throws IOException 
	{
		out.write("Reject Request since already reach maximal connection limit.") ;
		out.newLine() ;
		out.flush() ;
		
	}


	public void interrupt() {
		try {
			socket.close();
		} catch (IOException ignored) {
			
		} finally {
			super.interrupt();
		}
	}
    
    
    
}
