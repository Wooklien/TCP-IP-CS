import java.net.*;
import java.nio.file.Files;
import java.io.*;
import java.util.Date;

public class Server {

	public static final int BUFFER = 1;
	
	public static void main(String[] args) throws IOException {
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		Date date = new Date();
		
		//Sever Port String - Must Parse to Int before use. 
		int serverPort = 0;
		
		/*
		 * Server setup using either Command line argument
		 * or user input.
		 */
		if(args.length != 1){
			System.out.println("No Arguments passed: <PORT>");
			
			System.out.print("<PORT>: ");
			serverPort = Integer.parseInt(console.readLine());
		}
		else if(args.length == 1) {
			serverPort = Integer.parseInt(args[0]);
		}
		else {
			System.out.print("INCORRECT NUMBER OF PARAMETERS! Argument <PORT>: ");
			serverPort = Integer.parseInt(console.readLine());
		}
		
		System.out.println("===================================================================");
		System.out.println(":: Server Started :: Listening to Port " + serverPort + " ::");
		System.out.println("===================================================================");
		
		//Create a server socket to accept client connection requests
		ServerSocket serverSocket = new ServerSocket(serverPort);
		
		int size; //Size of the file.
		byte[] buffer = new byte[BUFFER]; //Byte array of buffer size.
		
		while(true) {
			//Accepting client socket request. 
			Socket clientSocket = serverSocket.accept();
			date.getTime();
			
			SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
			System.out.println("Time: " +  new Date().toString() + " | Client at " + clientAddress + " has Connected.");
			
			//Getting file name input from client.
			InputStream inFile = clientSocket.getInputStream();
			
			//Setting up a 32 byte array for file name.
			byte[] data = new byte[32];
			inFile.read(data);
			
			//Creating file object with file name.
			String fileName = new String(data);
			File file = new File(fileName);
			
			System.out.println("Client Requesting File " + file);
			
			/*
			 * Creating a Buffered File Stream to send through socket.  
			 * Error with setting up a client requested file. (Pathing Error)
			 */
			InputStream in = new BufferedInputStream(new FileInputStream("TEST.txt"));
			OutputStream out = clientSocket.getOutputStream();
			
			//Sending File to socket, a Byte at a time.
			System.out.println("Sending File Request to Client.");
			while((size = in.read(buffer)) != -1){
				out.write(buffer);
			}
			
			//Closing connection.
			System.out.println("Client has received its request.");
			System.out.println("Time: " +  new Date().toString() + " | Client at " + clientAddress + " has closed its connection.");
			System.out.println("===================================================================");
			in.close();
			out.close();
			clientSocket.close();
		}
	}
}