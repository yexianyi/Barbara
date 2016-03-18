package com.yxy.barbara.test.server.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

import com.yxy.barbara.test.common.util.FileUtil;
import com.yxy.barbara.test.server.exception.BarbaraTestBaseException;
import com.yxy.barbara.test.server.exception.FileCreationFailedException;
import com.yxy.barbara.test.server.exception.InsufficientPermissionException;
import com.yxy.barbara.test.server.exception.InvalidParamException;
import com.yxy.barbara.test.server.global.Configuration;
import com.yxy.barbara.test.server.handler.impl.TaskRejectedExecutionHandler;
import com.yxy.barbara.test.server.listener.ConnectionListener;
import com.yxy.barbara.test.server.pool.BarbaraThreadPool;
import com.yxy.barbara.test.server.writer.DataStoreWriter;


public class ServerMain {

	private static BarbaraThreadPool pool = null ;
	private static BlockingQueue<Runnable> worksQueue = new ArrayBlockingQueue<Runnable>(1000,true);
	private static RejectedExecutionHandler rejectHandler = new TaskRejectedExecutionHandler();
	
	
	private static final CountDownLatch startGate = new CountDownLatch(1) ; 
    private static final List<Runnable> startUpTaskList = new ArrayList<Runnable>() ;
	
	private static ConnectionListener listener = null ;
	private static DataStoreWriter writer = null ;
	
	private static Integer port ;
	private static String data ;
	private static Integer proc_count ;
	
	private static volatile String status = "" ;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		args = new String[]{"-port", "3008", "-data", "f:\\data", "-proc_count", "3"} ;
		
		Map params = new HashMap() ;
		
		for(int i=0; i<args.length ; i++)
		{
			params.put(args[i], args[i+1]) ;
			i++ ;
		}
		
		
		if(args.length>6)
		{
			System.out.println("ERROR:Invalid Command");
			System.out.println("Please follow format: java -jar <Jar File Name> <Server IP> <Port> <Command>");
			System.out.println("Example:");
			System.out.println("	java -jar BarbaraTestServer.jar -port 3008 -data c:\\data -proc_count 3");
			
			System.exit(0) ;
		}
		
		String port = (String) params.get("-port") ;
		String data = (String) params.get("-data") ;
		String proc_count = (String) params.get("-proc_count") ;
		
		try {
			init(port, data, proc_count) ;
			startup() ;
		} catch (BarbaraTestBaseException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void init(String port, String data, String proc_count) throws BarbaraTestBaseException 
	{
		
		// Init Port
		if (port == null) {
			
			ServerMain.port = new Integer(3000);
		}
		else
		{
			Integer p ;
			
			try{
				p = Integer.valueOf(port) ;
			} catch(NumberFormatException e){
				throw new InvalidParamException("Port number is invalid.") ;
			}
			
			if (p.intValue() >= 1 && p.intValue() <= 65535) 
			{
				ServerMain.port = p ;
			}
			else
			{
				throw new InvalidParamException("Port number is invalid.") ;
				
			}
		}
		
		
		//init data Store location
		if (data == null) //create "serverdata" folder in user's home dir.
		{
			String home_dir = System.getProperty("user.home");
			ServerMain.data = home_dir+File.separator+"serverdata" ;
			checkAndCreateDataStore(ServerMain.data) ;
			
		}
		else //create "serverdata" folder in user specified dir.
		{
			ServerMain.data = data+File.separator+"serverdata" ;
			checkAndCreateDataStore(ServerMain.data) ;
			
		}
		
		
		//init proc_count
		
		Integer proc ;
		if(proc_count==null)
		{
			ServerMain.proc_count = 2 ;
		}
		else
		{
			try{
				proc = Integer.valueOf(proc_count) ;
			} catch(NumberFormatException e){
				throw new InvalidParamException("proc_count is invalid.") ;
			}
			
			if(proc>=1)
			{
				ServerMain.proc_count = proc ;
			}
			else
			{
				throw new InvalidParamException("proc_count could not be <= ZERO.") ;
				
			}
		}
		
		System.out.println("Port:"+ServerMain.port) ;
		System.out.println("Data Store Files have been created on "+ServerMain.data) ;
		System.out.println("Proc_count:"+ServerMain.proc_count) ;
		
		
		//init thread pool
		pool = new BarbaraThreadPool(1,
				ServerMain.proc_count,
                10,
                TimeUnit.SECONDS,
                worksQueue,
                rejectHandler) ;
		
	}
	
	public static void startup() {

		//init datastore writer thread
		writer = new DataStoreWriter() ;
		startUpTaskList.add(writer) ;
		
		listener = new ConnectionListener(port,pool) ;
		listener.start() ;
		
		//this makes consumers and DBWriter can be run in simultaneously.
        for (final Runnable task :startUpTaskList) 
        {
            Thread t = new Thread(task){

                @Override
                public void run() {
                
                    try {
                        startGate.await() ;
                        task.run() ;
                        
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                }
                
            } ;
            
            t.start() ;   
        }//end for
        
        startGate.countDown(); //Open the door to begin to handle request.

	}
	
	
	
	public static boolean shutdown()
	{
		//1. stop accepting new connection request. 
		setStatus("SHUTDOWN") ;
		
		//2.shutdown thread pool
		List<Runnable> cancelList = pool.shutdownNow() ;
		cancelTasks(cancelList) ;
    	
		
		//3.terminate DB Writer
		writer.shutdown() ;
		
		//4.shutdown listener
		listener.shutdown() ;
		
    	System.out.println("Shutdown server successfully.") ;
		
		return true ;
	}
	
	private static void cancelTasks(List<Runnable> cancelList) 
	{
		for (Runnable runnable: cancelList) 
		{
			if(runnable instanceof FutureTask)
			{
				FutureTask task = (FutureTask) runnable ;
	        	task.cancel(true) ;
			}
			
		}
		
		System.out.println(cancelList.size()+" task(s) have been cancelled.") ;

		
	}


	private static void checkAndCreateDataStore(String folderPath) throws BarbaraTestBaseException 
	{
		File storeFolder = new File(folderPath);
		if(FileUtil.checkOperationAttrs(folderPath, "rw")==true)
		{
			if(storeFolder.exists()==false)
			{
				storeFolder.mkdirs() ;
			}
	
			if (storeFolder.exists() == true) 
			{
				Configuration.BIRD_FILE_PATH = folderPath+File.separator+"birds.xml" ;
				Configuration.SIGHTINGS_FILE_PATH = folderPath+File.separator+"sightings.xml" ;
				
				File birdFile = new File(Configuration.BIRD_FILE_PATH) ;
				File sightingsFile = new File(Configuration.SIGHTINGS_FILE_PATH) ;
				
				if(birdFile.exists()==false)
				{
					try {
						birdFile.createNewFile() ;
					} catch (IOException e) 
					{
						throw new FileCreationFailedException(e.getMessage()) ;
					}
					
				}
				
				if(sightingsFile.exists()==false)
				{
					try {
						sightingsFile.createNewFile() ;
					} catch (IOException e) 
					{
						throw new FileCreationFailedException(e.getMessage()) ;
					}
					
				}
				
			} //end if
		}
		else 
		{
			throw new InsufficientPermissionException("Cannot create log file, please confirm if current login user has sufficient privilege.");
		}
		
	}

	
	
	public static String getStatus() {
		return status;
	}


	public static void setStatus(String status) {
		ServerMain.status = status;
	}

}
