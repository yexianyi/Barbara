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

public class Bird implements Cloneable{
	
	private String name ;
	
	private String color ;
	
	private float weight ;
	
	private float height ;

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
        return (Bird)super.clone();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 17;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof Bird)
		{
			Bird another = (Bird) obj ;
			if(  (this.getName()==null && this.getName()==null) || this.getName().equalsIgnoreCase(another.getName())
			&& ( (this.getColor()==null && this.getColor()==null) || this.getColor().equalsIgnoreCase(another.getColor()))
			&& this.getHeight() == another.getHeight()
			&& this.getWeight() == another.getWeight())
			{
				return true ;
			}
			
		}
		
		return false;
	}
	
	
	
	
	

}
