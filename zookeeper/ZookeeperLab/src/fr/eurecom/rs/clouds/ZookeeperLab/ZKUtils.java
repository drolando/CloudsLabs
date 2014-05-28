/*
 * Laboratory Exercises on Zookeeper
 * Distributed Systems and Cloud Computing Course
 * 
 * Prof. Marko Vukolic
 * Networking and Security Department
 * EURECOM
 * 
 * Copyright 2013 EURECOM
 * *******************************************
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.eurecom.rs.clouds.ZookeeperLab;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/*
 * Zookeeper utility functions.
 */
public class ZKUtils {

	/**
	 * Finds the znode with a minimum suffix. 
	 * Use this to find the ``minimum'' sequential znodes. 
	 * 
	 * @param list List of znode paths.
	 * 
	 * @return znode path with minimum sequential suffix.
	 */
	public static String minZnode(List<String> list){
		String minNode = list.get(0);
		Integer min = new Integer(minNode.substring(minNode.length()-9, minNode.length()));
		for (String s : list) {
			System.out.println(s);
			//System.out.println(s);
			Integer tempValue = new Integer(s.substring(s.length()-9, s.length()));
			if(tempValue < min) { 
				min = tempValue;
				minNode = s;
			} //if
		} //for
		return minNode;
	} //minZnode
	
	/**
	 * Returns the host name of this node (null in case of an error).
	 */
	public static String getHostName() {
		String name = null;
		try {
			name = new String(InetAddress.getLocalHost().getCanonicalHostName().toString());
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
		} //try/catch
		
		return name;
	} //myNodeName
	
} //ZKUtils
