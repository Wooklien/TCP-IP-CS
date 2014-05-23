import java.net.*; // for Socket, ServerSocket, and InetAddress
import java.io.*; // for IOException and Input/OutputStream
import java.util.*; // Maps-HashMap

public class Server {
	public static final int PORT = 1234;
	private static ArrayList<Integer> list = new ArrayList<Integer>();

	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			throw new IllegalArgumentException("Parameter(s): <# of Random numbers> <# of clients>");
		}

		int size = Integer.parseInt(args[0]);
		int MAX = Integer.parseInt(args[1]);
		Random rand = new Random();

		for(int i = 0; i < size; i++) {
			int rand_number = rand.nextInt(11);
			list.add(rand_number);
		}

		System.out.println("===================================================================");
		System.out.println(":: Server Started :: Listening to Port " + PORT + " ::");
		System.out.println("===================================================================");

		// Create a server socket to accept client connection requests
		ServerSocket serverSocket = new ServerSocket(PORT);
		Socket clientSocket = null;

		int active = 0;
		while (true) { // Run forever, accepting and servicing connections
			if(active <= MAX) {
				try {
					clientSocket = serverSocket.accept();
					ServerThread server = new ServerThread(clientSocket,list);
		            new Thread(server).start();
		            active++;
				}
				catch(Exception e) {
					e.printStackTrace();
				}

				if(active == MAX) {
					Notifier msg = new Notifier(clientSocket);
			        new Thread(msg).start();
				}
			}
		}
	}
		/* NOT REACHED */
}

class ServerThread implements Runnable {
	private static ArrayList<Socket> clients = new ArrayList<Socket>();
	private static ArrayList<Integer> list;
	Socket client;

	public ServerThread(Socket client, ArrayList<Integer> list) {
		this.client = client;
		this.list = list;
		add(client);
		System.out.println(client.toString());
	}

	public void run() {
		synchronized (client) {
			try {
				client.wait();
				OutputStream out = null;
				InputStream is = null;
				DataOutputStream dos = null;
				DataInputStream dis = null;

				int size = list.size();
				
				while(size != 0) {
					if(size > 0) {
						for(int n = 0; n < clients.size(); n++) {
							out = find(n).getOutputStream();
							dos = new DataOutputStream(out);
							for(int i = 0; i < 10; i++) {
								dos.writeInt(list.get(0));
								list.remove(0);
								size--;
							}
							dos.writeInt(-1);
							dos.flush();

							is = find(n).getInputStream();
							dis = new DataInputStream(is);

							list.add(dis.readInt());

							if(n == clients.size()) {
								n = 0;
							}

							if(size == 0) {
								for(int i = 0; i < clients.size(); i++) {
									out = find(i).getOutputStream();
									dos = new DataOutputStream(out);
									dos.writeInt(-2);
								}
								break;
							}
						}
					}
				}

				int sum = sum();
				System.out.println("The sum of the list is: " + sum);
				System.exit(1);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void add(Socket client) {
		clients.add(client);
	}

	private static Socket find(int i) {
		return clients.get(i);
	}

	private static void print() {
		for(int i = 0; i < clients.size(); i++) {
			System.out.println(clients.get(i).toString());
		}
	}

	private static void print_list() {
		System.out.println("Printing List...");
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	private static int sum() {
		int temp = 0;
		for(int i = 0; i < list.size(); i++) {
			temp += list.get(i);
		}
		return temp;
	}
}

class Notifier implements Runnable {
	Socket client;
     
    public Notifier(Socket client) {
        this.client = client;
    }
 
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            synchronized (client) {
                client.notifyAll();
                // msg.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }
}