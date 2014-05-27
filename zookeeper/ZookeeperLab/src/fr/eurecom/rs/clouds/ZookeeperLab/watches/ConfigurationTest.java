/*
 * Laboratory Exercises on Zookeeper
 * Distributed Systems and Cloud Computing Course
 * 
 * Prof. Marko Vukolic
 * Networking and Security Department
 * EURECOM
 * 
 * Copyright © 2013 EURECOM
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

package fr.eurecom.rs.clouds.ZookeeperLab.watches;

/*
 * Configuration Test
 * 
 * Infinite loop waiting for asynchronous updates from watcher on configuration znode.
 * Do not modify this test file. 
 * Edit Configuration.java so the test implements the functionality of pseudocode in slide 49. 
 * 
 * @version 1.0
 */
public class ConfigurationTest {

	/**
	 * Do not change this test file. 
	 * Edit Configuration.java so the test implements the functionality of pseudocode in slide 49. 
	 */
	public static void main(String[] args) {
		
		new Configuration("motisma:2181","/config");
		
		while(true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	} //main
} //ConsensusTest
