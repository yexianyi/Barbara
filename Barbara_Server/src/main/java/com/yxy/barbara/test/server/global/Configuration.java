package com.yxy.barbara.test.server.global;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.yxy.barbara.test.server.model.Task;


public final class Configuration {
	
	public static final String DATE_FORMAT = "yy/MM/dd HH:mm" ;
	
	public static volatile String BIRD_FILE_PATH ;
	public static volatile String SIGHTINGS_FILE_PATH ;
	
	public static volatile ConcurrentLinkedQueue<Task> cacheQueue ;
	
	

}
