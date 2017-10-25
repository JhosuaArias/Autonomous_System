import Server_Client_Test.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{

    private As as;
    private int port;
    private ArrayList<ServerConnection> allConnections;
    ServerSocket serverSocket;
    public Server(As as, int port) {
        this.as = as;
        this.port = port;
        this.allConnections = new ArrayList<>();

        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenConnections() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(this.as ,this.serverSocket ,socket);
                allConnections.add(serverConnection);
                serverConnection.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void run(){
        System.out.println("Server on...");
        while (true) {
            this.listenConnections();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(new As(1,1,null,null),81);
        server.start();
    }
}
