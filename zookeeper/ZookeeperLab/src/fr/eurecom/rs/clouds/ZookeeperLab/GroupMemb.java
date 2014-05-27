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

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.SysexMessage;

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
public class GroupMemb extends SyncPrimitive {
	Stat stat = null;
	String name;

	/**
	 * Consensus constructor
	 *
	 * @param address Zookeeper server address:port
	 * @param root Consensus root znode. Must be unique for a given consensus instance for all clients.
	 * @param proposalPrefix String prefix for the proposal znode.
	 */
	GroupMemb(String address, String root, String name) {
		super(address);
		this.root = root;
		this.name = name;
		
		// Create consensus root znode
		if (zk != null) {
			try {
				Stat s = zk.exists(root, false);
				if (s == null) {
					System.err.println("Node " + root + " doesn't exist! Check your settings.");
					System.exit(1);
				} //if
			} catch (KeeperException e) {
				System.out
				.println("Keeper exception when instantiating consensus: "
						+ e.toString());
				System.exit(2);
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception");
				System.exit(2);
			} // try/catch
		} //if
	} //Consensus

	public void joinGroup(String name, boolean ephemeral) {
		try {
			zk.create(this.root + "/" + name, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public List<String>getMembers() {
		List<String> members = new ArrayList<String>();
		
		return members;
	}
	
} //Consensus