package com.barbara.test.server.writer;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.barbara.test.server.exception.BarbaraTestBaseException;
import com.barbara.test.server.global.Configuration;
import com.barbara.test.server.model.Bird;
import com.barbara.test.server.model.Sighting;
import com.barbara.test.server.service.BirdService;
import com.barbara.test.server.service.SightingService;


public class DataStoreWriter extends Thread {

	private BirdService bs = BirdService.getInstance() ;
	private SightingService ss = SightingService.getInstance() ;
	
	private int bird_version = 0 ;
	private int sighting_version = 0 ;
	
	@Override
	public void run() {
		super.run();
		
		while(!Thread.currentThread().isInterrupted())
		{
			//write bird list
			List<Bird> birdList = null;
	
			try {
				birdList = bs.getBirdList();
			} catch (BarbaraTestBaseException e) {
				System.out.println("Cannot get Bird List.");
			}
			
			int newBirdVersion = birdList.hashCode() ;
			if(bird_version != newBirdVersion)
			{
				persistBirds(birdList);
				bird_version = newBirdVersion ;
				
				System.out.println("Birds have been persisted.");
			}
			
			
			
			////write sighting list
			List<Sighting> sightingList = null;
	
			try {
				sightingList = ss.getSightingList();
			} catch (BarbaraTestBaseException e) {
				System.err.println("Cannot get Sighting List.");
			}
	
			int newSightingVersion = sightingList.hashCode() ;
			
			if(sighting_version != newSightingVersion)
			{
				persistSightings(sightingList);
				sighting_version = newSightingVersion ;
				System.out.println("Sightings have been persisted.");
			}
			
			
			
			try {
				Thread.sleep(5000) ;
			} catch (InterruptedException e) { 
				//allow thread to exit
				break ; 
			}
		
		}//end while
		
		System.out.println("DataStore Writer has been terminated successfully.");
	}


	


	private void persistBirds(List<Bird> birdList) 
	{
		
		try {
			
			File birdFile = new File(Configuration.BIRD_FILE_PATH);
			
			//clear content
			PrintWriter writer = new PrintWriter(birdFile);
			writer.print("");
			writer.close();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.newDocument() ;
			
			
			Element root = doc.createElement("birds");
			doc.appendChild(root);
			
			for(Bird b:birdList)
			{
				Element birdNode = doc.createElement("bird");
				root.appendChild(birdNode);
			
				Element el = doc.createElement("name");
				birdNode.appendChild(el);
				Text text = doc.createTextNode(b.getName());
				el.appendChild(text);
				
				el = doc.createElement("color");
				birdNode.appendChild(el);
				text = doc.createTextNode(b.getColor());
				el.appendChild(text);
				
				el = doc.createElement("weight");
				birdNode.appendChild(el);
				text = doc.createTextNode(String.valueOf(b.getWeight()));
				el.appendChild(text);
				
				el = doc.createElement("height");
				birdNode.appendChild(el);
				text = doc.createTextNode(String.valueOf(b.getHeight()));
				el.appendChild(text);
			}
			
			flushOut(doc,Configuration.BIRD_FILE_PATH) ;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}
			
	}
	
	
	private void persistSightings(List<Sighting> sightingList) {

		
		try {
			File sightingFile = new File(Configuration.SIGHTINGS_FILE_PATH);
			
			//clear content
			PrintWriter writer = new PrintWriter(sightingFile);
			writer.print("");
			writer.close();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.newDocument();
			
			
			//flush new data
			Element root = doc.createElement("sightings");
			doc.appendChild(root);
			
			for(Sighting s:sightingList)
			{
				Element birdNode = doc.createElement("sighting");
				root.appendChild(birdNode);
			
				Element el = doc.createElement("name");
				birdNode.appendChild(el);
				Text text = doc.createTextNode(s.getName());
				el.appendChild(text);
				
				el = doc.createElement("location");
				birdNode.appendChild(el);
				text = doc.createTextNode(s.getLocation());
				el.appendChild(text);
				
				el = doc.createElement("time");
				birdNode.appendChild(el);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");  
				String time = sdf.format(s.getDatetime()) ;
				
				text = doc.createTextNode(time);
				el.appendChild(text);
				
			}
			
			flushOut(doc,Configuration.SIGHTINGS_FILE_PATH) ;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}
	
		
	}


	public static void clearNodes(Node node, short nodeType, String name) 
	{
		if (node.getNodeType() == nodeType && (name == null || node.getNodeName().equals(name))) 
		{
			node.getParentNode().removeChild(node);
		} 
		else 
		{
			// check the children recursively
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				clearNodes(list.item(i), nodeType, name);
			}
		}
	}
	
	
	public static boolean flushOut(Document document, String filename) 
	{
		boolean flag = true;
		try {
			
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(filename));
			transformer.transform(source, result);
		
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}


	public void shutdown()
	{
		if(Thread.currentThread().isInterrupted()==false)
		{
			interrupt() ;
		}
		
		while(isAlive())
    	{
			try {
				Thread.sleep(300) ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		
	}
    



}
