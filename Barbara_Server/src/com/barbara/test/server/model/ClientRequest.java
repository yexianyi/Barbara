package com.barbara.test.server.model;

import java.io.BufferedWriter;

public class ClientRequest {

	private BufferedWriter writer ;
	
	private String request ;
	
	
	public ClientRequest(BufferedWriter out, String request) 
	{
		this.writer = out ;
		this.request = request ;
	}
	
	
	public BufferedWriter getWriter() {
		return writer;
	}

	public String getRequest() {
		return request;
	}
}
