package com.barbara.test.client.main;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.barbara.test.client.dispatcher.impl.CommandDispatcher;
import com.barbara.test.client.handler.inf.IDispatcher;

public class ClientMain{
	
	public static void main (String args[]){
		
//		args = new String[]{"-server", "localhost", "-port", "3000", "-cmd", "listbirds"} ;
		
		Map params = new HashMap() ;
		for(int i=0; i<args.length ; i++)
		{
			params.put(args[i], args[i+1]) ;
			i++ ;
		}
		
		
		if(args.length<3 || args.length>6 || validateArguments(params)==false)
		{
			System.out.println("ERROR:Invalid Command");
			System.out.println("Please follow format: java -jar <Jar File Name> -server <IP> -port <Port> -cmd <Command>");
			System.out.println("Example:");
			System.out.println("	java -jar BarbaraTestCient.jar -server localhost -port 3008 -cmd addbird");
			System.out.println("	java -jar BarbaraTestCient.jar -server localhost -port 3008 -cmd addsighting");
			System.out.println("	java -jar BarbaraTestCient.jar -server localhost -port 3008 -cmd listbirds");
			System.out.println("	java -jar BarbaraTestCient.jar -server localhost -port 3008 -cmd listsightings");
			System.out.println("	java -jar BarbaraTestCient.jar -server localhost -port 3008 -cmd remove");
			System.out.println("	java -jar BarbaraTestCient.jar -server localhost -port 3008 -cmd quit");
			
			System.exit(0) ;
		}
		else
		{
	        startupCmdConsole(params) ;
		}
		
		

		
	}
	



	private static void startupCmdConsole(Map params) {

        Socket socket = null ;
        String response = null ;
        
        try{
        	String ip = (String) params.get("-server") ;
        	Integer port = Integer.valueOf((String) params.get("-port")) ;
        	String command = (String) params.get("-cmd") ;
        	
    		if(port==null) //exclude port
    		{
    			socket =  new Socket(ip, 3000) ;
    			response = handleCommand(socket,command);
    			
    		}
    		else
    		{
    			socket = new Socket(ip, port) ;
    			response = handleCommand(socket,command);
    		}
        		
        		
        	System.out.println(response+"\n");
        		
           	
        } catch(UnknownHostException e) {
        	
        	System.out.println("Cannot find specified host.");
        	
        } catch(IOException e){
        	
        	System.out.println("Cannot connect to server because: "+e.getMessage());
        	
        } finally {
        	
        	System.out.print("Client is terminated.");
			System.exit(0);
        }
	    
		
	}


	/*
	 * Dispatch commands
	 */
	private static String handleCommand(Socket socket, String command) {

		IDispatcher dispatcher = new CommandDispatcher(socket, command) ;
		
		return dispatcher.dispatch() ;
		
	}


	
	
	
	private static boolean validateArguments(Map params) {
		
		String ip = (String) params.get("-server") ;
		String port = (String) params.get("-port") ;
		String command = (String) params.get("-cmd") ;

		
		if(ip!=null)
		{
			
			if(validateIPAddr(ip)==false)
			{
				return false ;
			}
		}
		
		if(port!=null) 
		{
			if(validateIPPort(port)==false)
			{
				return false ;
			}
		}
		
		if(command!=null)
		{
			if(validateCommand(command)==false)
			{
				return false ;
			}
		}
		
		return true;
	}


	private static boolean validateIPPort(String port) 
	{
		try{
			
			int portNum = Integer.valueOf(port) ;
			
			if(portNum<1 || portNum>65535)
			{
				return false ;
			}
			
		}catch (java.lang.NumberFormatException ignore)
		{
			return false ;
		}
		
		
		
		return true;
	}



	//validate command
	private static boolean validateCommand(String cmd) {
		
		if(cmd.equalsIgnoreCase("addbird") || cmd.equalsIgnoreCase("addsighting") ||
		   cmd.equalsIgnoreCase("listbirds") ||cmd.equalsIgnoreCase("listsightings") ||
		   cmd.equalsIgnoreCase("remove") ||cmd.equalsIgnoreCase("quit"))
		{
			return true ;
			
		}
		
		return false;
	}



	//validate IP
	private static boolean validateIPAddr(String ipAddr) {
		
		if(ipAddr.contains("."))
		{
			Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
			Matcher matcher = pattern.matcher(ipAddr);
			
			if(matcher.matches()==false)
				return false ;
			
		}
		
		return true;
	}
}

