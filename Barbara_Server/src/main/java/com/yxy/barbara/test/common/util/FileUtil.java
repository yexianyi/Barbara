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
