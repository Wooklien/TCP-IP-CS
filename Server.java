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
		
		System.out.println(":: Server Started :: Listening to Port " + serverPort + " ::");
		
		//Create a server socket to accept client connection requests
		ServerSocket serverSocket = new ServerSocket(serverPort);
		
		int size; //Size of the file.
		byte[] buffer = new byte[BUFFER];
		
		while(true) {
			Socket clientSocket = serverSocket.accept();
			date.getTime();
			
			SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
			System.out.println("Time: " +  date.toString() + " | Client at " + clientAddress + " has Connected.");
			
			InputStream inFile = clientSocket.getInputStream();
			
			byte[] data = new byte[32];
			inFile.read(data);
			
			String fileName = new String(data);
			File file = new File(fileName);
			
			System.out.println("Client Requesting File " + file );
			
			InputStream in = new BufferedInputStream(new FileInputStream("TEST.txt"));
			OutputStream out = clientSocket.getOutputStream();
			
			while((size = in.read(buffer)) != -1){
				out.write(buffer);
			}
			
			System.out.println("Time: " +  date.toString() + " | Client at " + clientAddress + " has closed its connection.");
			in.close();
			out.close();
			clientSocket.close();
		}
	}
}