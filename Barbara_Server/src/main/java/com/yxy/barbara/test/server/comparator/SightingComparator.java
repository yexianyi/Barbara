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
package com.yxy.barbara.test.server.comparator;

import java.util.Comparator;

import com.yxy.barbara.test.server.model.Sighting;


public class SightingComparator implements Comparator 
{

	@Override
	public int compare(Object bird1, Object bird2) 
	{
		
		Sighting b1=(Sighting)bird1;
		Sighting b2=(Sighting)bird2;
		
		int flag = b1.getName().compareTo(b2.getName());
		
		if (flag == 0) {
			return b1.getDatetime().compareTo(b2.getDatetime());
		} else {
			return flag;
		}
	}
	
	

}
