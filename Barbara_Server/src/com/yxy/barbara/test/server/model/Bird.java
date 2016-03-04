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
