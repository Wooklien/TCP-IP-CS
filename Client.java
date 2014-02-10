import java.net.Socket;
import java.net.SocketException;
import java.io.*;

public class Client {

	public static final int BUFFER = 100*1024;
	
	public static void main(String[] args) throws IOException {
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

		String server = null;
		int port = 0;
		
		if(args.length == 0) {
			System.out.println("No Arguments Passed...");
			
			System.out.print("Server: ");
			server = console.readLine();
			
			System.out.print("Port: ");
			port = Integer.parseInt(console.readLine());	
		}
		
		int size; //Size of the file.
		byte[] buffer = new byte[BUFFER];
		
		System.out.print("Enter <File>: ");
		String file = new String(console.readLine());
		byte[] data = file.getBytes();
		
		//Creating Socket that is connected to server on specific port.
		Socket socket = new Socket(server, port);
		System.out.println("Connected to server: " + server + ":" + port);
		
		OutputStream out = socket.getOutputStream();
		
		out.write(data);
		
		FileOutputStream fos = new FileOutputStream(file + ".txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		InputStream in = socket.getInputStream();
		
		int totalBytesRcvd = 0;
				
		while((size = in.read(buffer)) != -1) {
			fos.write(buffer, 0, size);
			totalBytesRcvd += size;
			
			System.out.println(totalBytesRcvd + "bytes received.");
		}
		
		System.out.println("Recieved File: <" + file + ".txt> | File Size: " + totalBytesRcvd);
		
		in.close();
		out.close();
		socket.close();
		
		System.out.println("Press Enter to exit...");
		console.readLine();
		console.close();
		System.exit(10);
	}

}
