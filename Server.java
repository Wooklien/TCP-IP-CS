import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static final int PORT = 1234;
    public static final int PACKETSIZE = 100;

    public static int connection = 0;

    public static void main(String args[]) throws IOException {
        // Connection Parameters
        DatagramSocket serverSocket = null;
        int maxConnection = 3;             // Max threads at any time

        byte[] receiveData = new byte[PACKETSIZE];

        // Create a place for the client to send data too.
        System.out.println("Waiting for client requests...");
        

        try {
            // Setup socket
            serverSocket = new DatagramSocket(PORT);
            DatagramSocket clientSocket = null;

            while(true) {
                byte[] name = new byte[PACKETSIZE];
                DatagramPacket packet = new DatagramPacket(name, name.length);
                if(connection < maxConnection) {
                    serverSocket.receive(packet); 

                    String request = new String(packet.getData()).trim();

                    String[] requestTok = request.split("@");

                    if(requestTok[0].equals("1")) {
                        connection++;
                        String client = requestTok[1];
                        System.out.println("Connected to " + client + "...");

                        InetAddress clientAddress = packet.getAddress();
                        int port = packet.getPort();
                        clientSocket = serverSocket;

                        ServerThread server = new ServerThread(clientSocket, client, port);
                        new Thread(server).start();
                    }
                }            
            }
        }
        catch (Exception e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
        finally {
            if(serverSocket != null) 
                serverSocket.close();
        }
    }
}

class ServerThread implements Runnable {
    public static final int PACKETSIZE = 100;
    public static final String ETX = "End_of_Communication";

    public static Map<String, Integer> clients = new HashMap<String, Integer>();

    private DatagramSocket serverSocket = null;
    private String clientName;

    // Constructor
    public ServerThread(DatagramSocket socket, String clientName, int port) {
        serverSocket = socket;
        this.clientName = clientName;
        addClient(clientName, port);
        printClients();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);

            while(true) {

                if(clients.size() < 1) {
                    break;
                }

                byte[] rData = new byte[PACKETSIZE];
                byte[] sData = new byte[PACKETSIZE];
                DatagramPacket receivePacket = new DatagramPacket(rData, rData.length);
                serverSocket.receive(receivePacket);
                String message = new String(receivePacket.getData()).trim();

                String[] tokens = message.split("æ");

                InetAddress IPAddress = InetAddress.getByName(tokens[1]);

                DatagramPacket sendPacket = null;
                String client = getClient(receivePacket, clients);
                int port = 0;

                if(tokens[2].equals("ALL")) {
                    for (Map.Entry<String, Integer> entry : clients.entrySet()) {
                        if(!client.equals(entry.getKey())) {
                            port = entry.getValue();
                            String newMessage = "<" + client + ">: " + tokens[0];
                            sData = newMessage.getBytes();
                            sendPacket = new DatagramPacket(sData, sData.length, IPAddress, port);
                            serverSocket.send(sendPacket);
                        }
                    }
                }
                else {
                    port = clients.get(tokens[2]);

                    if(tokens[0].equals(ETX)) {
                        System.out.println("Closed for " + client + "...");
                        sData = tokens[0].getBytes();
                        sendPacket = new DatagramPacket(sData, sData.length, IPAddress, port);
                        serverSocket.send(sendPacket);
                        clients.remove(client);
                        Server.connection--;
                    }
                    else if(tokens[0].equals("WhoIsOnline")) {
                        whoIsOnline(serverSocket, receivePacket, clients, IPAddress);
                    }
                    else {
                        String newMessage = "<" + client + ">: " + tokens[0];
                        sData = newMessage.getBytes();
                        sendPacket = new DatagramPacket(sData, sData.length, IPAddress, port);
                        serverSocket.send(sendPacket);
                    }    
                }          
            }
            serverSocket.disconnect();
            serverSocket.close();
            System.exit(0);
        }
        catch (Exception e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }

    private static void addClient(String id, int port) {
        clients.put(id,port);
    }

    private static void printClients() {
        for (Map.Entry<String, Integer> entry : clients.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }

    private static void whoIsOnline(DatagramSocket server, DatagramPacket receivePacket, Map<String, Integer> clients, InetAddress IPAddress) {
        byte[] sData = new byte[PACKETSIZE];
        for (Map.Entry<String, Integer> entry : clients.entrySet()) {
            try {
                int port = receivePacket.getPort();
                String client = entry.getKey() + " is Online.";

                sData = client.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sData, sData.length, IPAddress, port);
                server.send(sendPacket);               
            }
            catch (Exception e) {
                System.err.println("System: " + e.getMessage());
            }
        }
    }

    private static String getClient(DatagramPacket packet, Map<String, Integer> clients) {
        String client = null;
        for (Map.Entry<String, Integer> entry : clients.entrySet()) {
            if(entry.getValue().equals(packet.getPort())) {
                client = entry.getKey();
            }
        }
        return client;
    }
}

