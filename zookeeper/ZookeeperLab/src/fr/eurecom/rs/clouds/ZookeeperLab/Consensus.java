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

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/*
 * Consensus Synchronization Primitive
 * 
 * <P>Simple Zookeeper consensus implementation using sequential znodes.
 * 
 * <P> For pseudocode see course slides on Zookeeper, slide 48.
 * 
 * @version 1.0
 */
public class Consensus extends SyncPrimitive {
	Stat stat = null;
	String proposalPrefix;

	/**
	 * Consensus constructor
	 *
	 * @param address Zookeeper server address:port
	 * @param root Consensus root znode. Must be unique for a given consensus instance for all clients.
	 * @param proposalPrefix String prefix for the proposal znode.
	 */
	Consensus(String address, String root, String proposalPrefix) {
		super(address);
		this.root = root;
		this.proposalPrefix = proposalPrefix;

		// Create consensus root znode
		if (zk != null) {
			try {
				Stat s = zk.exists(root, false);
				if (s == null) {
					zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE,
							CreateMode.PERSISTENT);
				} //if
			} catch (KeeperException e) {
				System.out
				.println("Keeper exception when instantiating consensus: "
						+ e.toString());
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception");
			} // try/catch
		} //if
	} //Consensus

	/**
	 * Consensus body. Takes the proposal and returns a decision.
	 *
	 * @param v Proposal value.
	 * @return Consensus decision.
	 */
	public byte[] proposeAndDecide(byte[] v) {
		byte[] decision = "error".getBytes();

		String proposalZnode = root+"/"+proposalPrefix;
		String decisionZnode = null;

		try {
			//Propose a value
			proposalZnode = zk.create(proposalZnode, v, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
			System.out.println("Consensus: the proposal znode is " + proposalZnode);
			//List all proposals
			List<String> proposals = zk.getChildren(root, false);
			//Select a Znode with minimal sequence number
			decisionZnode=root+"/"+ZKUtils.minZnode(proposals);
			//Get decision value
			System.out.println("Consensus: the decision znode is " + decisionZnode);
			decision = zk.getData(decisionZnode, false, stat);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//try/catch
		return decision;
	} //proposeAndDecise
	
} //Consensus
