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
