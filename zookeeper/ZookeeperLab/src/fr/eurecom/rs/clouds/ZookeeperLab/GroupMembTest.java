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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
 * Consensus Test
 * 
 * myProp takes the String proposal value. Running ConsensusTest.java outputs the decision. 
 * 
 * @version 1.0
 */
public class GroupMembTest {

    static final String hostName = ZKUtils.getHostName();
    static final String loginId = System.getProperty("user.name");
    static final GroupMemb m = new GroupMemb("motisma:2181","/group");
    //TODO: put your own proposal in myProp
    static String data = "member data";
    static long INTERVAL = 10;
    static long ITERATIONS = (15*60)/INTERVAL + 1;
    static int count = 0;
    /**
     * @param args
     */
    public static void main(String[] args) {
        /**
         * If you pass null as first parameter of joinGroup an anonymous node will be
         * created. So you can use this program even if you don't have access to the 
         * hostName and loginId.
         * If you pass a name as first parameter it will be the name of the new node.
         */
        //m.joinGroup(hostName + "-" + loginId, true);
        m.joinGroup(null, true);
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            int count = 0;
            @Override
            public void run() {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                System.out.println("LOG::: GroupMember:: started processing " + dateFormat.format(date));
                List<String> members = m.getMembers();
                for (String memb: members) {
                    System.out.println("LOG::: GroupMember:: " + memb);
                }
                System.out.println("");
                count ++;
                if (count == ITERATIONS) {
                    System.exit(0);
                }
            }
        }, 0, INTERVAL, TimeUnit.SECONDS);
    } //main

} //ConsensusTest
