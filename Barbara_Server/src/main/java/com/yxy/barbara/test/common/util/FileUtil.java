package com.yxy.barbara.test.common.util;

import java.io.File;


public abstract class FileUtil {
    public FileUtil() {
        super();
    }
    
    public static String getOperationAttrs(String filePath)
    {
        File file = new File(filePath) ;  
        String attr = "" ;
        if(file.canRead()==true)
        {
            attr += "r" ;
        }
        
        if(file.canWrite()==true)
        {
            attr += "w" ;
        }
        
        return attr ;
    }
    
    
    public static boolean checkOperationAttrs(String filePath, String expect)
    {
        
        File file = new File(filePath) ;
        
        while(file!=null)
        {
        	if(file.exists()==false)
        	{
        		file = file.getParentFile() ;
        		continue ;
        	}
        	
        	String operation = getOperationAttrs(file.getAbsolutePath()) ;
        	
        	 char[] expArray = expect.toCharArray() ;
             for(char ch:expArray)
             {
                 if(operation.contains((""+ch).toLowerCase())==false)
                 {
                     return false ;
                 }
             }//end for
        	
             return true ;
             
        }//end while
        
        return true ;
       
    }
    
}
