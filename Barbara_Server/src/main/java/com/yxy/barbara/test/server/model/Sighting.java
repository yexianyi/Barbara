package com.yxy.barbara.test.server.model;

import java.util.Date;

public class Sighting implements Cloneable{
	
	private String name ;
	
	private Date datetime ;
	
	private String location ;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
        return (Sighting)super.clone();
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 18;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof Sighting)
		{
			Sighting another = (Sighting) obj ;
			if( (this.getName()==null && this.getName()==null) || this.getName().equalsIgnoreCase(another.getName())
			&&( (this.getLocation()==null && this.getLocation()==null) || this.getLocation().equalsIgnoreCase(another.getLocation()))
			&&( (this.getDatetime()==null && this.getDatetime()==null) || this.getDatetime().equals(another.getDatetime())))
			{
				return true ;
			}
			
		}
		
		return false;
	}
}
