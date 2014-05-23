import java.io.*;
import java.net.*;

public class Client extends Thread {
	public static final int PORT = 1234;

	public static void main(String[] args) throws IOException {
		String address;
		if(args.length != 1) {
			address = "127.0.0.1";
		}
		else {
			address = args[0];
		}

		// Create socket that is connected to server on specified port
		Socket socket = new Socket(address, 1234);

		System.out.println("|Connected to the server|");
		System.out.println("=========================");
		System.out.println();

		InputStream is = socket.getInputStream();
		DataInputStream dis = new DataInputStream(is);

		while(true) {
			int size;
			int total = 0;
			while((size = dis.readInt()) != -1) {
				if(size == -2) {
					break;
				} 
				total += size;
			}
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(total);

			if(size == -2) {
				break;
			}
		}
	}	
}