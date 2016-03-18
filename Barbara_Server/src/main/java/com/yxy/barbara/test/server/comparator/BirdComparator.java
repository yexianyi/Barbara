package com.yxy.barbara.test.server.comparator;

import java.util.Comparator;

import com.yxy.barbara.test.server.model.Bird;


public class BirdComparator implements Comparator 
{

	@Override
	public int compare(Object bird1, Object bird2) 
	{
		
		Bird b1=(Bird)bird1;
		Bird b2=(Bird)bird2;
		
		
		return b1.getName().compareTo(b2.getName());
	}
	
	

}
