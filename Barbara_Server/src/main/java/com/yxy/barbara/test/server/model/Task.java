package com.yxy.barbara.test.server.model;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import com.yxy.barbara.test.server.handler.impl.CancelRequestHandler;
import com.yxy.barbara.test.server.handler.impl.ClientRequestHandler;
import com.yxy.barbara.test.server.handler.inf.ServiceHandler;

public class Task implements Callable{

	private ClientRequest request ;
	
	public Task(ClientRequest request)
	{
		this.request = request ;
	}
	
	@Override
	public Object call() throws Exception 
	{
		
		ServiceHandler handler = new ClientRequestHandler(request) ;
		handler.handle() ;
		
		return "Done";
	}
	
	
	public void cancel()
	{
		ServiceHandler handler = new CancelRequestHandler(request) ;
		handler.handle() ;
	}
	
	
	public <T> RunnableFuture<T> newTask() 
	{
		return new FutureTask<T>(this) 
		{
			public boolean cancel(boolean mayInterruptIfRunning) 
			{
				
				try {
					Task.this.cancel();
				} finally {
					return super.cancel(mayInterruptIfRunning);
				}
			}
		};
		
	}

	


}
