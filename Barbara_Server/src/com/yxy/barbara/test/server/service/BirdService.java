package com.yxy.barbara.test.server.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yxy.barbara.test.server.comparator.BirdComparator;
import com.yxy.barbara.test.server.exception.BarbaraTestBaseException;
import com.yxy.barbara.test.server.exception.CloneObjectFailedException;
import com.yxy.barbara.test.server.global.Configuration;
import com.yxy.barbara.test.server.model.Bird;


public class BirdService {
	
	private static volatile BirdService instance = null ;
	
	private List<Bird> birdList = new ArrayList() ;
	
	
	public static synchronized BirdService getInstance()
	{
		if(instance==null)
		{
			instance = new BirdService() ;
			
		}
		
		return instance ;
	}
	
	
	private BirdService()
	{
		init() ;
		
	}
	
	
	private void init()
	{
		
		//load birds from data store
		try {
			 
			File birdFile = new File(Configuration.BIRD_FILE_PATH);
			
			//current file is empty
			if(birdFile.length()==0)
				return ;
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(birdFile);
		 
			NodeList nList = doc.getElementsByTagName("bird");
		 
			for (int i=0; i<nList.getLength(); i++) 
			{
		 
				Node nNode = nList.item(i);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
		 
					Element eElement = (Element) nNode;
		 
					String name = eElement.getElementsByTagName("name").item(0).getTextContent() ;
					String color = eElement.getElementsByTagName("color").item(0).getTextContent() ;
					String weight = eElement.getElementsByTagName("weight").item(0).getTextContent() ;
					String height = eElement.getElementsByTagName("height").item(0).getTextContent() ;
					
					Bird bird = new Bird() ;
					bird.setName(name) ;
					bird.setColor(color) ;
					bird.setWeight(Float.valueOf(weight)) ;
					bird.setHeight(Float.valueOf(height)) ;
					
					birdList.add(bird) ;
					
				}
			}
			
	    } catch (NumberFormatException e) {
	    	e.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		
	}
	
	
	public synchronized String checkAndAddbird(String name, String color, float weight, float height)
	{

		String status = null ;
		String error = null ;
		
		
		try
		{
			if(isBirdExisted(name)==true)
			{
				status = "FAILED" ;
				error = "Duplicate Bird Name is already existed." ;
				
			}
			else
			{
				Bird bird = new Bird() ;
				bird.setName(name) ;
				bird.setColor(color) ;
				bird.setWeight(Float.valueOf(weight)) ;
				bird.setHeight(Float.valueOf(height)) ;
				
				birdList.add(bird) ;
				
				status = "SUCCESS" ;
				error = "NONE" ;
			}
			
			
		} catch (NumberFormatException e) {
			status = "FAILED" ;
			error = "Weight or Height number format is invalid" ;
	    } 
		
		
		return "{\"BARBARA\":" +
		"{" +
        "   \"MSG TYPE\":\"Add Bird Response\"," +
        "   \"MSG PARAM\":{" +
        "                \"STATUS\":\""+status+"\"," +
        "                \"ERROR\":\""+error+"\"" +
        "                    }" +
        "        }" +
        "}" ;
		
		
	}
	
	
	public synchronized String checkAndRemovebird(String name)
	{
		String status = null ;
		String error = null ;
		
		
		boolean isRemoved = false ;
		
		Iterator<Bird> iter = birdList.iterator() ;
		while(iter.hasNext())
		{
			Bird b = iter.next() ;
			
			if(b.getName().equalsIgnoreCase(name))
			{
				iter.remove() ;
				isRemoved = true ;
				break ;
			}
			
		}//end while
		
		
		if(isRemoved==true)
		{	
			status = "SUCCESS" ;
			error = "NONE" ;
			
		}
		else
		{
			status = "FAILED" ;
			error = "Not Found Specified Bird." ;
		}
		
	
		
		return "{\"BARBARA\":" +
		"{" +
        "   \"MSG TYPE\":\"Remove Bird Response\"," +
        "   \"MSG PARAM\":{" +
        "                \"STATUS\":\""+status+"\"," +
        "                \"ERROR\":\""+error+"\"" +
        "                    }" +
        "        }" +
        "}" ;
		
	}
	
	
	public synchronized String listBirds()
	{
		BirdComparator comparator = new BirdComparator();
		Collections.sort(birdList, comparator);
        
        String array = "" ;
        Iterator<Bird> iter = birdList.iterator() ;
        
        while(iter.hasNext())
        {
        	Bird b = iter.next() ;
        	
        	array += "{" ;
        	array += "\"NAME\":"+"\""+b.getName()+"\"," ;
        	array += "\"COLOR\":"+"\""+b.getColor()+"\"," ;
        	array += "\"WEIGHT\":"+"\""+b.getWeight()+"\"," ;
        	array += "\"HEIGHT\":"+"\""+b.getHeight()+"\"" ;
            
            if(iter.hasNext()==false)
            	array += "}" ;
            else
            	array += "}," ;
        }
        
        return  "{\"BARBARA\":" +
		            "   {" + 
		            "   \"MSG TYPE\":\"List Birds Response\"," +
		            "               \"MSG PARAM\" : {" + 
		            "                       \"STATUS\" : \"SUCCESS\"," + 
		            "                       \"ERROR\" : \"" + "NONE" + "\", " + 
		            "                       \"BIRDS\" : [" + array + "] "+
		            "                               }" + 
		            "   }" + 
		            "}";
		  
		
	}
	
	
	public synchronized List getBirdList() throws BarbaraTestBaseException
	{
		List<Bird> clone = new ArrayList<Bird>(birdList.size());
	    for(Bird d: birdList) 
	    {
	    	try {
				clone.add((Bird) d.clone());
			} catch (CloneNotSupportedException e) {
				
				throw new CloneObjectFailedException(e.getMessage()) ;
			}
	    }
	    
	    return clone;
	}
	
	
	
	
	private boolean isBirdExisted(String name)
	{
		for(Bird b:birdList)
		{
			if(b.getName().equalsIgnoreCase(name))
			{
				return true ;
				
			}
			
		}
		
		return false ;
		
	}
	
	

}
