import java.io.*;
import java.net.*;

public class Client extends Thread {
	public static final int PORT = 1234;

	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			throw new IllegalArgumentException("Parameter(s): <Client ID#> <Address>");
		}

		String client = args[0];
		String address = args[1];

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");

		byte[] sendData = new byte[100];

		clientSocket.connect(IPAddress, PORT);

		DatagramPacket sendPacket = null;
		String request = "1@" + client;

		sendData = request.getBytes();

		sendPacket = new DatagramPacket(sendData, sendData.length);
		clientSocket.send(sendPacket);

		System.out.println("|Connected to the server|");
		System.out.println("=========================");
		System.out.println();

		Writer w = new Writer(clientSocket, address, client);
		Listener t = new Listener(clientSocket);
	}	
}

class Listener extends Thread {
	DatagramSocket clientSocket = null;

	public static final int PACKETSIZE = 100;
	public static final String ETX = "End_of_Communication";

	public Listener(DatagramSocket clientSocket) {
		this.clientSocket = clientSocket;
		this.start();
	}

	public void run() {
		while(true) {
			byte[] receiveData = new byte[PACKETSIZE];
			DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
			try {
				clientSocket.receive(packet);
				String message = new String(packet.getData()).trim();

				if(message.equals(ETX)) {
					break;
				}
				System.out.println(message);
			}
			catch(IOException e) {
				System.err.println(e);
			}
		}
	}
}

class Writer extends Thread {
	public static final int PACKETSIZE = 100;
	public static final String ETX = "End_of_Communication";

	DatagramSocket clientSocket = null;
	String address;
	String who;
	int myAddress;

	public Writer(DatagramSocket clientSocket, String address, String who) {
		this.clientSocket = clientSocket;
		this.address = address;
		this.who = who;
		myAddress = clientSocket.getLocalPort();
		this.start();
	}

	public void run() {
		DatagramPacket sendPacket = null;

		try {
			while(true) {
				byte[] sendData = new byte[PACKETSIZE];

				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String message = br.readLine();
				String str = message;

				if(message.equals("WhoIsOnline") || message.equals(ETX)) {
					message = message + "æ" + address + "æ" + who; 
				}
				else {
					String[] parser = message.split("<<");
					who = parser[0];
					message = parser[1] + "æ" + address + "æ" + who;
				}

				sendData = message.getBytes();
				sendPacket = new DatagramPacket(sendData, sendData.length);
				clientSocket.send(sendPacket);

				if(str.equals(ETX)) {
					break;
				}
			}
			clientSocket.disconnect();
			clientSocket.close();
			System.out.println("|Socket Closed|");
			System.exit(0);
		}
		catch(IOException e) {
			System.err.println(e);
		}
	}
}