package com.barbara.test.server.handler.impl;

import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskRejectedExecutionHandler implements RejectedExecutionHandler {
    public TaskRejectedExecutionHandler() {
        super();
    }

    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor pool) 
    {

        System.out.println("Reject request, because Cache Queue is full.");
        if(runnable instanceof FutureTask)
        {
        	FutureTask task = (FutureTask) runnable ;
        	task.cancel(true) ;
        }
        	
        
    }
}