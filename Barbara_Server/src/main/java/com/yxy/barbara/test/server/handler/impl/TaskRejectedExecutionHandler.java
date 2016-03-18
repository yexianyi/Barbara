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
package com.yxy.barbara.test.server.handler.impl;

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