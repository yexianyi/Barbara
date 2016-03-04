package com.barbara.test.server.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.barbara.test.server.comparator.SightingComparator;
import com.barbara.test.server.exception.BarbaraTestBaseException;
import com.barbara.test.server.exception.CloneObjectFailedException;
import com.barbara.test.server.global.Configuration;
import com.barbara.test.server.model.Sighting;


public class SightingService {
	
	private static volatile SightingService instance = null ;
	
	private List<Sighting> sightingList = new ArrayList() ;
	
	
	public static synchronized SightingService getInstance()
	{
		if(instance==null)
		{
			instance = new SightingService() ;
			
		}
		
		return instance ;
	}
	
	
	private SightingService()
	{
		init() ;
		
	}
	
	
	private void init()
	{
		
		//load birds from data store
		try {
			 
			File sightingFile = new File(Configuration.SIGHTINGS_FILE_PATH);
			//current file is empty
			if(sightingFile.length()==0)
				return ;
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(sightingFile);
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nList = doc.getElementsByTagName("sighting");
		 
			for (int i=0; i<nList.getLength(); i++) 
			{
		 
				Node nNode = nList.item(i);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
		 
					Element eElement = (Element) nNode;
		 
					String name = eElement.getElementsByTagName("name").item(0).getTextContent() ;
					String location = eElement.getElementsByTagName("location").item(0).getTextContent() ;
					String time = eElement.getElementsByTagName("time").item(0).getTextContent() ;
					
					SimpleDateFormat sdf = new SimpleDateFormat(Configuration.DATE_FORMAT);
					Date datetime = sdf.parse(time);
						
					Sighting s = new Sighting() ;
					s.setName(name) ;
					s.setLocation(location) ;
					s.setDatetime(datetime) ;
					
					sightingList.add(s) ;
					
				}
			}
			
	    } catch (NumberFormatException e) {
	    	e.printStackTrace();
	    	//TODO:Need to enhance code to handle this exception 
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
		} 
		
		
	}
	
	
	public synchronized String addSighting(String name, String location, String time)
	{

		String status = null ;
		String error = null ;
		
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(Configuration.DATE_FORMAT);
			Date datetime = sdf.parse(time);
			
			Sighting s = new Sighting() ;
			s.setName(name) ;
			s.setLocation(location) ;
			s.setDatetime(datetime) ;
			
			sightingList.add(s) ;
			
			status = "SUCCESS" ;
			error = "NONE" ;
			
			
		} catch (ParseException e) {
			status = "FAILED" ;
			error = "Datetime is not qualified." ;
		} 
		
		
		return "{\"BARBARA\":" +
		"{" +
        "   \"MSG TYPE\":\"Add Sighting Response\"," +
        "   \"MSG PARAM\":{" +
        "                \"STATUS\":\""+status+"\"," +
        "                \"ERROR\":\""+error+"\"" +
        "                    }" +
        "        }" +
        "}" ;
		
		
	}
	

	public synchronized String listSightings(String nameExp, String start, String end)
	{
		String status = null ;
		String error = null ;
		
		 
		String array = "" ;
		Date startTime = null;
		Date endTime = null ;
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Configuration.DATE_FORMAT);  
			startTime = sdf.parse(start);
			endTime = sdf.parse(end);
			
			SightingComparator comparator = new SightingComparator();
			Collections.sort(sightingList, comparator);
	        
	        Iterator<Sighting> iter = sightingList.iterator() ;
	        while(iter.hasNext())
	        {
	        	Sighting s = iter.next() ;
	        	
	        	String name = s.getName() ;
	        	Date time = s.getDatetime() ;
	        	
	        	Pattern pattern = Pattern.compile(nameExp);
				Matcher matcher = pattern.matcher(name);
				boolean b= matcher.matches();
				
				if(b==false || time.before(startTime) || time.after(endTime))
				{
					continue ;
				}
				
	        	array += "{" ;
	        	array += "\"NAME\":"+"\""+name+"\"," ;
	        	array += "\"LOCATION\":"+"\""+s.getLocation()+"\"," ;
	        	 
	   	     	String datetime = sdf.format(time);
	        	array += "\"TIME\":"+"\""+datetime+"\"" ;
	            
	            if(iter.hasNext()==false)
	            	array += "}" ;
	            else
	            	array += "}," ;
	        }//end while
			
	        
	        status = "SUCCESS" ;
			error = "NONE" ;
			
		} catch (ParseException e) {
			status = "FAILED" ;
			error = "Time format is not qualified to "+Configuration.DATE_FORMAT ;
			
		}
        
        return  "{\"BARBARA\":" +
		            "   {" + 
		            "   \"MSG TYPE\":\"List Sighting Response\"," +
		            "               \"MSG PARAM\" : {" + 
		            "                       \"STATUS\" : \""+status+"\"," + 
		            "                       \"ERROR\" : \"" + error + "\"," + 
		            "                       \"SIGHTINGS\" : [" + array + "]"+
		            "                               }" + 
		            "   }" + 
		            "}";
	}
	
	
	public synchronized List getSightingList() throws BarbaraTestBaseException
	{
		List<Sighting> clone = new ArrayList<Sighting>(sightingList.size());
	    for(Sighting s: sightingList) 
	    {
	    	try {
				clone.add((Sighting) s.clone());
			} catch (CloneNotSupportedException e) {
				
				throw new CloneObjectFailedException(e.getMessage()) ;
			}
	    }
	    
	    return clone;
	}
	

}
