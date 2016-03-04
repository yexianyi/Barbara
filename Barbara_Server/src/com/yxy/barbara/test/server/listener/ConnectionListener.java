package com.yxy.barbara.test.server.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.yxy.barbara.test.server.Server;

public class ConnectionListener extends Thread{

	private int port ;
	private final List<Server> thList = new ArrayList<Server>() ;
	private Socket socket = null ;
	private ServerSocket ss = null; 
	private ThreadPoolExecutor pool = null ;
	
	public ConnectionListener(int port, ThreadPoolExecutor pool)
	{
		this.port = port ;
		this.pool = pool ;
	}
	
	@Override
	public void run() {
		super.run();
		
		try {
			// init Server Socket
			ss = new ServerSocket(port);

			while(!Thread.currentThread().isInterrupted()) 
			{
				//wait new connection request
				socket = ss.accept();

				// Received a client connection request.
				Server server = new Server(socket,pool);
				server.start();
				
				thList.add(server) ;
				
				try {
					Thread.sleep(300);
				} catch (InterruptedException ignored) {
					//allow thread to exit. 
					break ;
				}
			}//end while
				

		} catch (IOException e){
			//allow thread to exit
			
		} finally {
			
			System.out.println("Shutdown Connection Listener successfully.");
		}
	}

	@Override
	public void interrupt() {
		try {
			ss.close();
		} catch (IOException ignored) {
			
		} finally {
			super.interrupt();
		}
	}
	
	
	public void shutdown()
	{
		
		//2.terminate all existing services
		for(Thread th:thList)
		{
			if(th.isInterrupted()==false)
			{
				th.interrupt() ;
			}
		}
    	
		for(Thread th:thList)
		{
			while(th.isAlive())
			{
				try {
					Thread.sleep(300) ;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if(Thread.currentThread().isInterrupted()==false)
		{
			interrupt() ;
		}
		
		while(isAlive())
    	{
			try {
				Thread.sleep(300) ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
	
}
