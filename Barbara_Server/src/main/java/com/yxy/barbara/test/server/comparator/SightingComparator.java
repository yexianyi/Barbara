package com.yxy.barbara.test.server.comparator;

import java.util.Comparator;

import com.yxy.barbara.test.server.model.Sighting;


public class SightingComparator implements Comparator 
{

	@Override
	public int compare(Object bird1, Object bird2) 
	{
		
		Sighting b1=(Sighting)bird1;
		Sighting b2=(Sighting)bird2;
		
		int flag = b1.getName().compareTo(b2.getName());
		
		if (flag == 0) {
			return b1.getDatetime().compareTo(b2.getDatetime());
		} else {
			return flag;
		}
	}
	
	

}
