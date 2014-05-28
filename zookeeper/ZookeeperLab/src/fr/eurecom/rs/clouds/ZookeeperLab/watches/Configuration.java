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
package fr.eurecom.rs.clouds.ZookeeperLab.watches;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.KeeperException;

import fr.eurecom.rs.clouds.ZookeeperLab.SyncPrimitive;

/*
 * Configuration Synchronization Primitive
 * 
 * <P>Simple Zookeeper configuration implementation using watchers.
 * 
 * <P> For pseudocode see course slides on Zookeeper, slide 49.
 * 
 * @version 1.0
 */
public class Configuration extends SyncPrimitive {
	Stat stat = null;
	byte[] config;

	/**
	 * Configuration constructor
	 *
	 * @param address Zookeeper server address:port
	 * @param root Configuration root znode. 
	 */
	Configuration(String address, String root) {
		super(address);
		this.root = root;
		
		if (zk != null) {
            try {
                Stat s = zk.exists(root, false);
                if (s == null) {
                    System.err.println("Node " + root + " doesn't exist! Check your settings.");
                    System.exit(1);
                } else {
                    Stat s1 = new Stat();

                    byte[] res = zk.getData(this.root, true, s1);
                    this.printStat(s1);
                }
            } catch (KeeperException e) {
                System.out
                .println("keeper exception when instantiating consensus: "
                        + e.toString());
                System.exit(2);
            } catch (InterruptedException e) {
                System.out.println("interrupted exception");
                System.exit(2);
            } // try/catch
				
				/*
				 * TODO 
				 * 1) Check if the configuration root exists and set the watch
				 * 2) if it exists print the configuration and reset the watch
				 * 
				 */
			
		} //if
	} //Configuration
	
	synchronized public void process(WatchedEvent event) {
		if (event == null || event.getPath() == null)
			return;
		if (event.getPath().equals(root)) {
			//TODO complete the Configuration code			
            try {
                Stat s1 = new Stat();
                zk.getData(this.root, true, s1);
                this.printStat(s1);
            } catch (KeeperException e) {
                System.out
                .println("keeper exception when instantiating consensus: "
                        + e.toString());
                System.exit(2);
            } catch (InterruptedException e) {
                System.out.println("interrupted exception");
                System.exit(2);
            } // try/catch
		}
	} //process

    public void printStat(Stat s) {
        System.out.println("STAT:: lczxid " + s.getCzxid());
        System.out.println("STAT:: mzxid " + s.getMzxid());
        System.out.println("STAT:: ctime " + s.getCtime());
        System.out.println("STAT:: mtime " + s.getMtime());
        System.out.println("STAT:: version " + s.getVersion());
        System.out.println("STAT:: cversion " + s.getCversion());
        System.out.println("STAT:: aversion " + s.getAversion());
        System.out.println("STAT:: ephemeralOwner " + s.getEphemeralOwner());
        System.out.println("STAT:: dataLength " + s.getDataLength());
        System.out.println("STAT:: numChildren " + s.getNumChildren());
        System.out.println("STAT:: pzxid " + s.getPzxid());
        System.out.println("");
    }
	
} //Configuration
