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
