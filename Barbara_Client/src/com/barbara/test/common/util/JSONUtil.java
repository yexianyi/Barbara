package com.barbara.test.common.util;


import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class JSONUtil {
    public JSONUtil() {
        super();
    }
    
    
    public static boolean hasAttribute(JSONObject jsonObj, String attr)
    {
                Iterator it = jsonObj.keys();
                String key = "";
                while (it.hasNext()) {
                    key = it.next().toString();
                    if (key.equalsIgnoreCase(attr)) {
                        return true ;
                    }
                    
                    try {
                        Object obj = jsonObj.get(key);
                        if(obj instanceof JSONObject)
                        {
                            return hasAttribute((JSONObject)obj, attr);
                        }
                        else if(obj instanceof JSONArray)
                        {
                            JSONArray array = (JSONArray)obj;
                            for(int i = 0; i < array.length(); i++)
                            {
                                return hasAttribute(array.getJSONObject(i), attr);         
                            }
                        }
                        
                    } catch (JSONException e) {
                    	e.printStackTrace() ;
                    }
                }

                return false;
    }
    
    
    public static String findAttribute(JSONObject jsonObj, String attr) {

        Iterator it = jsonObj.keys();
        String key = "";
        while (it.hasNext()) {
            key = it.next().toString();
            if (key.equalsIgnoreCase(attr)) {
                try {
                    return jsonObj.get(key).toString();
                } catch (JSONException e) {
                	e.printStackTrace() ;
                }
            }
            
            try {
                Object obj = jsonObj.get(key);
                if(obj instanceof JSONObject)
                {
                    return findAttribute((JSONObject)obj, attr);
                }
                else if(obj instanceof JSONArray)
                {
                    JSONArray array = (JSONArray)obj;
                    for(int i = 0; i < array.length(); i++)
                    {
                        return findAttribute(array.getJSONObject(i), attr);         
                    }
                }
                
            } catch (JSONException e) {
            	e.printStackTrace() ;
            }
        }

        return null;
     
    }
    
    public static JSONObject findAttrObj(JSONObject jsonObj, String attr) {

        Iterator it = jsonObj.keys();
        String key = "";
        while (it.hasNext()) {
            key = it.next().toString();
            if (key.equalsIgnoreCase(attr)) {
                try {
                    return jsonObj.getJSONObject(key);
                } catch (JSONException e) {
                	e.printStackTrace() ;
                }
            }
            
            try {
                Object obj = jsonObj.get(key);
                if(obj instanceof JSONObject)
                {
                    return findAttrObj((JSONObject)obj, attr);
                }
                else if(obj instanceof JSONArray)
                {
                    JSONArray array = (JSONArray)obj;
                    for(int i = 0; i < array.length(); i++)
                    {
                        return findAttrObj(array.getJSONObject(i), attr);         
                    }
                }
                
            } catch (JSONException e) {
            	e.printStackTrace() ;
            }
        }

        return null;
     
    }
    
    
    public static JSONArray findArray(JSONObject jsonObj, String attr) {
            
            Iterator it = jsonObj.keys();
            String key = "";
            while (it.hasNext()) {
                key = it.next().toString();
                if (key.equalsIgnoreCase(attr)) {
                    try {
                        Object obj = jsonObj.get(key);
                        if(obj instanceof JSONArray)
                            return (JSONArray)obj;
                    } catch (JSONException e) {
                    	e.printStackTrace() ;
                    }
                }
                
                try {
                    Object obj = jsonObj.get(key);
                    if(obj instanceof JSONObject)
                    {
                        return findArray((JSONObject)obj, attr);
                    }
                    else if(obj instanceof JSONArray)
                    {
                        JSONArray array = (JSONArray)obj;
                        for(int i = 0; i < array.length(); i++)
                        {
                            return findArray(array.getJSONObject(i), attr);         
                        }
                    }
                    
                } catch (JSONException e) {
                	e.printStackTrace() ;
                }
            }

            return null;
        }
    
    
}



