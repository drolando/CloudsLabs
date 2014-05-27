/*
 * Laboratory Exercises on Zookeeper
 * Distributed Systems and Cloud Computing Course
 * 
 * Prof. Marko Vukolic
 * Networking and Security Department
 * EURECOM
 * 
 * Copyright � 2013 EURECOM
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

/*
 * Consensus Test
 * 
 * myProp takes the String proposal value. Running ConsensusTest.java outputs the decision. 
 * 
 * @version 1.0
 */
public class ConsensusTest {

	static final String myId = System.getProperty("user.name");
	
	//TODO: put your own proposal in myProp
	static String myProp = "put your consensus proposal here";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Uncomment the line below to use hostname as proposal
		myProp = "uppalilo";
		
		Consensus cons = new Consensus("motisma:2181","/cons", myId);
		byte[] proposal = myProp.getBytes();
		byte[] decision = cons.proposeAndDecide(proposal);
		System.out.println("ConsensusTest Decision: " + new String(decision));
	} //main
} //ConsensusTest
