package com.yxy.barbara.test.server.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.yxy.barbara.test.server.model.Task;

public class BarbaraThreadPool extends ThreadPoolExecutor{


	public BarbaraThreadPool(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) 
	{
		if (callable instanceof Task)
			return ((Task) callable).newTask();
		else
			return super.newTaskFor(callable);
	}
	
	
	@Override
	public List<Runnable> shutdownNow()
	{
		List<Runnable> cancelList = new ArrayList<Runnable>() ;
		super.shutdown(); // + new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!super.awaitTermination(5, TimeUnit.SECONDS)) 
			{
				System.err.println("terminate in mandatory");
				cancelList = super.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!super.awaitTermination(10, TimeUnit.SECONDS))
				{
					System.err.println("Pool did not terminate");
				}
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			super.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
		
		System.out.println("Shutdown ThreadPool successfully.") ;
		
		return cancelList ;
		
	}
	
}
