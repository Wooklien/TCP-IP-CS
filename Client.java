import java.net.Socket;
import java.net.SocketException;
import java.io.*;

public class Client {

	public static final int BUFFER = 100*1024;
	
	public static void main(String[] args) throws IOException {
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

		String server = null;
		int port = 0;
		
		/*
		 * Setting up server variables, using command line or user input. 
		 */
		if(args.length == 0) {
			System.out.println("No Arguments Passed...");
			
			System.out.println("===================================================================");
			
			System.out.print("Server: ");
			server = console.readLine();
			
			System.out.print("Port: ");
			port = Integer.parseInt(console.readLine());	
		}
		else if(args.length == 2) {
			System.out.println("===================================================================");
			server = args[0];
			port = Integer.parseInt(args[1]);
		}
		
		//Byte Array with 100kb buffer.
		int size; //Size of the file.
		byte[] buffer = new byte[BUFFER];
		
		//Creating Socket that is connected to server on specific port.
		Socket socket = new Socket(server, port);
		System.out.println("Connected to server: " + server + ":" + port);
		System.out.println("===================================================================");
		
		//Asking user to input file name. Will save file using input name.
		System.out.print("Enter <File>: ");
		String file = new String(console.readLine());
		byte[] data = file.getBytes();
		
		OutputStream out = socket.getOutputStream();
		
		//Sending file name to server.
		out.write(data);
		
		//Creating file, with file name and retrieving byte from server.
		FileOutputStream fos = new FileOutputStream(file + ".txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		InputStream in = socket.getInputStream();
		
		int totalBytesRcvd = 0;
		int count = 0;
		
		System.out.println("Downloading " + file + ".txt");
			
		//Writing buffered array to file.
		while((size = in.read(buffer)) != -1) {
			fos.write(buffer, 0, size);
			totalBytesRcvd += size;
			count++;
			
			if(count == 60000){
				System.out.print("[]");
				count = 0;
			}
			
		}
		
		System.out.println("");
		System.out.println("\tRecieved File: <" + file + ".txt> | File Size: " + totalBytesRcvd);
		System.out.println("===================================================================");
		
		//Closing connection.
		in.close();
		out.close();
		socket.close();
		
		System.out.println("Press Enter to exit...");
		console.readLine();
		console.close();
		System.exit(10);
	}

}
