package AsDynamics;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{

    private As as;
    private ServerSocket serverSocket;
    private ArrayList<ServerConnection> serverConnections;
    private boolean retry;

    Server(As as, int port) {
        this.as = as;
        this.serverConnections = new ArrayList<>();

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenConnections() {
        this.as.depositMessage("Listener server: ON");
        this.as.getLogWriter().writeIntoLog("Listener server: ON");

        while (this.retry) {
            try {
                Socket clientSocket = this.serverSocket.accept();

                ServerConnection serverConnection = new ServerConnection(this.as, clientSocket);
                this.serverConnections.add(serverConnection);
                serverConnection.start();
            } catch (IOException e) {
               // e.printStackTrace();
                this.as.getLogWriter().writeIntoLog("Error the server cannot establish a connection");
            }

        }

    }

    void kill() {

        for (ServerConnection connection : this.serverConnections) {
            connection.kill();
            System.err.println("Server connection: OFF");
            this.as.getLogWriter().writeIntoLog("Server connection: OFF");
        }

        this.retry = false;
        boolean closed = false;
        while (!closed) {
            try {
                this.serverSocket.close();
                closed = true;
            } catch (IOException e) {
                System.err.println("Couldn't close the server socket, retrying...");
                this.as.getLogWriter().writeIntoLog("Couldn't close the server socket, retrying...");
            }
        }

    }

    @Override
    public void run(){

        this.retry = true;
        while (this.retry) {
            this.listenConnections();
        }
    }

}
