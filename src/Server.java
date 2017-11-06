
import Server_Client_Test.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{

    private As as;
    private ServerSocket serverSocket;

    public Server(As as, int port) {
        this.as = as;

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenConnections() {
        System.err.println("Server is listening");
        while (true) {
            try {
                Socket clientSocket = this.serverSocket.accept();

                ServerConnection serverConnection = new ServerConnection(this.as, clientSocket);
                serverConnection.start();
            } catch (IOException e) {
               // e.printStackTrace();
            }

        }

    }

    public void kill() {

        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("Server stopped listening");
        this.stop();

    }

    @Override
    public void run(){

        while (true) {
            this.listenConnections();
        }
    }

}
