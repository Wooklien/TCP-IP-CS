Server Creates A # of Random Generated Numbers and Sends 10 numbers to each Different Clients. 
The Clients will Read and Add the total of the numbers and return the value back to the server 
so that the sever can easily calculate the total. 


:: Running Server ::
java Server <# of randomly-generated numbers> <# of clients>

example:
java Server 1000 3

:: Running the Client ::
java Client 
or
java Client 127.0.0.1
