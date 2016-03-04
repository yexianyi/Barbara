# Barbara

Barbara project is an java project that complies with Apache License 2.0 and demonstrates advanced usage of Java Concurrency Toolkit.
Detailed requirements are stated as below. You can also find out an separate user guide from this repository to know how to run this app.

## Barbara Test Server
### How to build
1)	Unzip BarbaraTest.zip under any path
2)	Open Barbara_Server folder
3)	Run ant command, e.g X:\Barbara_Server\ant
4)	You can find the build jar, BarbaraTestServer.jar, at X:\Barbara_Server\build

### About source code
1)	Unzip BarbaraTest.zip under any path
2)	Open Eclipse and locate to Barbara_Server folder to import project
3)	You can see all the sourcecode under src folder

### How to run
1)	Open a command line console
2)	Drill down to X:\Barbara_Server\build
3)	Invoke following command:
java -jar BarbaraTestServer.jar -port 3008 -data c:\\data -proc_count 3

[Parameters]:
-port (optional): Default port is 3000. A valid TCP port number is between 1 and 65535.
-data (option): is the location of the folder where server keeps its data store. Default location is "serverdata" folder in user's home directory. If the directory does not exist, it is created. If it exists, it is used. 
-proc_count (optional): is a positive integer to determine how many requests can be handled in parallelly. Default value is 2.
4) Once Server startup, you will see following info from console:

Port:3008
Data Store Files have been created on c:\\data\serverdata
Proc_count:3
Birds have been persisted.
Sightings have been persisted.

## Barbara Test Client
###	How to build
1)	Unzip BarbaraTest.zip under any path
2)	Open Barbara_Client folder
3)	Run ant command, e.g X:\Barbara_Client\ant
4)	You can find the build jar, BarbaraTestClient.jar, at X:\Barbara_Client\build

###	About source code
1)	Unzip BarbaraTest.zip under any path
2)	Open Eclipse and locate to Barbara_Client folder to import project
You can see all the sourcecode under src folder

###	How to run
1)	Open a command line console
2)	Drill down to X:\Barbara_Client\build
3)	Invoke following command(s):
java -jar BarbaraTestClient.jar -server localhost -port 3008 -cmd addbird
java -jar BarbaraTestClient.jar -server localhost -port 3008 -cmd addsighting
java -jar BarbaraTestClient.jar -server localhost -port 3008 -cmd listbirds
java -jar BarbaraTestClient.jar -server localhost -port 3008 -cmd listsightings
java -jar BarbaraTestClient.jar -server localhost -port 3008 -cmd remove
java -jar BarbaraTestClient.jar -server localhost -port 3008 -cmd quit

[Parameters]:
-server: Server IP Address
-serverPort (optional): Server Port. Default is 3000.
-addbird: When this option is specified, the client will be asked for enter the bird name, color, weight and height on the command line. (Please do not include UNIT into Height and Weight; And also do not input duplicate Bird name. )
-addsighting: When this option is specified, the client will be asked for enter the bird name, location, date and time on the command line. (Please follow Date Format prompted in the command line.)
-remove: When this option is specified, client prompts the user for bird name only.
-quit: When this option is specified, client sends a quit message to the server and server performs a shutdown operation and quits.
-listbirds: When this option is specified, the client sends a message to the server to send the entire bird list. Server sends this information back and client prints all the birds on the server in a table with a column for name, color, weight and height. The table is printed in alphabetical order of the bird names. Client also prints total number of birds present on the server.
- listsightings: When this option is specified, the client asks the user to enter the bird name, start date and end date on the command line. The bird name can be a regular expression. Client takes this data and sends to the server a message to return the bird sightings and prints a table with following columns: name, date which is sorted by alphabetical order of the bird names and further sorted in order of time.

?