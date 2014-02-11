Quoc Lien
02.10.14

##### Running through Jar Files #####
Note: TEST.txt should be in parent directory along with Jar Files. 
Export to Client.java and Server.java files to executable jar as Client.jar and Server.jar

Run Server through command line.
java -jar server.jar <Port#>
Server should be set to listen to port #.
Note: Port is passed in through command line. Can change it to anything.

Run Client through command line.
java -jar Client.jar <ServerIP> <Port#>
If Arguments are not pass through command line.
Enter Server Ip and Port (using server port) 
Enter File name.

Server will send a hard coded file and it will be saved as the input File Name. 
Note: Using the same name will overwrite any file with the same name.



##### Running through compiler #####
Note: TEST.txt should be in default folder. It is hardcoded to 
read from default source folder.
Compile Server.java
Enter Port #
Server should be set to listen to port #.

Compile Client.java
Enter Server IP and Port (Using server port)
Enter File name.

Server will send a hard coded file and it will be saved as the input File Name.
The file will be saved in the client's default src folder. 
Note: Using the same name will overwrite any file with the same name.
