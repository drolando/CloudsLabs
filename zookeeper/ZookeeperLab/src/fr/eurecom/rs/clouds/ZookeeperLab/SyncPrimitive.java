/*
 * Copyright © 2010 The Apache Software Foundation 
 * 
 * file SyncPrimitives.java modified for
 * 
 * Laboratory Exercises on Zookeeper
 * Distributed Systems and Cloud Computing Course
 * 
 * Prof. Marko Vukolic
 * Networking and Security Department
 * EURECOM
 * 
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

package fr.eurecom.rs.clouds.ZookeeperLab;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;


/*
 * Thin wrapper around the Zookeeper service constructor.
 */
public class SyncPrimitive implements Watcher {

    protected static ZooKeeper zk = null;

    protected String root;

    /*
     * Thin wrapper around the Zookeeper service constructor.
     */
    protected SyncPrimitive(String address) {
        if(zk == null){
            try {
                System.out.println("Starting ZK:");
                zk = new ZooKeeper(address, 3000, this);
                System.out.println("Finished starting ZK: " + zk);
            } catch (IOException e) {
                System.out.println(e.toString());
                zk = null;
            }
        }
    }

    /*
     *  Needs to be populated in case the synchronization primitive uses watches. 
     *  @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
     */
    synchronized public void process(WatchedEvent event) {
    	
    } //process
}