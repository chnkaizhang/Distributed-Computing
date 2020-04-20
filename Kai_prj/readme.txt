1. OS: Mac OS Catalina
2. Language: Java version 12
3. Software needed: cloudlab
4. External jars: zookeeper-3.6.0.jar zookeeper-jute-3.6.0.jar log4j-1.2.17.jar slf4j-api-1.7.25.jar slf4j-log4j12-1.7.25.jar
4. Program structure: 
My program can be divided into 7 parts:
DFSCLIENT.java & DFSZCLIENT.java: connect client and server
DFSSERVER.java & DFSZSERVER.java: connect client and server
ServerSevice.java & ZKSevice.java: operation of servers
CONFIG.java: store IP and port of servers
Instruction of run my code:
First, run make to compile my program.
Second, run java -cp .:zookeeper-3.6.0.jar:zookeeper-jute-3.6.0.jar:log4j-1.2.17.jar:slf4j-api-1.7.25.jar:slf4j-log4j12-1.7.25.jar DFSSERVER/DFSCLIENT

Instruction:
I set unique ID for every clients and servers. Thus I split all servers and clients into independent directory. When you run my code in cloud lab, you have to find right place.
For instance: from my topology, there are five nodes in cloudlab experiment. Node0 is client1, node1 is client2, node2 is server1, node3 is server2, node4 is server3. If you want to run client1, you need to cd to client1 folder in node0 and run DFSCLIENT.java, if  you want to run client2, you need to cd to client2 folder in node1 and run DFSCLIENT.java, if you want to run server1, you need to cd to server1 in node2 and run DFSSERVER.java.